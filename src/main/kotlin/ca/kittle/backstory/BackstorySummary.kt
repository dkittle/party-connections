package ca.kittle.backstory

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.util.UUID

data class BackstorySummary(
    val summary: String,
    @Contextual @SerialName("_id")
    val id: String = UUID.randomUUID().toString(),
)
