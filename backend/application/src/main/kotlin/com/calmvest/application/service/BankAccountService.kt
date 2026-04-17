package com.calmvest.application.service

import com.calmvest.application.dto.BankAccountDto
import com.calmvest.application.dto.CreateBankAccountRequest
import com.calmvest.domain.exception.ConsentInvalidException
import com.calmvest.domain.model.BankAccount
import com.calmvest.domain.model.BankAccountId
import com.calmvest.domain.model.UserId
import com.calmvest.domain.port.OpenBankingProvider
import com.calmvest.domain.repository.BankAccountRepository
import com.calmvest.domain.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val userRepository: UserRepository,
    private val openBankingProvider: OpenBankingProvider
) {

    private val log = LoggerFactory.getLogger(BankAccountService::class.java)

    @Transactional
    fun addBankAccount(userId: UUID, request: CreateBankAccountRequest): BankAccountDto {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        if (!openBankingProvider.isConsentActive(request.consentId)) {
            throw ConsentInvalidException(request.consentId)
        }

        log.info("Linking bank account for user {} with provider {}", userId, request.provider)

        val account = BankAccount(
            id = BankAccountId.generate(),
            userId = uid,
            iban = request.iban.trim(),
            provider = request.provider.trim(),
            consentId = request.consentId.trim(),
            isActive = true,
            createdAt = Instant.now()
        )
        return bankAccountRepository.save(account).toDto()
    }

    @Transactional(readOnly = true)
    fun getBankAccountsForUser(userId: UUID): List<BankAccountDto> {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }
        return bankAccountRepository.findByUserId(uid).map { it.toDto() }
    }

    private fun BankAccount.toDto() = BankAccountDto(
        id = id.value,
        userId = userId.value,
        iban = iban,
        provider = provider,
        consentId = consentId,
        isActive = isActive,
        createdAt = createdAt
    )
}
