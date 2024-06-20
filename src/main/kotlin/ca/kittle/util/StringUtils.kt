package ca.kittle.util

import kotlinx.serialization.json.Json

val json =
    Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
