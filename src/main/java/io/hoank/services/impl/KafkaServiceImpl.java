package io.hoank.services.impl;

import io.hoank.services.KafkaService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import java.util.Map;

/**
 * Created by hoank92 on May, 2019
 */
public class KafkaServiceImpl implements KafkaService {

    private final KafkaProducer producer;
    private final Map<String, String> config;

    public KafkaServiceImpl(Map<String, String> config, KafkaProducer producer, Handler<AsyncResult<KafkaService>> readyHandler) {
        this.config = config;
        this.producer = producer;
        readyHandler.handle(Future.succeededFuture(this));
    }

    @Override
    public KafkaService broadcast(JsonObject message) {
        KafkaProducerRecord<String, JsonObject> record = KafkaProducerRecord.create(config.get("topic"),
                config.get("group.id"), message);
        producer.write(record);
        return this;
    }
}
