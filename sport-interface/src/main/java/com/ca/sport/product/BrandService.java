package com.ca.sport.product;

import com.ca.sport.bean.product.Brand;
import com.ca.sport.page.Pagination;

import java.util.List;

public interface BrandService {

    //查询分页对象
    Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo);

    //查询结果集
    List<Brand> selectBrandListByQuery(Integer isDisplay);

    //通过ID查询品牌
    Brand selectBrandById(Long id);

    //修改
    void updateBrandById(Brand brand);

    //删除
    void deletes(Long[] ids);

    //查询 从Redis中
    List<Brand> selectBrandListFromRedis();
}
