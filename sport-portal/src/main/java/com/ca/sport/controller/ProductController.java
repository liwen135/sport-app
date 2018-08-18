package com.ca.sport.controller;


import com.ca.sport.CmsService;
import com.ca.sport.SearchService;
import com.ca.sport.bean.product.Brand;
import com.ca.sport.bean.product.Product;
import com.ca.sport.bean.product.Sku;
import com.ca.sport.page.Pagination;
import com.ca.sport.product.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 前台商品
 *
 * @author lx
 */
@Controller
public class ProductController {

    //去首页  入口
    @RequestMapping(value = "/")
    public String index(Model model) throws Exception {
        List<Ad> ads = new ArrayList<>();
        Ad ad = new Ad();
        ad.setSrcC("http://img20.360buyimg.com/da/jfs/t2194/146/1422356273/89641/fa42d4d6/56a1a803N4dc5d45b.jpg");
        ad.setSrc("http://img20.360buyimg.com/da/jfs/t2194/146/1422356273/89641/fa42d4d6/56a1a803N4dc5d45b.jpg");
        Ad ad1 = new Ad();
        ad1.setSrcC("http://img14.360buyimg.com/da/jfs/t1945/248/2151313863/99141/611f128b/56a4bf99N2bd3a226.jpg");
        ad1.setSrc("http://img14.360buyimg.com/da/jfs/t1945/248/2151313863/99141/611f128b/56a4bf99N2bd3a226.jpg");
        Ad ad2 = new Ad();
        ad2.setSrcC("//img13.360buyimg.com/da/jfs/t1999/232/1443656462/100912/aa50e8f9/56a1d267N802300a0.jpg");
        ad2.setSrc("//img13.360buyimg.com/da/jfs/t1999/232/1443656462/100912/aa50e8f9/56a1d267N802300a0.jpg");
        Ad ad3 = new Ad();
        ad3.setSrcC("//img10.360buyimg.com/da/jfs/t2029/47/2070421966/100441/bb0feba8/569d9d63Nf3ac0787.jpg");
        ad3.setSrc("//img10.360buyimg.com/da/jfs/t2029/47/2070421966/100441/bb0feba8/569d9d63Nf3ac0787.jpg");
        Ad ad4 = new Ad();
        ad4.setSrc("//img13.360buyimg.com/da/jfs/t1876/340/2212333876/171444/76104f57/56a1bb56N9ec74a8d.jpg");
        ads.add(ad);
        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);
        ads.add(ad4);
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(Include.NON_NULL);
        String aa = om.writeValueAsString(ads);
        model.addAttribute("aa", aa);
        return "index";
    }

    @Autowired
    private SearchService searchService;
    @Autowired
    private BrandService brandService;

    //搜索
    @RequestMapping(value = "/search")
    public String search(Integer pageNo, String keyword, Long brandId, String price, Model model) throws Exception {
        //查询品牌结果集 Redis中查
        List<Brand> brands = brandService.selectBrandListFromRedis();
        model.addAttribute("brands", brands);
        model.addAttribute("brandId", brandId);
        model.addAttribute("price", price);

        //已选条件 容器 Map
        Map<String, String> map = new HashMap<String, String>();
        //品牌
        if (null != brandId) {
            for (Brand brand : brands) {
                if (brandId == brand.getId()) {
                    map.put("品牌", brand.getName());
                    break;
                }
            }
        }
        //价格  0-99     1600
        if (null != price) {
            if (price.contains("-")) {
                map.put("价格", price);
            } else {
                map.put("价格", price + "以上");
            }
        }

        model.addAttribute("map", map);

        Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword, brandId, price);
        model.addAttribute("pagination", pagination);
        model.addAttribute("keyword", keyword);
        return "search";
    }

    @Autowired
    private CmsService cmsService;

    //去商品详情页面
    @RequestMapping(value = "/product/detail")
    public String detail(Long id, Model model) {
        //商品
        Product product = cmsService.selectProductById(id);
        //sku
        List<Sku> skus = cmsService.selectSkuListByProductId(id);

        //遍历一次  相同不要
        Set<Color> colors = new HashSet<>();
        for (Sku sku : skus) {
            colors.add(sku.getColor());
        }


        model.addAttribute("product", product);
        model.addAttribute("skus", skus);
        model.addAttribute("colors", colors);

        return "product";
    }

}
