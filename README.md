# microsweeper-quarkus
Microsweeper Game with Quarkus

By default this application will run an embedded H2 database and use JPA for persistence.

If you want to run it on Cosmos DB, you need to set an environment variables:

- `COSMOSDB_uri=<Your Cosmos DB URI>` (should start with `mongodb://`)
