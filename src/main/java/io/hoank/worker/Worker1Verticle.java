package io.hoank.worker;

import io.vertx.core.AbstractVerticle;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by hoank92 on May, 2019
 */
public class Worker1Verticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.<Void>executeBlocking(f -> {
            Integer couter = 0;
            while (true) {
                // blocking...
                try {
                    //Thread.sleep(500);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                vertx.eventBus().send("sample.data", couter.toString(), resp -> {
                    System.out.println("[Main] Receiving reply ' " + resp.result().body()
                            + "' in " + Thread.currentThread().getName());
                });
                couter += 1;
            }
        }, voidAsyncResult -> System.out.println("done"));
    }
}
