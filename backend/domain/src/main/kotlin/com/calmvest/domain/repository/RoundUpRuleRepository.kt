package com.calmvest.domain.repository

import com.calmvest.domain.model.RoundUpRule
import com.calmvest.domain.model.RoundUpRuleId
import com.calmvest.domain.model.UserId
import java.util.Optional

interface RoundUpRuleRepository {
    fun save(rule: RoundUpRule): RoundUpRule
    fun findById(id: RoundUpRuleId): Optional<RoundUpRule>
    fun findByUserId(userId: UserId): Optional<RoundUpRule>
}
