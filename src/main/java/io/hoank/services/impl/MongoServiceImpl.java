package io.hoank.services.impl;

import io.hoank.services.MongoService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

/**
 * Created by hoank92 on May, 2019
 */
public class MongoServiceImpl implements MongoService {
    private final MongoClient client;

    public MongoServiceImpl(final MongoClient mongoClient, final Handler<AsyncResult<MongoService>> readyHandler) {
        this.client = mongoClient;
        this.client.getCollections(resp -> {
            if (resp.failed()) {
                readyHandler.handle(Future.failedFuture(resp.cause()));
            } else {
                readyHandler.handle(Future.succeededFuture(this));
            }
        });
    }
    @Override
    public MongoService findOne(String collection, JsonObject query, JsonObject fields, Handler<AsyncResult<JsonObject>> resultHandler) {
        try {
            client.findOne(collection, query, fields, resp -> {
                if (resp.failed()) {
                    resultHandler.handle(Future.failedFuture(resp.cause()));
                }
                resultHandler.handle(Future.succeededFuture(resp.result()));
            });
        } catch (Exception ex) {
            resultHandler.handle(Future.failedFuture(ex));
        }
        return this;
    }

    @Override
    public MongoService find(String collection, JsonObject query, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        try {
            client.find(collection, query, resp -> {
                if (resp.failed()) {
                    resultHandler.handle(Future.failedFuture(resp.cause()));
                }
                resultHandler.handle(Future.succeededFuture(resp.result()));
            });
        } catch (Exception ex) {
            resultHandler.handle(Future.failedFuture(ex));
        }
        return this;
    }

    @Override
    public MongoService insertOne(String collection, JsonObject document, Handler<AsyncResult<String>> resultHandler) {
        try {
            client.insert(collection, document, resp -> {
                if (resp.failed()) {
                    resultHandler.handle(Future.failedFuture(resp.cause()));
                }
                resultHandler.handle(Future.succeededFuture(resp.result()));
            });
        } catch (Exception ex) {
            resultHandler.handle(Future.failedFuture(ex));
        }
        return this;
    }

    @Override
    public void close() {
        this.client.close();
    }
}
