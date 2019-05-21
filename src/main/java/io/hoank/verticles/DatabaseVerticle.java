package io.hoank.verticles;

import io.hoank.services.MongoService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.serviceproxy.ServiceBinder;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on May, 2019
 */
@Log4j2
public class DatabaseVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> future) throws Exception {
        JsonObject config = new JsonObject()
                .put("connection_string", "mongodb://10.20.43.48:27017")
                .put("db_name", "tiki");

        final MongoClient mongoClient = MongoClient.createShared(vertx, config);

        MongoService.create(mongoClient, ready -> {
            if (ready.succeeded()) {
                ServiceBinder binder = new ServiceBinder(vertx);
                binder.setAddress(MongoService.class.getSimpleName())
                        .register(MongoService.class, ready.result());
                log.info("Mongodb hash ben connected");
            } else {
                log.error("Failed to connect mongodb");
                future.fail(ready.cause());
            }
        });
    }
}
