package br.com.knipers.decomposer.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.mongo.MongoHealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoHealth implements HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoHealth.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Health health() {
    return mongoHealthCustom();
    }

    public Health mongoHealthCustom() {
        try {
            return (new MongoHealthIndicator(mongoTemplate).health().getStatus().equals(Status.UP)) ? Health.status("UP").build() : Health.status("DOWN").build();
        } catch (Exception e) {
            LOGGER.error("mongodb down", e);
            return Health.status("DOWN").build();
        }
    }
}
