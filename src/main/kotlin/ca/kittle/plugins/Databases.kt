package ca.kittle.plugins

import ca.kittle.db.models.PartyEntity
import ca.kittle.db.models.PlayerCharacterEntity
import com.mongodb.MongoCommandException
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.config.*

fun Application.configureDatabases() {
    val mongoDatabase = connectToMongoDB()
    try {
        mongoDatabase.createCollection(PartyEntity.COLLECTION_NAME)
    } catch (e: MongoCommandException) {
        // do nothing as the collection is already created
    }
    try {
        mongoDatabase.createCollection(PlayerCharacterEntity.COLLECTION_NAME)
    } catch (e: MongoCommandException) {
        // do nothing as the collection is already created
    }
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
    val user = environment.config.tryGetString("db.mongo.user") ?: "mongoadmin"
    val password = environment.config.tryGetString("db.mongo.password") ?: System.getenv("MONGO_PASSWORD")
    val host = environment.config.tryGetString("db.mongo.host") ?: "127.0.0.1"
    val port = environment.config.tryGetString("db.mongo.port") ?: "27017"
    val maxPoolSize = environment.config.tryGetString("db.mongo.maxPoolSize")?.toInt() ?: 20
    val databaseName = environment.config.tryGetString("db.mongo.database.name") ?: "PartyConnections"

    val credentials = user.let { userVal -> password?.let { passwordVal -> "$userVal:$passwordVal@" } }.orEmpty()
    val uri = "mongodb://$credentials$host:$port/?maxPoolSize=$maxPoolSize&w=majority"

    val mongoClient = MongoClients.create(uri)
    val database = mongoClient.getDatabase(databaseName)

    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    dbConnection = database
    return database
}

lateinit var dbConnection: MongoDatabase
