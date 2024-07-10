package ca.kittle.db.models

import ca.kittle.capabilities.party.models.PartyMember
import ca.kittle.capabilities.pc.models.PlayerCharacter
import ca.kittle.util.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document

fun PartyMember.toDocument(): Document = Document.parse(Json.encodeToString(this))

object PartyMemberEntity {
    fun fromDocument(document: Document): PartyMember = json.decodeFromString(document.toJson())
}
