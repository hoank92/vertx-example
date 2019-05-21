package io.hoank.rest;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * Created by hoank92 on May, 2019
 */
public class PingHandler extends AbstractHttpHandler {
    public PingHandler(Vertx vertx) {
        super(vertx);
    }

    @Override
    public void configRoute(Router router) {
        router.route("/").handler(routingContext -> {
            HttpServerRequest request = routingContext.request();
            JsonObject about = new JsonObject();
            about.put("message", "welcome to vertx service");
            routingContext.response().end(about.encode());
        });

        router.route("/ping").handler(routingContext -> {
            HttpServerRequest request = routingContext.request();
            JsonObject about = new JsonObject();
            about.put("message", "pong");
            routingContext.response().end(about.encode());
        });
    }
}
