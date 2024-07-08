package ca.kittle.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import java.time.Instant
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
val json =
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

private val random = SecureRandom()

fun randomUUIDv7(): UUID {
    val value = ByteArray(16)
    random.nextBytes(value)
    val timestamp = Instant.now().toEpochMilli()

    // timestamp bits
    value[0] = ((timestamp shr 40) and 0xFF).toByte()
    value[1] = ((timestamp shr 32) and 0xFF).toByte()
    value[2] = ((timestamp shr 24) and 0xFF).toByte()
    value[3] = ((timestamp shr 16) and 0xFF).toByte()
    value[4] = ((timestamp shr 8) and 0xFF).toByte()
    value[5] = (timestamp and 0xFF).toByte()

    // version and variant
    value[6] = (value[6].toInt() and 0x0F or 0x70).toByte()
    value[8] = (value[8].toInt() and 0x3F or 0x80).toByte()

    var msb: Long = 0
    var lsb: Long = 0
    for (i in 0..7) msb = (msb shl 8) or (value.get(i).toInt() and 0xff).toLong()
    for (i in 8..15) lsb = (lsb shl 8) or (value.get(i).toInt() and 0xff).toLong()
    return UUID(msb, lsb)
}

