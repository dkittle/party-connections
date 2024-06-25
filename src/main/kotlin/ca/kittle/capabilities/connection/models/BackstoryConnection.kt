package ca.kittle.capabilities.connection.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BackstoryConnection(
    val pcId: String,
    val connection: String,
    val otherPcs: List<String>,
    @Contextual @SerialName("_id")
    val id: String = UUID.randomUUID().toString(),
)
