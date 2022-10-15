package com.example.myapplication.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.myapplication.Person
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object PersonSerializer : Serializer<Person> {
    override val defaultValue: Person
        get() = Person.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Person {
        try {
            return Person.parseFrom(input)
        }
        catch (e: InvalidProtocolBufferException){
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: Person, output: OutputStream) {
        t.writeTo(output)
    }
}