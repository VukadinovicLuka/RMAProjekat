package com.example.prviprojekatrma.auth

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AuthDataSerializer : Serializer<AuthData> {

    override val defaultValue: AuthData = AuthData("", "", "", "")

    override suspend fun readFrom(input: InputStream): AuthData {
        try {
            val json = input.readBytes().decodeToString()
            return Json.decodeFromString(json)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read AuthData", e)
        }
    }

    override suspend fun writeTo(t: AuthData, output: OutputStream) {
        val json = Json.encodeToString(t)
        output.write(json.encodeToByteArray())
    }
}
