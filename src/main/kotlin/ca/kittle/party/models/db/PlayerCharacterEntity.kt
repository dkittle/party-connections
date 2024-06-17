package ca.kittle.party.models.db

import ca.kittle.party.models.PlayerCharacter
import ca.kittle.util.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document

fun PlayerCharacter.toDocument(): Document = Document.parse(Json.encodeToString(this))

object PlayerCharacterEntity {
    const val COLLECTION_NAME = "characters"

    fun fromDocument(document: Document): PlayerCharacter = json.decodeFromString(document.toJson())
}
