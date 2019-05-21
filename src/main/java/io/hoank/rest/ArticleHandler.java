package io.hoank.rest;

import io.hoank.services.MongoService;
import io.hoank.services.impl.MongoServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Created by hoank92 on May, 2019
 */

@Log4j2
public class ArticleHandler extends AbstractResourceHandler {
    public ArticleHandler(Vertx vertx, MongoService mongoService) {
        super(vertx, mongoService);
    }

    @Override
    public void configRoute(Router router) {
        router.get("/v1/articles")
                .handler(this::getArticles);

        router.post("/v1/article")
                .handler(this::addArticles);

    }

    private void getArticles(RoutingContext routingContext) {

        this.getMongoService().find("articles", new JsonObject(), res -> {
            routingContext.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(res.result()));
        });

    }

    private void addArticles(RoutingContext context) {
        this.getMongoService().insertOne("articles", new JsonObject().put("autho", "hoank").put("content", "kakak"), res -> {
            context.response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(context.getBodyAsJson()));
        });
    }
}
