package com.calmvest.application.service

import com.calmvest.application.dto.PortfolioDto
import com.calmvest.domain.model.Portfolio
import com.calmvest.domain.model.UserId
import com.calmvest.domain.port.CustodyProvider
import com.calmvest.domain.repository.PortfolioRepository
import com.calmvest.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PortfolioService(
    private val portfolioRepository: PortfolioRepository,
    private val userRepository: UserRepository,
    private val custodyProvider: CustodyProvider
) {

    @Transactional(readOnly = true)
    fun getPortfolioSummary(userId: UUID): List<PortfolioDto> {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        val portfolios = portfolioRepository.findByUserId(uid)
        return portfolios.map { portfolio ->
            val currentValue = try {
                custodyProvider.getPortfolioValue(
                    userReference = userId.toString(),
                    mode = portfolio.mode
                )
            } catch (ex: Exception) {
                portfolio.currentValue
            }
            portfolio.copy(currentValue = currentValue).toDto()
        }
    }

    private fun Portfolio.toDto() = PortfolioDto(
        id = id.value,
        userId = userId.value,
        mode = mode.name,
        totalInvestedEuros = totalInvested.toEuros(),
        currentValueEuros = currentValue.toEuros(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
