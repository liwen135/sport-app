package com.ca.sport.user;

import com.ca.sport.bean.BuyerCart;
import com.ca.sport.bean.BuyerItem;
import com.ca.sport.bean.order.Detail;
import com.ca.sport.bean.order.Order;
import com.ca.sport.bean.product.Sku;
import com.ca.sport.bean.user.Buyer;
import com.ca.sport.bean.user.BuyerQuery;
import com.ca.sport.dao.order.DetailDao;
import com.ca.sport.dao.order.OrderDao;
import com.ca.sport.dao.product.ColorDao;
import com.ca.sport.dao.product.ProductDao;
import com.ca.sport.dao.product.SkuDao;
import com.ca.sport.dao.user.BuyerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 用户管理
 *
 * @author lx
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private BuyerDao buyerDao;

    //通过用户名查询用户对象
    public Buyer selectBuyerByUsername(String username) {
        BuyerQuery buyerQuery = new BuyerQuery();
        buyerQuery.createCriteria().andUsernameEqualTo(username);

        List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
        if (null != buyers && buyers.size() > 0) {
            return buyers.get(0);
        }
        return null;
    }

    @Autowired
    private Jedis jedis;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private DetailDao detailDao;

    //保存订单
    public void insertOrder(Order order, String username) {
//		ID（订单编号）全国唯一Redis
        Long id = jedis.incr("oid");
        order.setId(id);
        //加载购物车
        BuyerCart buyerCart = selectBuyerCartFromRedis(username);
        List<BuyerItem> items = buyerCart.getItems();
        for (BuyerItem buyerItem : items) {
            buyerItem.setSku(selectSkuById(buyerItem.getSku().getId()));
        }
//		运费　　　　　由购物车提供
        order.setDeliverFee(buyerCart.getFee());
//		总价
        order.setTotalPrice(buyerCart.getTotalPrice());
//		订单金额
        order.setOrderPrice(buyerCart.getProductPrice());
//		支付状态：0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
        if (order.getPaymentWay() == 1) {
            order.setIsPaiy(0);
        } else {
            order.setIsPaiy(1);
        }
//		订单状态：0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
        order.setOrderState(0);
//		时间  ：  后台程序自己写的
        order.setCreateDate(new Date());
//		用户ID ：   前台用户做注册 （ 用户名、密码）用户ID  由Redis生成   用户名： 用户ID  保存到Redis中   5个
        String uid = jedis.get(username);
        order.setBuyerId(Long.parseLong(uid));
        //保存订单
        orderDao.insertSelective(order);
        //保存订单详情
        for (BuyerItem buyerItem : items) {
            Detail detail = new Detail();
            //		ID
            //		订单ID
            detail.setOrderId(id);
            //		商品编号
            detail.setProductId(buyerItem.getSku().getProductId());
            //		商品名称
            detail.setProductName(buyerItem.getSku().getProduct().getName());
            //		颜色
            detail.setColor(buyerItem.getSku().getColor().getName());
            //		尺码
            detail.setSize(buyerItem.getSku().getSize());
            //		价格
            detail.setPrice(buyerItem.getSku().getPrice());
            //		数量
            detail.setAmount(buyerItem.getAmount());
            //		购物车提供
            detailDao.insertSelective(detail);
        }


        //清空购物车
        jedis.del("buyerCart:fbb2016");
        //练习 hash 指定 K


    }

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private ProductDao productDao;

    //通过SKUID查询SKU对象
    public Sku selectSkuById(Long id) {
        //SKu对象
        Sku sku = skuDao.selectByPrimaryKey(id);
        //商品对象
        sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
        //颜色对象
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        return sku;

    }

    //取出购物车从Redis
    public BuyerCart selectBuyerCartFromRedis(String username) {
        BuyerCart buyerCart = new BuyerCart();
        Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
        if (null != hgetAll) {
            Set<Entry<String, String>> entrySet = hgetAll.entrySet();
            for (Entry<String, String> entry : entrySet) {
//				5：追加当前商品到购物车
                Sku sku = new Sku();
                //ID
                sku.setId(Long.parseLong(entry.getKey()));
                BuyerItem buyerItem = new BuyerItem();
                buyerItem.setSku(sku);
                //Amount
                buyerItem.setAmount(Integer.parseInt(entry.getValue()));
                //追加商品到购物车
                buyerCart.addItem(buyerItem);

            }
        }
        return buyerCart;
    }
}
