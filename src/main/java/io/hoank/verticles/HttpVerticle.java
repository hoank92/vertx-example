package io.hoank.verticles;

import io.hoank.rest.ArticleHandler;
import io.hoank.rest.PingHandler;
import io.hoank.services.MongoService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.log4j.Log4j2;

/**
 * Created by hoank92 on May, 2019
 */

@Log4j2
public class HttpVerticle extends AbstractVerticle {

    private MongoService mongoService;

    @Override
    public void start(Future<Void> future) throws Exception {

        //mongoService = MongoService.createProxy(vertx, MongoService.class.getSimpleName());
        final Integer httpPort = config().getInteger("http.port", 8080);
        var router = Router.router(vertx);
        new PingHandler(vertx).configRoute(router);
        // new ArticleHandler(vertx, mongoService).configRoute(router);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        log.info("Server running, listen port: 8080");
                    } else {
                        future.fail(result.cause());
                    }
                });
    }
}
