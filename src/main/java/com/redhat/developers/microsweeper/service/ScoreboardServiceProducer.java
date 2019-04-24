package com.redhat.developers.microsweeper.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentracing.Tracer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Optional;

public class ScoreboardServiceProducer {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    @ConfigProperty(name = "cosmosdb.uri")
    Optional<String> cosmosdbUri;

    // @Inject
    // Tracer tracer;
    
    @ApplicationScoped
    @Produces
    public ScoreboardService scoreboardService() {
        String environment = System.getenv().get("ENVIRONMENT");
        logger.info("Cosmos DB URI: " + cosmosdbUri.orElse("!!UNSET!!"));
        if ("PRODUCTION".equals(environment)) {
            logger.info("Instantiating a COSMOSDB Scoreboard Service");
            return new CosmosDbScoreboardService(cosmosdbUri.orElseThrow(() -> new IllegalStateException("COSMOSDB_URI property is required")));
        } else {
            logger.info("Instantiating a Quarkus Hibernate Panache Scoreboard Service");
            return new QuarkusPanacheScoreboardService();
        }
    }

}
