package com.ca.sport.message;


import com.ca.sport.CmsService;
import com.ca.sport.bean.product.Color;
import com.ca.sport.bean.product.Product;
import com.ca.sport.bean.product.Sku;
import com.ca.sport.staticpage.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.*;

/**
 * 处理类
 *
 * @author lx
 */
public class CustomMessageListener implements MessageListener {

    @Autowired
    private StaticPageService staticPageService;
    @Autowired
    private CmsService cmsService;

    @Override
    public void onMessage(Message message) {
        // TODO Auto-generated method stub
        ActiveMQTextMessage am = (ActiveMQTextMessage) message;
        try {
            System.out.println("ActiveMq中的消息是-cms:" + am.getText());
            String id = am.getText();
            //数据
            Map<String, Object> root = new HashMap<>();
            //商品
            Product product = cmsService.selectProductById(Long.parseLong(id));
            //sku
            List<Sku> skus = cmsService.selectSkuListByProductId(Long.parseLong(id));

            //遍历一次  相同不要
            Set<Color> colors = new HashSet<>();
            for (Sku sku : skus) {
                colors.add(sku.getColor());
            }
            root.put("product", product);
            root.put("skus", skus);
            root.put("colors", colors);
            //静态化
            staticPageService.productStaticPage(root, id);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
