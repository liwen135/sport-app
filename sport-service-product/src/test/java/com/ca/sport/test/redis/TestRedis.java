package com.ca.sport.test.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class TestRedis {

    @Test
    public void test01(){
        Jedis jedis = new Jedis("192.168.47.100", 6379);
        String pno = jedis.get("pno");
        System.out.println(pno);
    }
}
