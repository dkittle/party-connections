package ca.kittle.capabilities.pc.models

import ca.kittle.capabilities.party.models.NewId
import ca.kittle.capabilities.party.models.PartyId
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerCharacter(
    val name: Name,
    val ancestry: Ancestry,
    val baseClass: BaseClass,
    val role: PartyRole,
    val background: Background,
    val backstory: Backstory,
    val partyId: PartyId,
    @Contextual @SerialName("_id")
    val id: PlayerCharacterId = PlayerCharacterId()
)


@JvmInline
@Serializable
value class Name(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
@Serializable
value class Ancestry(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
@Serializable
value class BaseClass(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
@Serializable
value class PartyRole(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
@Serializable
value class Background(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

@JvmInline
@Serializable
value class Backstory(val value: String) {
    init {
        require(value.isNotBlank())
    }
}


@JvmInline
@Serializable
value class PlayerCharacterId(val value: String = NewId())

//@Serializable
//@JvmInline
//value class NonEmptyString(private val string: String) {
//    init {
//        require(string.isNotEmpty()) {
//            "Full name cannot be empty"
//        }
//    }
//    companion object {
//        operator fun invoke(string: String, fieldName: String)
//    }
//}
