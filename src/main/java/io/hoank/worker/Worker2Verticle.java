package io.hoank.worker;

import io.vertx.core.AbstractVerticle;

import java.util.concurrent.TimeUnit;

/**
 * Created by hoank92 on May, 2019
 */
public class Worker2Verticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        while (true) {
            try {
                Thread.sleep(2000);
                System.out.println("hello hoank " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
