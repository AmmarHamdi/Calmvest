package com.calmvest.domain.repository

import com.calmvest.domain.model.Money
import com.calmvest.domain.model.ReserveEntry
import com.calmvest.domain.model.ReserveEntryId
import com.calmvest.domain.model.UserId
import java.util.Optional

interface ReserveEntryRepository {
    fun save(entry: ReserveEntry): ReserveEntry
    fun findById(id: ReserveEntryId): Optional<ReserveEntry>
    fun findByUserId(userId: UserId): List<ReserveEntry>
    fun findByIdempotencyKey(idempotencyKey: String): Optional<ReserveEntry>
    fun existsByIdempotencyKey(idempotencyKey: String): Boolean
    fun sumAmountByUserId(userId: UserId): Money
}
