package io.hoank.services;

import io.hoank.services.impl.KafkaServiceImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.serviceproxy.ServiceProxyBuilder;

import java.util.Map;

/**
 * Created by hoank92 on May, 2019
 */

@ProxyGen // Generate the proxy and handler
public interface KafkaService {

    @Fluent
    KafkaService broadcast(JsonObject message);

    static KafkaService create(Map<String, String> config, KafkaProducer producer, Handler<AsyncResult<KafkaService>> readyHandler) {
        return new KafkaServiceImpl(config, producer, readyHandler);
    }

    static KafkaService createProxy(Vertx vertx, String address) {
        return new ServiceProxyBuilder(vertx).setAddress(address).build(KafkaService.class);
    }
}
