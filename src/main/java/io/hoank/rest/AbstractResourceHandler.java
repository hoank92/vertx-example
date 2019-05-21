package io.hoank.rest;

import io.hoank.services.MongoService;
import io.vertx.core.Vertx;
import lombok.Data;

/**
 * Created by hoank92 on May, 2019
 */

@Data
public abstract class AbstractResourceHandler extends AbstractHttpHandler {
    protected MongoService mongoService;
    public AbstractResourceHandler(Vertx vertx, MongoService mongoService) {
        super(vertx);
        this.mongoService = mongoService;
    }


}
