package com.calmvest.api.controller

import com.calmvest.application.dto.PortfolioDto
import com.calmvest.application.service.PortfolioService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users/{userId}/portfolio")
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @GetMapping
    fun getPortfolio(@PathVariable userId: UUID): List<PortfolioDto> =
        portfolioService.getPortfolioSummary(userId)
}
