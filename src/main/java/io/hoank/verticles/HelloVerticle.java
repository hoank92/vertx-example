package io.hoank.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on May, 2019
 */

@Log4j2
public class HelloVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloVerticle());
    }

    @Override
    public void start(Future<Void> future) {
        log.info("Welcome to Vertx");
    }

    @Override
    public void stop() {
        log.info("Shutting down application");
    }
}
