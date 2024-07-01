package ca.kittle.capabilities.party.models

import ca.kittle.capabilities.pc.models.Name
import ca.kittle.capabilities.pc.models.PlayerCharacter
import ca.kittle.util.randomUUIDv7
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Party(
    val name: String,
    val playerCharacters: List<PlayerCharacter> = listOf(),
    @SerialName("_id")
    val id: PartyId = PartyId(),
)

object NewId {
    operator fun invoke() = randomUUIDv7().toString()
}


@JvmInline
@Serializable
value class PartyId(val value: String = NewId())