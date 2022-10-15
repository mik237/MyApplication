package com.example.myapplication.models

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val name: String = "",
    val age: Int = 0,
    val address: Address? = null
)

@Serializable
data class Address(
    val flatNumber: Int = 0,
    val street: String = "",
    val city: String = "",
    val country: String = ""
)
