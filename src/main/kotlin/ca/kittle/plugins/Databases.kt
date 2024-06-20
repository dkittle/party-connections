package ca.kittle.plugins

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.config.tryGetString
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun Application.configureDatabases(): MongoDatabase {
    return connectToMongoDB()
}

/**
 * Establishes connection with a MongoDB database.
 *
 * The following configuration properties (in application.yaml/application.conf) can be specified:
 * * `db.mongo.user` username for your database
 * * `db.mongo.password` password for the user
 * * `db.mongo.host` host that will be used for the database connection
 * * `db.mongo.port` port that will be used for the database connection
 * * `db.mongo.maxPoolSize` maximum number of connections to a MongoDB server
 * * `db.mongo.database.name` name of the database
 *
 * IMPORTANT NOTE: in order to make MongoDB connection working, you have to start a MongoDB server first.
 * See the instructions here: https://www.mongodb.com/docs/manual/administration/install-community/
 * all the paramaters above
 *
 * @returns [MongoDatabase] instance
 * */
fun Application.connectToMongoDB(): MongoDatabase {
    val user = environment.config.tryGetString("db.mongo.user") ?: System.getenv("MONGO_USER")
    val password = environment.config.tryGetString("db.mongo.password") ?: System.getenv("MONGO_PASSWORD")
    val host = environment.config.tryGetString("db.mongo.host") ?: "127.0.0.1"
    val port = environment.config.tryGetString("db.mongo.port") ?: "27017"
    val maxPoolSize = environment.config.tryGetString("db.mongo.maxPoolSize")?.toInt() ?: 20
    val databaseName = environment.config.tryGetString("db.mongo.database.name") ?: "PartyConnections"

    val credentials = user.let { userVal -> password?.let { passwordVal -> "$userVal:$passwordVal@" } }.orEmpty()
    val uri = "mongodb://$credentials$host:$port/?replicaSet=rs0&maxPoolSize=$maxPoolSize&w=majority"

    val mongoClient = MongoClients.create(uri)
    val mongoConnection = mongoClient.getDatabase(databaseName)

    logger.info { "Creating application stopped shutdown hook" }
    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    return mongoConnection
}
