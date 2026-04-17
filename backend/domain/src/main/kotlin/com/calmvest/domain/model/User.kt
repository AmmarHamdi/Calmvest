package com.calmvest.domain.model

import java.time.Instant

data class User(
    val id: UserId,
    val email: String,
    val name: String,
    val kycStatus: KycStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class KycStatus { PENDING, VERIFIED, REJECTED }
