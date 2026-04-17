package com.calmvest.api.controller

import com.calmvest.application.dto.ImportTransactionRequest
import com.calmvest.application.dto.TransactionDto
import com.calmvest.application.service.RoundUpService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users/{userId}/transactions")
class TransactionController(
    private val roundUpService: RoundUpService
) {

    @GetMapping
    fun getTransactions(@PathVariable userId: UUID): List<TransactionDto> =
        roundUpService.getTransactionsForUser(userId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun importTransaction(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: ImportTransactionRequest
    ): TransactionDto = roundUpService.importTransaction(userId, request)
}
