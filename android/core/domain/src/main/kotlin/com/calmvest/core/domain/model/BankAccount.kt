package com.calmvest.core.domain.model

data class BankAccount(
    val id: String,
    val userId: String,
    val bankName: String,
    val accountNumberMasked: String,
    val iban: String,
    val isActive: Boolean,
    val linkedAt: String
)
