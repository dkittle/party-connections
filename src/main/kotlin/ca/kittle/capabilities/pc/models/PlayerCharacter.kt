package ca.kittle.capabilities.pc.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class PlayerCharacter private constructor(
    val name: Name,
    val ancestry: String,
    val baseClass: String,
    val role: String,
    val background: String,
    val backstory: String,
    val partyId: String,
    @Contextual @SerialName("_id")
    val id: String = UUID.randomUUID().toString(),
)


@Serializable
@JvmInline
value class Name(val value: String) {
    init {
        require(value.isNotBlank())
    }
}

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
