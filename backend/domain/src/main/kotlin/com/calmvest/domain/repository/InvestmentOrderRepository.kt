package com.calmvest.domain.repository

import com.calmvest.domain.model.InvestmentOrder
import com.calmvest.domain.model.InvestmentOrderId
import com.calmvest.domain.model.UserId
import java.util.Optional

interface InvestmentOrderRepository {
    fun save(order: InvestmentOrder): InvestmentOrder
    fun findById(id: InvestmentOrderId): Optional<InvestmentOrder>
    fun findByUserId(userId: UserId): List<InvestmentOrder>
    fun findByIdempotencyKey(idempotencyKey: String): Optional<InvestmentOrder>
    fun existsByIdempotencyKey(idempotencyKey: String): Boolean
}
