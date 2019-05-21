package io.hoank.rest;

import io.hoank.services.KafkaService;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * Created by hoank92 on May, 2019
 */
public class PingHandler extends AbstractKafkaHandler {
    public PingHandler(Vertx vertx, KafkaService kafkaService) {
        super(vertx, kafkaService);
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
            kafkaService.broadcast(about);
            routingContext.response().end(about.encode());
        });
    }
}
