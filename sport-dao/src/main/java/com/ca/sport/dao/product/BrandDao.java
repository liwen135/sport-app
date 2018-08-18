package com.ca.sport.dao.product;

import com.ca.sport.bean.product.Brand;
import com.ca.sport.bean.product.BrandQuery;

import java.util.List;

public interface BrandDao {

    /**
     * 查询结果集
     *
     * @param query
     * @return
     */
    List<Brand> selectList(BrandQuery query);


    /**
     * 查询总条数
     *
     * @param brandQuery
     * @return
     */
    Integer selectCount(BrandQuery brandQuery);


    /**
     * 通过ID查询品牌
     *
     * @param id
     * @return
     */
    Brand selectBrandById(Long id);


    /**
     * 修改
     *
     * @param brand
     */
    void updateBrandById(Brand brand);


    /**
     * 删除
     *
     * @param ids
     */
    void deletes(Long[] ids);
}
