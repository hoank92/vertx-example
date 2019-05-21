package io.hoank;

import io.hoank.verticles.DatabaseVerticle;
import io.hoank.verticles.HelloVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Created by hoank92 on May, 2019
 */


public class Application{
    public static void main(String[] args) {
        System.out.println("test");
        Vertx vertx = Vertx.vertx();
        Future<String> future = Future.future();
        vertx.deployVerticle("io.hoank.verticles.DatabaseVerticle", future.completer());
        vertx.deployVerticle("io.hoank.verticles.HelloVerticle", future.completer());
        vertx.deployVerticle("io.hoank.verticles.HttpVerticle", future.completer());

    }
}
