package com.calmvest.api.controller

import com.calmvest.application.dto.CreateInvestmentOrderRequest
import com.calmvest.application.dto.InvestmentOrderDto
import com.calmvest.application.service.InvestmentOrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users/{userId}/investment-orders")
class InvestmentOrderController(
    private val investmentOrderService: InvestmentOrderService
) {

    @GetMapping
    fun getOrders(@PathVariable userId: UUID): List<InvestmentOrderDto> =
        investmentOrderService.getOrdersForUser(userId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: CreateInvestmentOrderRequest
    ): InvestmentOrderDto = investmentOrderService.createOrder(userId, request)
}
