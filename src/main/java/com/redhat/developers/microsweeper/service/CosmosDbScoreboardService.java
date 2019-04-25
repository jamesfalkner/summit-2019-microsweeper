package com.redhat.developers.microsweeper.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.redhat.developers.microsweeper.model.Score;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentracing.Tracer;
import io.opentracing.contrib.mongo.TracingMongoClient;
import io.opentracing.contrib.mongo.common.TracingCommandListener;

public class CosmosDbScoreboardService implements ScoreboardService {

    MongoClient mongoClient;

    final Logger logger = LoggerFactory.getLogger(getClass());

    String uri;

    @Inject Tracer tracer;
    
    public CosmosDbScoreboardService(String uri) {
        this.uri = uri;
    }

    private MongoClient getMongoClient() {
        if (mongoClient == null) {
            TracingCommandListener listener =
              new TracingCommandListener.Builder(tracer).build();

            // Instantiate Synchronous Tracing MongoClient
            mongoClient = new TracingMongoClient(listener, new MongoClientURI(uri));
        }
        return mongoClient;
    }

    @Override
    public List<Score> getScoreboard() {
        List<Score> scores = new ArrayList<>();

        for (Document document : getScoresCollection().find()) {
            scores.add(fromDocument(document));
        }
        logger.info("Fetched scores from AzureDB: " + scores);
        return scores;
    }

    @Override
    public void addScore(Score score) {
        createScoreItem(score);
        logger.info("Stored score in AzureDB: " + score);
    }

    @Override
    public void clearScores() {
        getScoresCollection().drop();
        collectionCache = null;
        logger.info("Cleared scores in AzureDB");
    }

    // Cache for the database object, so we don't have to query for it to
    // retrieve self links.
    private static MongoDatabase databaseCache;

    // Cache for the collection object, so we don't have to query for it to
    // retrieve self links.
    private static MongoCollection<Document> collectionCache;

    // The name of our database.
    private static final String DATABASE_ID = "ScoresDB";

    // The name of our collection.
    private static final String COLLECTION_ID = "ScoresCollection";

    private void createScoreItem(Score score) {
        Document scoreItemDocument = new Document(toMap(score));
        // Persist the document using the DocumentClient.
        getScoresCollection().insertOne(scoreItemDocument);
    }

    private MongoDatabase getScoreDatabase() {
        if (databaseCache != null) {
            return databaseCache;
        } else {
            databaseCache = getMongoClient().getDatabase(DATABASE_ID);
            return databaseCache;
        }
    }

    private MongoCollection<Document> getScoresCollection() {
        if (collectionCache != null) {
            return collectionCache;
        } else {
            collectionCache = getScoreDatabase().getCollection(COLLECTION_ID);
            return collectionCache;
        }
    }

    private static Map<String, Object> toMap(Score score) {
        Map<String, Object> obj = new HashMap<>();
        obj.put("name", score.name);
        obj.put("level", score.level);
        obj.put("time", score.time);
        obj.put("success", score.success);
        return obj;
    }

    private static Score fromDocument(Document d) {
        Score score = new Score();
        score.name = d.getString("name");
        score.level = d.getString("level");
        score.time = d.getInteger("time");
        score.success = d.getBoolean("success");
        return score;
    }

}