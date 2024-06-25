package ca.kittle.db.models

import ca.kittle.capabilities.backstory.models.BackstorySummary
import ca.kittle.util.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document

fun BackstorySummary.toDocument(): Document = Document.parse(Json.encodeToString(this))

object BackstorySummaryEntity {
    const val COLLECTION_NAME = "backstory-summaries"

    fun fromDocument(document: Document): BackstorySummary = json.decodeFromString(document.toJson())
}
