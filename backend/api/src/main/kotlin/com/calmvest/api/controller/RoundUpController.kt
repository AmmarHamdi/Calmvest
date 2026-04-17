package com.calmvest.api.controller

import com.calmvest.application.dto.ReserveDto
import com.calmvest.application.dto.RoundUpRuleDto
import com.calmvest.application.dto.UpdateRoundUpRuleRequest
import com.calmvest.application.service.RoundUpRuleService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users/{userId}")
class RoundUpController(
    private val roundUpRuleService: RoundUpRuleService
) {

    @GetMapping("/round-up-rule")
    fun getRoundUpRule(@PathVariable userId: UUID): RoundUpRuleDto =
        roundUpRuleService.getRoundUpRule(userId)

    @PutMapping("/round-up-rule")
    fun upsertRoundUpRule(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: UpdateRoundUpRuleRequest
    ): RoundUpRuleDto = roundUpRuleService.upsertRoundUpRule(userId, request)

    @GetMapping("/reserve")
    fun getReserve(@PathVariable userId: UUID): ReserveDto =
        roundUpRuleService.getReserve(userId)
}
