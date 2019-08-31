package io.hoank.worker;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.AbstractVerticle;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by hoank92 on May, 2019
 */
public class Worker2Verticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        Config cfg = new Config();
        while (true) {
            try {
                Thread.sleep(2000);
                System.out.println("hello hoank " + Thread.currentThread().getName());
                HazelcastInstance instance = Hazelcast.getHazelcastInstanceByName("hoank");
                Map<Integer, String> sharedData = instance.getMap("shared");

                String theSharedString = sharedData.get(1);
                System.out.println(theSharedString);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
