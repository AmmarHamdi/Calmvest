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
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(InvestmentOrderService::class.java)

    @Transactional
    fun createOrder(userId: UUID, request: CreateInvestmentOrderRequest): InvestmentOrderDto {
        // Idempotency: return existing order if already created
        val existing = investmentOrderRepository.findByIdempotencyKey(request.idempotencyKey)
        if (existing.isPresent) {
            log.debug("Investment order already exists for idempotency key {}", request.idempotencyKey)
            return existing.get().toDto()
        }

        val uid = UserId(userId)
        userRepository.findById(uid)
            .orElseThrow { NoSuchElementException("User not found: $userId") }

        val goalId = GoalId(request.goalId)
        val goal = goalRepository.findById(goalId)
            .orElseThrow { NoSuchElementException("Goal not found: ${request.goalId}") }
        require(goal.userId == uid) { "Goal does not belong to user $userId" }

        val mode = try {
            InvestmentMode.valueOf(request.mode.uppercase())
        } catch (ex: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid investment mode: ${request.mode}. Must be one of: ${InvestmentMode.entries.joinToString()}")
        }

        val amount = Money.ofEuros(request.amountEuros)
        val now = Instant.now()

        log.info("Creating investment order for user {} amount {} mode {}", userId, amount, mode)

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

        val targetGoal = activeGoals.firstOrNull() ?: run {
            log.debug("No active goals found for user {}, skipping auto-investment", userId)
            return
        }

        val idempotencyKey = "auto:${userId.value}:${UUID.randomUUID()}"
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
            log.error("Failed to submit investment order {} to custody provider", order.id, ex)
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
