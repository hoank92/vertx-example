package io.hoank.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by hoank92 on May, 2019
 */
    public class Receiver extends AbstractVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Receiver());
    }


    @Override
    public void start() throws Exception {

        EventBus eb = vertx.eventBus();

        eb.consumer("news-feed", message -> System.out.println("Received news on consumer 1: " + message.body()));

        eb.consumer("news-feed", message -> System.out.println("Received news on consumer 2: " + message.body()));

        eb.consumer("news-feed", message -> System.out.println("Received news on consumer 3: " + message.body()));

        System.out.println("Ready!");
    }
}
