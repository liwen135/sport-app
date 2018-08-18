package com.ca.sport.service.product;


import com.ca.sport.bean.product.*;
import com.ca.sport.dao.product.ColorDao;
import com.ca.sport.dao.product.ProductDao;
import com.ca.sport.dao.product.SkuDao;
import com.ca.sport.page.Pagination;
import com.ca.sport.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;

/**
 * 商品
 *
 * @author lx
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    //分页对象
    public Pagination selectPaginationByQuery(Integer pageNo, String name
            , Long brandId, Boolean isShow) {
        ProductQuery productQuery = new ProductQuery();
        productQuery.setPageNo(Pagination.cpn(pageNo));
        //排序
        productQuery.setOrderByClause("id desc");
        ProductQuery.Criteria createCriteria = productQuery.createCriteria();
        StringBuilder params = new StringBuilder();
        if (null != name) {
            createCriteria.andNameLike("%" + name + "%");
            params.append("name=").append(name);
        }
        if (null != brandId) {
            createCriteria.andBrandIdEqualTo(brandId);
            params.append("&brandId=").append(brandId);
        }
        if (null != isShow) {
            createCriteria.andIsShowEqualTo(isShow);
            params.append("&isShow=").append(isShow);
        } else {
            createCriteria.andIsShowEqualTo(false);
            params.append("&isShow=").append(false);
        }

        Pagination pagination = new Pagination(
                productQuery.getPageNo(),
                productQuery.getPageSize(),
                productDao.countByExample(productQuery),
                productDao.selectByExample(productQuery)
        );
        String url = "/product/list.do";

        pagination.pageView(url, params.toString());

        return pagination;
    }

    //加载颜色
    @Autowired
    private ColorDao colorDao;

    //颜色结果集
    public List<Color> selectColorList() {
        ColorQuery colorQuery = new ColorQuery();
        colorQuery.createCriteria().andParentIdNotEqualTo(0L);
        return colorDao.selectByExample(colorQuery);
    }

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private Jedis jedis;

    //商品保存
    public void insertProduct(Product product) {
        //保存商品
        Long id = jedis.incr("pno");
        product.setId(id);
//		下架状态 后台程序写的
        product.setIsShow(false);
//		删除  后台程序写的不删除
        product.setIsDel(true);
        productDao.insertSelective(product);
        //返回ID
        //保存SKU
        String[] colors = product.getColors().split(",");
        String[] sizes = product.getSizes().split(",");
        //颜色
        for (String color : colors) {
            for (String size : sizes) {
                //保存SKU
                Sku sku = new Sku();
                //商品ＩＤ
                sku.setProductId(product.getId());
                //颜色
                sku.setColorId(Long.parseLong(color));
                //尺码
                sku.setSize(size);
                //市场价
                sku.setMarketPrice(999f);
                //售价
                sku.setPrice(666f);
                //运费
                sku.setDeliveFee(8f);
                //库存
                sku.setStock(0);
                //限制
                sku.setUpperLimit(200);
                //时间
                sku.setCreateTime(new Date());

                skuDao.insertSelective(sku);

            }
        }
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    //上架
    public void isShow(Long[] ids) {
        Product product = new Product();
        //上架
        product.setIsShow(true);
        for (final Long id : ids) {
            product.setId(id);
            //商品状态的变更
            productDao.updateByPrimaryKeySelective(product);
            //发送消息 到ActiveMQ中   brandId
//			jmsTemplate.send("brandId", messageCreator);
            jmsTemplate.send(new MessageCreator() {

                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }

            });

            //TODO 静态化
        }
    }
}
