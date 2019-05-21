package io.hoank.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by hoank92 on May, 2019
 */

public class HelloVerticle extends AbstractVerticle {

    private static Logger log = LogManager.getLogger(HelloVerticle.class);


    @Override
    public void start(Future<Void> future) {
        log.info("Welcome to Vertx");
    }

    @Override
    public void stop() {
        log.info("Shutting down application");
    }
}
