package com.ca.sport.service.test;

import com.ca.sport.bean.TestApp;
import com.ca.sport.dao.TestAppDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("testAppService")
public class TestAppServiceImpl implements TestAppService {
    @Resource
    private TestAppDao testAppDao;

    @Override
    public Integer insert(TestApp testApp) {
        return testAppDao.insert(testApp);
    }

    @Override
    public Integer update(TestApp testApp) {
        return testAppDao.update(testApp);
    }

    @Override
    public TestApp selectById(Long id) {
        return testAppDao.selectById(id);
    }
}
