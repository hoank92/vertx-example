package io.hoank;

import io.hoank.verticles.HttpVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by hoank92 on May, 2019
 */


public class Application extends AbstractVerticle{
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpVerticle());
    }
}
