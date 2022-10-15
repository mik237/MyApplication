package com.example.myapplication.models


import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object UserSerializer : Serializer<User> {
    override val defaultValue: User
        get() = User()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return Json.decodeFromString(
                deserializer = User.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (se: SerializationException) {
            throw CorruptionException("Unable to read User ", se)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = User.serializer(),
                value = t
            )
                .encodeToByteArray()
        )
    }
}