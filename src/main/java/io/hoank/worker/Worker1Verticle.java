package io.hoank.worker;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.AbstractVerticle;

import java.sql.Time;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hoank92 on May, 2019
 */
public class Worker1Verticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.<Void>executeBlocking(f -> {
            Integer couter = 0;
            // Code in process 1
            Config cfg = new Config();
            cfg.setInstanceName("hoank");
            HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
            Map<Integer, String> sharedData = instance.getMap("shared");
            sharedData.put(1, "This is shared data");
            System.out.println(sharedData.get(1));
            while (true) {
                // blocking...
                try {
                    //Thread.sleep(500);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                vertx.eventBus().send("sample.data", couter.toString(), resp -> {
                    System.out.println("[Main] Receiving reply ' " + resp.result().body()
                            + "' in " + Thread.currentThread().getName());
                });
                couter += 1;
            }
        }, voidAsyncResult -> System.out.println("done"));
    }
}
