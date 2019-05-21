package io.hoank;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by hoank92 on May, 2019
 */

public class Application{
    private static Logger log = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Future<String> future = Future.future();
        vertx.deployVerticle("io.hoank.verticles.DatabaseVerticle", Application::handle);
        vertx.deployVerticle("io.hoank.verticles.HelloVerticle", Application::handle);
        vertx.deployVerticle("io.hoank.verticles.HttpVerticle", Application::handle);

    }

    private static void handle(AsyncResult<String> res) {
        if (!res.succeeded()) {
            log.error("FATAL: Deploy Verticle failed!");
        }
    }
}
