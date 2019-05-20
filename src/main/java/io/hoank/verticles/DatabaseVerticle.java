package io.hoank.verticles;

import io.hoank.rest.ArticleHandler;
import io.hoank.rest.PingHandler;
import io.hoank.services.MongoService;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.serviceproxy.ServiceBinder;

/**
 * Created by hoank92 on May, 2019
 */
public class DatabaseVerticle extends AbstractVerticle {
    public static MongoService mongoService;

    @Override
    public void start(Future<Void> future) throws Exception {
        JsonObject config = new JsonObject()
                .put("connection_string", "mongodb://10.20.43.48:27017")
                .put("db_name", "tiki");

        final MongoClient mongoClient = MongoClient.createShared(vertx, config);

        mongoService = MongoService.create(mongoClient, ready -> {
            if (ready.succeeded()) {
                new ServiceBinder(vertx.getDelegate()).setAddress(MongoService.DEFAULT_ADDRESS)
                        .register(MongoService.class, ready.result()).completionHandler(ar -> {
                            if (ar.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(ar.cause());
                            }
                });
            } else {
                future.fail(ready.cause());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        mongoService.close();
    }
}
