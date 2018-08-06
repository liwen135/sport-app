package com.ca.sport.test.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config.xml")
public class TestRedis {

    @Resource
    private Jedis jedis;

    @Test
    public void test01() {
        Jedis jedis = new Jedis("192.168.198.134", 6379);
        String pno = jedis.get("pno");
        System.out.println(pno);
    }

    @Test
    public void test02() {
        String pno = jedis.get("pno");
        System.out.println(pno);
        Long incr = jedis.incrBy("pno", 5);
        System.out.println(incr);
        System.out.println(jedis.get("pno"));
    }
}
