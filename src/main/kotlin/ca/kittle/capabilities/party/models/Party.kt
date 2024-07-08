package ca.kittle.capabilities.party.models

import ca.kittle.capabilities.pc.models.Name
import ca.kittle.util.randomUUIDv7
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Party(
    val name: Name,
    val partyMembers: List<PartyMember> = listOf(),
    @SerialName("_id")
    val id: PartyId = PartyId(),
)

object NewId {
    operator fun invoke() = randomUUIDv7().toString()
}

@JvmInline
@Serializable
value class PartyId(val value: String = NewId())
