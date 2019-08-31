package io.hoank;

import io.hoank.config.Configuration;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by hoank92 on May, 2019
 */

public class Application{
    private static Logger log = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        Future<String> future = Future.future();
        ConfigStoreOptions file = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject().put("path", "application.properties"));

        String profile = System.getProperty("application.profile", "uat");
        log.warn("Active profile: {}", profile);

        ConfigStoreOptions profileFile = new ConfigStoreOptions()
                .setType("file")
                .setFormat("properties")
                .setConfig(new JsonObject().put("path", String.format("%s.properties", profile)));

        ConfigStoreOptions env = new ConfigStoreOptions().setType("env");
        ConfigStoreOptions sys = new ConfigStoreOptions().setType("sys");

        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(file)
                .addStore(profileFile)
                .addStore(env)
                .addStore(sys);
        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        retriever.getConfig(json -> {
            if (json.failed()) {
                // Failed to retrieve the configuration
                log.error("Failed to retrieve configurations. Cause: " + json.cause().getMessage(), json.cause());
                return;
            }
            log.info("Retrieve configuration success");
            JsonObject config = json.result();
            // Close the Vert.X instance, we don't need it anymore
            vertx.close();

            // Configuration must be setup before other manipulations
            Configuration configuration = Configuration.setup(config);

            // Actual verticle options
            VertxOptions vertxOptions = new VertxOptions(configuration.getVertxOptions());
            Vertx deploymentVertx = Vertx.vertx(vertxOptions);

            // DeploymentOptions deploymentOptions = new DeploymentOptions().setWorker(true).setInstances(3);
            int INSTANCES = Runtime.getRuntime().availableProcessors() * 2;
            DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(INSTANCES);



            //deploymentVertx.deployVerticle("io.hoank.verticles.DatabaseVerticle", Application::handle);
            //deploymentVertx.deployVerticle("io.hoank.verticles.HelloVerticle", Application::handle);
            deploymentVertx.deployVerticle("io.hoank.verticles.HttpVerticle", deploymentOptions, Application::handle);
            //deploymentVertx.deployVerticle("io.hoank.verticles.KafkaVerticle", deploymentOptions, Application::handle);

        });

    }

    private static void handle(AsyncResult<String> res) {
        if (!res.succeeded()) {
            log.error("FATAL: Deploy Verticle failed!");
        }
    }
}
