package com.ca.sport.dao;

import com.ca.sport.bean.TestApp;

public interface TestAppDao {

    Integer insert(TestApp testApp);

    Integer update(TestApp testApp);

    TestApp selectById(Long id);

}
