package ca.kittle.capabilities.party.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Party(
    val name: String,
    @Contextual @SerialName("_id")
    val id: String = UUID.randomUUID().toString(),
)
