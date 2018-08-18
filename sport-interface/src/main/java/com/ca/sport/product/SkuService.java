package com.ca.sport.product;

import com.ca.sport.bean.BuyerCart;
import com.ca.sport.bean.product.Sku;

import java.util.List;

public interface SkuService {

    //商品ID 查询 库存结果集
    List<Sku> selectSkuListByProductId(Long productId);

    //修改
    void updateSkuById(Sku sku);

    //通过SKUID查询SKU对象
    Sku selectSkuById(Long id);


    //保存商品到Redis中
    void insertBuyerCartToRedis(BuyerCart buyerCart, String username);


    //取出购物车从Redis
    BuyerCart selectBuyerCartFromRedis(String username);

}
