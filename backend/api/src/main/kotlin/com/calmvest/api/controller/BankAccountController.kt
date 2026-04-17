package com.calmvest.api.controller

import com.calmvest.application.dto.BankAccountDto
import com.calmvest.application.dto.CreateBankAccountRequest
import com.calmvest.application.service.BankAccountService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users/{userId}/bank-accounts")
class BankAccountController(
    private val bankAccountService: BankAccountService
) {

    @GetMapping
    fun getBankAccounts(@PathVariable userId: UUID): List<BankAccountDto> =
        bankAccountService.getBankAccountsForUser(userId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBankAccount(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: CreateBankAccountRequest
    ): BankAccountDto = bankAccountService.addBankAccount(userId, request)
}
