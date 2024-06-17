package ca.kittle.party.models.db

import ca.kittle.party.models.Party
import ca.kittle.util.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document

fun Party.toDocument(): Document = Document.parse(Json.encodeToString(this))

object PartyEntity {
    const val COLLECTION_NAME = "parties"

    fun fromDocument(document: Document): Party = json.decodeFromString(document.toJson())
}
