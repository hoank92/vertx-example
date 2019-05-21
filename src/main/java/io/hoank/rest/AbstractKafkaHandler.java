package io.hoank.rest;

import io.hoank.services.KafkaService;
import io.vertx.core.Vertx;
import lombok.Data;

/**
 * Created by hoank92 on May, 2019
 */

@Data
public abstract class AbstractKafkaHandler extends AbstractHttpHandler {
    protected KafkaService kafkaService;

    public AbstractKafkaHandler(Vertx vertx, KafkaService kafkaService) {
        super(vertx);
        this.kafkaService = kafkaService;
    }
}
