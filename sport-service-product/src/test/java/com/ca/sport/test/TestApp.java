package com.ca.sport.test;

import com.ca.sport.service.test.TestAppService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class TestApp {

    @Resource
    private TestAppService testAppService;

    @Test
    public void testInsert() {
        com.ca.sport.bean.TestApp testApp = new com.ca.sport.bean.TestApp();
        testApp.setAge(10);
        testApp.setName("小米");
        testAppService.insert(testApp);
    }
}
