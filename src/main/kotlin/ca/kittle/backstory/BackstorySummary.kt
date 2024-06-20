package ca.kittle.backstory

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BackstorySummary(
    val summary: String,
    val pcId: String,
    @Contextual @SerialName("_id")
    val id: String = UUID.randomUUID().toString(),
)
