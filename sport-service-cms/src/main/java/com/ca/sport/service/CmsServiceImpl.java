package com.ca.sport.service;

import com.ca.sport.CmsService;
import com.ca.sport.bean.product.Product;
import com.ca.sport.bean.product.Sku;
import com.ca.sport.bean.product.SkuQuery;
import com.ca.sport.dao.product.ColorDao;
import com.ca.sport.dao.product.ProductDao;
import com.ca.sport.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论
 * 晒单
 * 广告
 * 静态化
 *
 * @author lx
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;

    //查询商品
    public Product selectProductById(Long productId) {

        return productDao.selectByPrimaryKey(productId);
    }

    //查询Sku结果集 (包括颜色）  有货
    public List<Sku> selectSkuListByProductId(Long productId) {
        SkuQuery skuQuery = new SkuQuery(); //大于0
        skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0);
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        for (Sku sku : skus) {
            sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }
}
