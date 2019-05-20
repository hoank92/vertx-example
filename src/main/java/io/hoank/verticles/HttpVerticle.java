package io.hoank.verticles;

import io.hoank.rest.ArticleHandler;
import io.hoank.rest.PingHandler;
import io.hoank.services.MongoService;
import io.hoank.services.impl.MongoServiceImpl;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.Router;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on May, 2019
 */

@Log4j2
public class HttpVerticle extends AbstractVerticle {

    private MongoService mongoService;

    @Override
    public void start(Future<Void> future) throws Exception {
        JsonObject config = new JsonObject()
                .put("connection_string", "mongodb://10.20.43.48:27017")
                .put("db_name", "tiki");

        final MongoClient mongoClient = MongoClient.createShared(vertx, config);

        mongoService = MongoService.create(mongoClient, ready -> {
            if (ready.succeeded()) {
                var router = Router.router(vertx);
                new PingHandler(vertx).configRoute(router);
                new ArticleHandler(vertx, mongoService).configRoute(router);
                vertx.createHttpServer()
                        .requestHandler(router)
                        .listen(8080, result -> {
                            if (result.succeeded()) {
                                log.info("Server running, listen port: 8080");
                            } else {
                                future.fail(result.cause());
                            }
                        });
            } else {
                future.fail(ready.cause());
            }
        });
    }
}
