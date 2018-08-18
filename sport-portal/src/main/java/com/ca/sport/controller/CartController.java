package com.ca.sport.controller;

import com.ca.sport.bean.BuyerCart;
import com.ca.sport.bean.BuyerItem;
import com.ca.sport.bean.product.Sku;
import com.ca.sport.product.SkuService;
import com.ca.sport.user.BuyerService;
import com.ca.sport.user.SessionProvider;
import com.ca.sport.utils.RequestUtils;
import com.ca.sport.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.List;

/**
 * 购物车
 * 去购物车页面
 * 添加商品到购物车
 * 删除
 * —+
 *
 * @author lx
 */
@Controller
public class CartController {

    @Autowired
    private SessionProvider sessionProvider;

    //加入购物车
    @RequestMapping(value = "/addCart")
    public String addCart(Long skuId, Integer amount, Model model
            , HttpServletRequest request, HttpServletResponse response) throws Exception {

        ObjectMapper om = new ObjectMapper();
        //不要NULL 不要转了
        om.setSerializationInclusion(Include.NON_NULL);
        //声明
        BuyerCart buyerCart = null;

//		1：从Request中取Cookies、
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            //遍历Cookie 取出之前的购物车
            for (Cookie cookie : cookies) {
//		2：判断Cookie中没有购物车
                if (Constants.BUYER_CART.equals(cookie.getName())) {
                    //转回对象
                    buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
                    break;
                }
            }
        }
//		3：有 
//		4：没有 创建购物车
        //判断购物车是否为null
        if (null == buyerCart) {
            buyerCart = new BuyerCart();
        }
//		5：追加当前商品到购物车
        Sku sku = new Sku();
        //ID
        sku.setId(skuId);
        BuyerItem buyerItem = new BuyerItem();
        buyerItem.setSku(sku);
        //Amount
        buyerItem.setAmount(amount);
        //追加商品到购物车
        buyerCart.addItem(buyerItem);

        //用户是否登陆
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
        if (null != username) {
//			3：有  把购物车中商品添加到Redis的购物车中，
            skuService.insertBuyerCartToRedis(buyerCart, username);
            //清理之前Cookie4
            Cookie cookie = new Cookie(Constants.BUYER_CART, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
//			6：创建Cookie 把新购物车放进去
            StringWriter w = new StringWriter();
            om.writeValue(w, buyerCart);
            Cookie cookie = new Cookie(Constants.BUYER_CART, w.toString());
            //设置时间     写程序1天
            cookie.setMaxAge(60 * 60 * 24);
            //设置路径
            cookie.setPath("/");
            //上线后  申请域名
//			7：保存写回浏览器
            response.addCookie(cookie);
        }
        return "redirect:/toCart";
    }

    @Autowired
    private SkuService skuService;

    //去购物车页面
    @RequestMapping(value = "/toCart")
    public String toCart(Model model
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
//		1：从Request中取Cookies、遍历Cookie 取出之前的购物车
        ObjectMapper om = new ObjectMapper();
        //不要NULL 不要转了
        om.setSerializationInclusion(Include.NON_NULL);
        //声明
        BuyerCart buyerCart = null;

//		1：从Request中取Cookies、
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            //遍历Cookie 取出之前的购物车
            for (Cookie cookie : cookies) {
//		2：判断Cookie中没有购物车
                if (Constants.BUYER_CART.equals(cookie.getName())) {
                    //转回对象
                    buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
                    break;
                }
            }
        }
        //用户是否登陆
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
        if (null != username) {
//		登陆
//		2：再把购物车保存到Redis中，清理Cookie
            if (null != buyerCart) {
                skuService.insertBuyerCartToRedis(buyerCart, username);
                //清理之前Cookie4
                Cookie cookie = new Cookie(Constants.BUYER_CART, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
//		3：从Redis中取出所有的购物车  Service层

            buyerCart = skuService.selectBuyerCartFromRedis(username);
        }

        //判断有或没有
        if (null != buyerCart) {
//		2:有   购物车数量及SKUID
//		把购物车装满
            List<BuyerItem> items = buyerCart.getItems();
            for (BuyerItem buyerItem : items) {
                buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
            }

        }
//		3：没有
//		回显购物车内容
        model.addAttribute("buyerCart", buyerCart);
//		跳转到购物车页面
        return "cart";

    }

    //结算
    @RequestMapping(value = "/buyer/trueBuy")
    public String trueBuy(Long[] skuIds, Model model, HttpServletRequest request, HttpServletResponse response) {
//		1）	购物车中必须有商品
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
        BuyerCart buyerCart = skuService.selectBuyerCartFromRedis(username);
        List<BuyerItem> items = buyerCart.getItems();
        //标记
        Boolean flag = false;
        if (items.size() > 0) {
//			把购物车装满
            for (BuyerItem buyerItem : items) {
                buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
//		2）	购物车中必须有库存
                if (buyerItem.getAmount() > buyerItem.getSku().getStock()) {
                    //无货
                    buyerItem.setIsHave(false);
                    flag = true;
                }
            }
            //至少一款无货
            if (flag) {
                //视图 有一个无货不能进入下个订单页面
                model.addAttribute("buyerCart", buyerCart);
                return "cart";
            }
        } else {
            return "redirect:/toCart";
        }
        //视图  如果都有货进入下个订单页面
        return "order";

    }

    @Autowired
    private BuyerService buyerService;

    //提交订单
    @RequestMapping(value = "/buyer/submitOrder")
    public String submitOrder(Order order, Model model, HttpServletRequest request, HttpServletResponse response) {
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
        //用户
        buyerService.insertOrder(order, username);

        return "success";

    }
}
