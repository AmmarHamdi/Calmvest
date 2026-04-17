package com.calmvest.application.service

import com.calmvest.application.dto.CreateInvestmentOrderRequest
import com.calmvest.application.dto.InvestmentOrderDto
import com.calmvest.domain.model.GoalId
import com.calmvest.domain.model.InvestmentMode
import com.calmvest.domain.model.InvestmentOrder
import com.calmvest.domain.model.InvestmentOrderId
import com.calmvest.domain.model.Money
import com.calmvest.domain.model.OrderStatus
import com.calmvest.domain.model.RoundUpRule
import com.calmvest.domain.model.UserId
import com.calmvest.domain.port.CustodyProvider
import com.calmvest.domain.repository.GoalRepository
import com.calmvest.domain.repository.InvestmentOrderRepository
import com.calmvest.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class InvestmentOrderService(
    private val investmentOrderRepository: InvestmentOrderRepository,
    private val goalRepository: GoalRepository,
    private val userRepository: UserRepository,
    private val custodyProvider: CustodyProvider
) {

    @Transactional
    fun createOrder(userId: UUID, request: CreateInvestmentOrderRequest): InvestmentOrderDto {
        require(request.idempotencyKey.isNotBlank()) { "Idempotency key must not be blank" }
        require(request.amountEuros > java.math.BigDecimal.ZERO) { "Amount must be positive" }

        // Idempotency: return existing order if already created
        val existing = investmentOrderRepository.findByIdempotencyKey(request.idempotencyKey)
        if (existing.isPresent) return existing.get().toDto()

        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        val goalId = GoalId(request.goalId)
        goalRepository.findById(goalId)
            .orElseThrow { NoSuchElementException("Goal not found: ${request.goalId}") }

        val mode = InvestmentMode.valueOf(request.mode.uppercase())
        val amount = Money.ofEuros(request.amountEuros)
        val now = Instant.now()

        val order = InvestmentOrder(
            id = InvestmentOrderId.generate(),
            userId = uid,
            goalId = goalId,
            amount = amount,
            mode = mode,
            status = OrderStatus.PENDING,
            idempotencyKey = request.idempotencyKey,
            providerOrderId = null,
            createdAt = now,
            executedAt = null,
            updatedAt = now
        )
        val saved = investmentOrderRepository.save(order)

        return submitToProvider(saved)
    }

    @Transactional
    fun triggerAutomaticInvestment(userId: UserId, amount: Money, rule: RoundUpRule) {
        val activeGoals = goalRepository.findByUserId(userId)
            .filter { it.status == com.calmvest.domain.model.GoalStatus.ACTIVE }

        val targetGoal = activeGoals.firstOrNull() ?: return

        val idempotencyKey = "auto:${userId.value}:${Instant.now().toEpochMilli()}"
        if (investmentOrderRepository.existsByIdempotencyKey(idempotencyKey)) return

        val now = Instant.now()
        val order = InvestmentOrder(
            id = InvestmentOrderId.generate(),
            userId = userId,
            goalId = targetGoal.id,
            amount = amount,
            mode = InvestmentMode.SAFE,
            status = OrderStatus.PENDING,
            idempotencyKey = idempotencyKey,
            providerOrderId = null,
            createdAt = now,
            executedAt = null,
            updatedAt = now
        )
        val saved = investmentOrderRepository.save(order)
        submitToProvider(saved)
    }

    private fun submitToProvider(order: InvestmentOrder): InvestmentOrderDto {
        return try {
            val providerOrderId = custodyProvider.submitOrder(
                idempotencyKey = order.idempotencyKey,
                amount = order.amount,
                mode = order.mode,
                userReference = order.userId.value.toString()
            )
            val now = Instant.now()
            val submitted = order.copy(
                status = OrderStatus.SUBMITTED,
                providerOrderId = providerOrderId,
                executedAt = now,
                updatedAt = now
            )
            investmentOrderRepository.save(submitted).toDto()
        } catch (ex: Exception) {
            val failed = order.copy(
                status = OrderStatus.FAILED,
                updatedAt = Instant.now()
            )
            investmentOrderRepository.save(failed).toDto()
        }
    }

    @Transactional(readOnly = true)
    fun getOrdersForUser(userId: UUID): List<InvestmentOrderDto> {
        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }
        return investmentOrderRepository.findByUserId(uid).map { it.toDto() }
    }

    private fun InvestmentOrder.toDto() = InvestmentOrderDto(
        id = id.value,
        userId = userId.value,
        goalId = goalId.value,
        amountEuros = amount.toEuros(),
        mode = mode.name,
        status = status.name,
        idempotencyKey = idempotencyKey,
        providerOrderId = providerOrderId,
        createdAt = createdAt,
        executedAt = executedAt,
        updatedAt = updatedAt
    )
}
