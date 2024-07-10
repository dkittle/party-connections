package ca.kittle.capabilities.party.models

import ca.kittle.capabilities.backstory.models.BackstorySummary
import ca.kittle.capabilities.pc.models.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartyMember(
    val name: Name,
    val ancestry: Ancestry,
    val baseClass: BaseClass,
    val role: PartyRole,
    val backstorySummary: BackstorySummary?,
    @SerialName("_id")
    val id: PartyMemberId = PartyMemberId(),
    )

@JvmInline
@Serializable
value class PartyMemberId(val value: String = NewId())
