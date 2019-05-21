package io.hoank.worker;

import io.vertx.core.AbstractVerticle;

/**
 * Created by hoank92 on May, 2019
 */
public class WorkerVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("[Worker] Starting in " + Thread.currentThread().getName());
        vertx.eventBus().<String>consumer("sample.data", message -> {
            System.out.println("[Worker] Consuming data in " + Thread.currentThread().getName() + " msg: " + message.body());
            String body = message.body();
            message.reply(body.toUpperCase());
        });
    }
}
