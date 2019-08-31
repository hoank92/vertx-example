package io.hoank.verticles;

import io.hoank.AppConstant;
import io.hoank.config.Configuration;
import io.hoank.services.KafkaService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.serviceproxy.ServiceBinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by hoank92 on May, 2019
 */
public class KafkaMasterVerticle extends AbstractVerticle {

    private static Logger log = LogManager.getLogger(KafkaMasterVerticle.class);
    public static final String DEFAULT_TOPIC = "mkp.test";
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        // Prepare kafka configurations
        Map<String, String> kafkaConfig = Configuration.getInstance().getKafkaConfig();
        // Generate consumer group id
        if (!kafkaConfig.containsKey("group.id")) {
            String groupPrefix = kafkaConfig.getOrDefault("group.id.prefix", "themis.");
            kafkaConfig.put("group.id", groupPrefix);
            //kafkaConfig.put("group.id", groupPrefix + InetAddress.getLocalHost().getHostAddress());
        }

        // Initialize shared producer
        KafkaProducer<String, String> producer = KafkaProducer.createShared(vertx, AppConstant.PRIMARY, kafkaConfig);
        KafkaService.create(kafkaConfig, producer, ready -> {
            if (ready.succeeded()) {
                ServiceBinder binder = new ServiceBinder(vertx);
                binder.setAddress(KafkaService.class.getSimpleName()).register(KafkaService.class, ready.result());
            } else {
                startFuture.fail(ready.cause());
            }
        });

        // Initialize consumer
        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, kafkaConfig);
        consumer.handler(message -> {
            log.info("Processing key={} partition={} ,offset={}, value={}", message.key(), message.partition(), message.offset(), message.value());
            boolean reloaded = false;
            consumer.commit();
        });
        consumer.partitionsAssignedHandler(partitions -> {
            log.info("Partition assigned: {}", partitions);
        });
        consumer.exceptionHandler(h -> {
            log.error(h.getMessage(), h);
        });

        String kafkaTopic = kafkaConfig.getOrDefault("topic", DEFAULT_TOPIC);
        consumer.subscribe(kafkaTopic, subscribe -> {
            if (subscribe.succeeded()) {
                log.info("Subscribed topic {} by group.id {}", kafkaTopic, kafkaConfig.get("group.id"));
//                consumer.seekToEnd(Collections.singleton(topicPartition), done -> {
//                    if (done.succeeded()) {
//                        log.debug("Seek to end {topic={}}", kafkaTopic);
//                    } else {
//                        log.error(done.cause().getMessage(), done.cause());
//                    }
//                });
                startFuture.complete();
            } else {
                log.error("Failed to subscribe topic {}", kafkaTopic);
                startFuture.fail(subscribe.cause());
            }
        });
    }
}
