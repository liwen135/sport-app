package com.ca.sport.service.product;

import com.ca.sport.bean.BuyerCart;
import com.ca.sport.bean.BuyerItem;
import com.ca.sport.bean.product.Sku;
import com.ca.sport.bean.product.SkuQuery;
import com.ca.sport.dao.product.ColorDao;
import com.ca.sport.dao.product.ProductDao;
import com.ca.sport.dao.product.SkuDao;
import com.ca.sport.product.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 库存管理
 *
 * @author lx
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {


    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;

    //商品ID 查询 库存结果集
    public List<Sku> selectSkuListByProductId(Long productId) {
        SkuQuery skuQuery = new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(productId);
        List<Sku> skus = skuDao.selectByExample(skuQuery);
        //15
        for (Sku sku : skus) {
            // 3条Sql  一级缓存
            sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }

    //修改
    public void updateSkuById(Sku sku) {
        skuDao.updateByPrimaryKeySelective(sku);
    }

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

    @Autowired
    private Jedis jedis;

    //保存商品到Redis中
    public void insertBuyerCartToRedis(BuyerCart buyerCart, String username) {
        //判断购物项的长度大于0
        List<BuyerItem> items = buyerCart.getItems();
        if (items.size() > 0) {
            for (BuyerItem buyerItem : items) {
                //判断是否已经存在了
                if (jedis.hexists("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()))) {
                    //加数量
                    jedis.hincrBy("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()), buyerItem.getAmount());
                } else {
                    jedis.hset("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()), String.valueOf(buyerItem.getAmount()));
                }
            }
        }
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
