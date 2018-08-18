package com.ca.sport.staticpage;

import java.util.Map;

public interface StaticPageService {


    //静态化 商品  ActiveMQ
    void productStaticPage(Map<String, Object> root, String id);

}
