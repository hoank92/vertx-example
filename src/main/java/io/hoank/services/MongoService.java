package io.hoank.services;

import io.hoank.services.impl.MongoServiceImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyClose;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.serviceproxy.ServiceProxyBuilder;

import java.util.List;

/**
 * Created by hoank92 on May, 2019
 */

@ProxyGen
public interface MongoService {
    public static final String DEFAULT_ADDRESS = MongoService.class.getName();
    @GenIgnore
    static MongoService createProxy(final Vertx vertx, final String address) {
        return new ServiceProxyBuilder(vertx).setAddress(address).build(MongoService.class);
    }

    @GenIgnore
    static MongoService create(final MongoClient mongoClient, final Handler<AsyncResult<MongoService>> readyHandler) {
        return new MongoServiceImpl(mongoClient, readyHandler);
    }

    @Fluent
    public MongoService findOne(final String collection, final JsonObject query, final JsonObject fields,
                                final Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    public MongoService find(final String collection, final JsonObject query, final FindOptions options, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    @Fluent
    public MongoService insertOne(final String collection, final JsonObject document, final Handler<AsyncResult<String>> resultHandler);

    @ProxyClose
    public void close();

}
