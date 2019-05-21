package io.hoank.verticles;

import io.hoank.rest.ArticleHandler;
import io.hoank.rest.PingHandler;
import io.hoank.services.KafkaService;
import io.hoank.services.MongoService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by hoank92 on May, 2019
 */

public class HttpVerticle extends AbstractVerticle {
    private static Logger log = LogManager.getLogger(HttpVerticle.class);
    protected MongoService mongoService;

    @Override
    public void start(Future<Void> future) throws Exception {
        var router = Router.router(vertx);
        new PingHandler(vertx).configRoute(router);
        mongoService = MongoService.createProxy(vertx, MongoService.class.getSimpleName());
        final Integer httpPort = config().getInteger("http.port", 8080);
        new ArticleHandler(vertx, mongoService).configRoute(router);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(httpPort, result -> {
                    if (result.succeeded()) {
                        log.info("Server running, listen port: 8080");
                    } else {
                        future.fail(result.cause());
                    }
                });
    }
}
