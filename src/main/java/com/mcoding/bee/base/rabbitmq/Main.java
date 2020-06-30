package com.mcoding.bee.base.rabbitmq;

import java.util.concurrent.TimeUnit;

/**
 * @author wzt on 2020/6/6.
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setName("thread 111111");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                long i = 0;
                while (true) {
                    i ++;
                }
            }
        });

        t2.setName("thread 22222");
        t.start();
        t2.start();

    }

}
