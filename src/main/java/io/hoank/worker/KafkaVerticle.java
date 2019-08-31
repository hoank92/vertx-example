package io.hoank.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoank92 on May, 2019
 */
public class KafkaVerticle extends AbstractVerticle {

    private static Logger log = LogManager.getLogger(KafkaVerticle.class);
    public static final String DEFAULT_TOPIC = "mkp.test";
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        // Prepare kafka configurations
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "dev-kafka-1.svr.tiki.services:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("group.id", "my_group");
        config.put("acks", "1");

        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, config);
        for (int i = 0; i < 25; i++) {
            String key = String.valueOf(i % 5);
            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> record =
                    KafkaProducerRecord.create(DEFAULT_TOPIC, key,"message_" + i);
            producer.write(record);
        }
    }
}
