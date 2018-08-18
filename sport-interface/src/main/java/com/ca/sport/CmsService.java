package com.ca.sport;


import com.ca.sport.bean.product.Product;
import com.ca.sport.bean.product.Sku;

import java.util.List;

public interface CmsService {


    //查询商品
    Product selectProductById(Long productId);

    //查询Sku结果集 (包括颜色）  有货
    List<Sku> selectSkuListByProductId(Long productId);

}
