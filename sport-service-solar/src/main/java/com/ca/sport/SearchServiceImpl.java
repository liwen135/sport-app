package com.ca.sport;


import com.ca.sport.bean.product.Product;
import com.ca.sport.bean.product.ProductQuery;
import com.ca.sport.bean.product.Sku;
import com.ca.sport.bean.product.SkuQuery;
import com.ca.sport.dao.product.ProductDao;
import com.ca.sport.dao.product.SkuDao;
import com.ca.sport.page.Pagination;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全文检索  用的SOlr
 * @author lx
 *
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SolrServer solrServer;
	
	//全文检索
	@Override
	public Pagination selectPaginationByQuery(Integer pageNo, String keyword, Long brandId, String price) throws Exception {
		//创建包装类
	    ProductQuery productQuery = new ProductQuery();
	    //当前页
	    productQuery.setPageNo(Pagination.cpn(pageNo));
	    //每页显示 12条
	    productQuery.setPageSize(12);
	    
	    //拼接条件
	    StringBuilder params = new StringBuilder();
	    
		List<Product> products = new ArrayList<Product>();
		SolrQuery solrQuery = new SolrQuery();
		//关键词
		solrQuery.set("q", "name_ik:" + keyword);
		params.append("keyword=").append(keyword);
		//过滤条件
		if(null != brandId){
			solrQuery.addFilterQuery("brandId:" + brandId);
		}
		//价格   0-99 1600 
		if(null != price){
			String[] p = price.split("-");
			if(p.length == 2){
				solrQuery.addFilterQuery("price:[" + p[0] + " TO " + p[1] + "]");
			}else{
				solrQuery.addFilterQuery("price:[" + p[0] + " TO *]");
			}
		}
		
		//高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("name_ik");
		// 样式  <span style='color:red'>2016</span>
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		//排序
		solrQuery.addSort("price", ORDER.asc);
		//分页  limit 开始行 ， 每页显示条数
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		//执行查询
		QueryResponse response = solrServer.query(solrQuery);
		
		//取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		// Map K : V    442 : Map
		// Map K : V    name_ik : List<String>
		// List<String> list   2016年最新款191期卖的瑜伽服最新限量纯手工制作细心打造商户经典买一送一清 list.get(0);
		//结果集
		SolrDocumentList docs = response.getResults();
		//发现的条数 （总条件）构建分页时用到
		long numFound = docs.getNumFound();
		for (SolrDocument doc : docs) {
			//创建商品对象
			Product product = new Product();
			//商品ID 
			String id = (String) doc.get("id");
			product.setId(Long.parseLong(id));
			//商品名称  ik
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			product.setName(list.get(0));
		/*	String name = (String) doc.get("name_ik");
			product.setName(name);*/
			//图片
			String url = (String) doc.get("url");
			product.setImgUrl(url);//img,img2,img3
			//价格 售价   select price from bbs_sku where product_id =442 order by price asc limit 0,1
			product.setPrice((Float) doc.get("price"));
			//品牌ID Long
			product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));
			
			products.add(product);
		}
		//构建分页对象
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				(int)numFound,
				products
				);
		//页面展示
		String url = "/search";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}
	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	//保存商品信息到Solr服务器
	@Override
	public void insertProductToSolr(Long id){
		//TODO 保存商品信息到SOlr服务器
		SolrInputDocument doc = new SolrInputDocument();
		//商品ID 
		doc.setField("id", id);
		//商品名称  ik
		Product p = productDao.selectByPrimaryKey(id);
		doc.setField("name_ik", p.getName());
		//图片
		doc.setField("url", p.getImages()[0]);
		//价格 售价   select price from bbs_sku where product_id =442 order by price asc limit 0,1
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		skuQuery.setFields("price");
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		doc.setField("price", skus.get(0).getPrice());
		//品牌ID Long
		doc.setField("brandId", p.getBrandId());
		//时间  可选
		try {
			solrServer.add(doc);
			solrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
