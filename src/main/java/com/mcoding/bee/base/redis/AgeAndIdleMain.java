package com.mcoding.bee.base.redis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @author wzt on 2020/6/7.
 * @version 1.0
 */
public class AgeAndIdleMain {

    public static void main(String[] args) throws InterruptedException {

        String key = "hello";

        Jedis jedis = new Jedis("47.95.192.230", 6379);
        jedis.auth("redis#123");

        System.out.println(jedis.get(key));

        TimeUnit.SECONDS.sleep(31);

        System.out.println(jedis.ping());

        TimeUnit.SECONDS.sleep(5);

        jedis.close();
    }

}
