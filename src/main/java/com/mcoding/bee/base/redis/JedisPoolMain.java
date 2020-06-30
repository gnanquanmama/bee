package com.mcoding.bee.base.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.TimeUnit;

/**
 * @author wzt on 2020/6/7.
 * @version 1.0
 */
public class JedisPoolMain {

    public static void main(String[] args) {

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxTotal(5);
        poolConfig.setMaxIdle(5);

        JedisPool jedisPool = new JedisPool(poolConfig, "47.95.192.230", 6379, 0, "redis#123", 0, "jedis_pools");

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    super.onMessage(channel, message);
                }
            }, "+switch-master");

            System.out.println("hello youosfsjfljsdf");
            jedis.get("hello");

            TimeUnit.SECONDS.sleep(10);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }


}
