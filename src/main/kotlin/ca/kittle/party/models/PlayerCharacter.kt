package ca.kittle.party.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlayerCharacter(
    val name: String,
    val gender: String,
    val ancestry: String,
    val baseClass: String,
    val role: String,
    val backstory: String,
    val partyId: String,
    @Contextual @SerialName("_id")
    val id: String = UUID.randomUUID().toString(),
)
