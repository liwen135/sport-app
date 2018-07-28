package com.ca.sport.service.test;

import com.ca.sport.bean.TestApp;

public interface TestAppService {

    Integer insert(TestApp testApp);

    Integer update(TestApp testApp);

    TestApp selectById(Long id);
}
