package ca.kittle.util

@JvmInline
value class ValidationError(val message: String)

typealias Errors = List<ValidationError>

sealed class Validated<out Errors, out B> {
    data class Invalid<out Errors, out B>(val value: Errors) : Validated<Errors, B>()
    data class Valid<out Errors, out B>(val value: B) : Validated<Errors, B>()

    companion object {
        fun <Errors, B> invalid(value: Errors): Validated<Errors, B> = Invalid(value)
        fun <Errors, B> valid(value: B): Validated<Errors, B> = Valid(value)
    }
}

typealias Validator<T> = (T) -> Boolean

//val Int.maxLength: Validator<String> get() = { if (it.length <= this@maxLength) Validated.Valid(it) else Invalid(listOf(ValidationError(""))) }
//val Int.minLength: Validator<String> get() = { it.length >= this@minLength }
//val Int.exactLength: Validator<String> get() = { it.length == this@exactLength }
//val IntRange.length: Validator<String> get() = { this@length.contains(it.length) }
//val String.regex: Validator<String> get() = toRegex().let { v -> { v.matches(it) } }
//
//object Validate {
//
//    fun <T> validatedBy(vararg validators: (T) -> Validated<Errors, T>) =
//
//
//        fun <T> invoke(vararg validators: (T) -> Errors): (T) -> Validated<Errors, T> {
//
//        }
//}
//
