package io.hoank.services.impl;

import io.hoank.services.MongoService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.reactivex.ext.mongo.MongoClient;

import java.util.List;

/**
 * Created by hoank92 on May, 2019
 */
public class MongoServiceImpl implements MongoService {
    private MongoClient client;

    public MongoServiceImpl(final MongoClient mongoClient, final Handler<AsyncResult<MongoService>> readyHandler) {
        this.client = mongoClient;
        this.client.rxGetCollections().subscribe(resp -> {
            readyHandler.handle(Future.succeededFuture(this));
        }, cause -> {
            readyHandler.handle(Future.failedFuture(cause));
        });
    }
    @Override
    public MongoService findOne(String collection, JsonObject query, JsonObject fields, Handler<AsyncResult<JsonObject>> resultHandler) {
        try {
            client.rxFindOne(collection, query, fields).subscribe(resp -> {
                resultHandler.handle(Future.succeededFuture(resp));
            }, cause -> {
                resultHandler.handle(Future.failedFuture(cause));
            });
        } catch (Exception ex) {
            resultHandler.handle(Future.failedFuture(ex));
        }
        return this;
    }

    @Override
    public MongoService find(String collection, JsonObject query, FindOptions options, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        try {
            client.rxFindWithOptions(collection, query, options).subscribe(resp -> {
                resultHandler.handle(Future.succeededFuture(resp));
            }, cause -> {
                resultHandler.handle(Future.failedFuture(cause));
            });
        } catch (Exception ex) {
            resultHandler.handle(Future.failedFuture(ex));
        }
        return this;
    }

    @Override
    public MongoService insertOne(String collection, JsonObject document, Handler<AsyncResult<String>> resultHandler) {
        try {
            client.rxInsert(collection, document).subscribe(resp -> {
                resultHandler.handle(Future.succeededFuture(resp));
            }, cause -> {
                resultHandler.handle(Future.failedFuture(cause));
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
