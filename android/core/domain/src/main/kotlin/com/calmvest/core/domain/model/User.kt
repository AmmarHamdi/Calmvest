package com.calmvest.core.domain.model

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdAt: String
) {
    val fullName: String get() = "$firstName $lastName"
}
