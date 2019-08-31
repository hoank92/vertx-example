package io.hoank.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * Created by hoank92 on May, 2019
 */
public class MainVerticle {

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {

        DeploymentOptions deploymentOptions = new DeploymentOptions().setWorker(true).setInstances(1);
        Vertx vertx = Vertx.vertx();
        //vertx.deployVerticle("io.hoank.Worker1Verticle.KafkaVerticle", deploymentOptions);
        vertx.deployVerticle("io.hoank.worker.Worker1Verticle",
                deploymentOptions);

        vertx.deployVerticle("io.hoank.worker.Worker2Verticle",
                deploymentOptions);

        System.out.println(deploymentOptions.getInstances());
        System.out.println(deploymentOptions.getWorkerPoolSize());
    }
}
