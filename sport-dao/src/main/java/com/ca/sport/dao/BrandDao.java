package com.ca.sport.dao;

import com.ca.sport.bean.Brand;

import java.util.List;

public interface BrandDao {

  List<Brand> selectList(BrandQuery query);
}
