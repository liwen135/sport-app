package com.ca.sport;


import com.ca.sport.page.Pagination;

public interface SearchService {


    //全文检索
    //全文检索
    Pagination selectPaginationByQuery(Integer pageNo, String keyword, Long brandId, String price) throws Exception;

    //保存商品信息到Solr服务器
    void insertProductToSolr(Long id);

}
