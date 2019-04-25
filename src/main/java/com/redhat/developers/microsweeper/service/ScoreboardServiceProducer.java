package com.redhat.developers.microsweeper.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Optional;

public class ScoreboardServiceProducer {

    final Logger logger = LoggerFactory.getLogger(getClass());

    Optional<String> cosmosdbUri = Optional.of("mongodb://3454353434-2323-24be-2342-222c5ebadb61:NpxCEjVIe82NW3RnHxoVeD2iBUpfFOHdVRXnDpRo3oxMm3sdg==@asdf8c-81d0");

    // @Inject
    // @ConfigProperty(name = "cosmosdb.uri")
    // Optional<String> cosmosdbUri;
    
    @Produces
    @ApplicationScoped
    public ScoreboardService scoreboardService() {

        if (cosmosdbUri.isPresent() && cosmosdbUri.get().startsWith("mongodb://")) {
            logger.info("Instantiating a COSMOSDB Scoreboard Service");
            return new CosmosDbScoreboardService(cosmosdbUri.orElseThrow(() -> new IllegalStateException("COSMOSDB_URI property is required")));
        } else {
            logger.info("Instantiating a Quarkus Hibernate Panache Scoreboard Service");
            return new QuarkusPanacheScoreboardService();
        }
    }

}
