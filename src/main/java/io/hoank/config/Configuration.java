package io.hoank.config;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by hoank92 on May, 2019
 */

public class Configuration extends JsonObject {

    public static final String VERTICLES_CONFIG_PREFIX = "vertx.verticles.";
    public static final String MONGO_CONFIG_PREFIX = "mongo.";
    public static final String KAFKA_CONFIG_PREFIX = "kafka.";

    private static volatile Configuration instance;
    private static Object mutex = new Object();

    private JsonObject verticleConfig;
    private JsonObject mongoConfig;
    private Map<String, String> kafkaConfig;

    private Configuration() {
        super();
    }

    public static Configuration getInstance() {
        Configuration result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = result = new Configuration();
                }
            }
        }

        return result;
    }

    public static Configuration setup(JsonObject config) {
        // Resolve placeholders in configs
        Map<String, Object> properties = config.getMap();
        // numbers in properties will be cast to Double, we must convert them to Integer for the substitution work correctly
        properties.forEach((k, v) -> {
            if (v instanceof Number) {
                properties.put(k, ((Number) v).intValue());
            }
        });
        StringSubstitutor substitutor = new StringSubstitutor(properties);
        Configuration instance = getInstance();
        properties.forEach((k, v) -> {
            if (v instanceof String && !StringUtils.isEmpty((String) v) && ((String) v).contains("${")) {
                instance.put(k, substitutor.replace(v));
            } else {
                instance.put(k, v);
            }
        });
        return instance;
    }

    public static Configuration setup(Map<String, Object> config) {
        return setup(JsonObject.mapFrom(config));
    }

    /**
     * Get Vert.X deployment options by extract all configs with prefix "vertxOptions"
     * @return
     */
    public JsonObject getVertxOptions() {
        Properties properties = new Properties();
        properties.putAll(getMap());

        Map map = null;
        try {
            map = new JavaPropsMapper().readPropertiesAs(properties, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonObject();
        }

        JsonObject vertxOptions = new JsonObject(map).getJsonObject("vertxOptions");
        if (vertxOptions.isEmpty()) {
            return new JsonObject();
        }

        convertVertxOptions(vertxOptions);

        return vertxOptions;
    }

    private void convertVertxOptions(JsonObject options) {
        options.stream().forEach(entry -> {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) return;

            if (value instanceof JsonObject) {
                convertVertxOptions((JsonObject) entry.getValue());
                return;
            }

            if (value != null && (key.equalsIgnoreCase("enabled") || key.endsWith("Enabled"))) {
                entry.setValue(Boolean.valueOf(value.toString()));
            }
            if (value != null && (key.equalsIgnoreCase("size") || key.endsWith("Size"))) {
                entry.setValue(Integer.valueOf(value.toString()));
            }
        });
    }

    /**
     * Get datasource configurations by extract all configs with prefix "datasource."
     * @return
     */
    public JsonObject getMongoConfig() {
        if (mongoConfig == null) {
            int prefixLength = MONGO_CONFIG_PREFIX.length();
            Map<String, Object> datasource = this.stream().filter(entry -> entry.getKey().startsWith(MONGO_CONFIG_PREFIX))
                    .collect(Collectors.toMap(x -> x.getKey().substring(prefixLength), x -> x.getValue()));
            mongoConfig = new JsonObject(datasource);
        }

        return mongoConfig;
    }

    public Map<String, String> getKafkaConfig() {
        if (kafkaConfig == null) {
            int prefixLength = KAFKA_CONFIG_PREFIX.length();
            kafkaConfig = this.stream().filter(entry -> entry.getKey().startsWith(KAFKA_CONFIG_PREFIX))
                    .collect(Collectors.toMap(x -> x.getKey().substring(prefixLength), x -> String.valueOf(x.getValue())));
        }

        return kafkaConfig;
    }

    public JsonObject getVerticleConfig() {
        if (verticleConfig == null) {
            int prefixLength = VERTICLES_CONFIG_PREFIX.length();
            Map<String, Object> config = this.stream().filter(entry -> entry.getKey().startsWith(VERTICLES_CONFIG_PREFIX))
                    .collect(Collectors.toMap(x -> x.getKey().substring(prefixLength), x -> new JsonObject(String.valueOf(x.getValue()))));
            verticleConfig = new JsonObject(config);
        }

        return verticleConfig;
    }

}
