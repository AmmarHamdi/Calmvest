package com.calmvest.core.data.remote.api

import com.calmvest.core.data.remote.dto.*
import retrofit2.http.*

interface CalmvestApi {

    @GET("api/v1/users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserDto

    @PUT("api/v1/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body body: Map<String, String>
    ): UserDto

    @GET("api/v1/users/{userId}/bank-accounts")
    suspend fun getBankAccounts(@Path("userId") userId: String): List<BankAccountDto>

    @GET("api/v1/users/{userId}/transactions")
    suspend fun getTransactions(@Path("userId") userId: String): List<TransactionDto>

    @GET("api/v1/users/{userId}/bank-accounts/{bankAccountId}/transactions")
    suspend fun getTransactionsByBankAccount(
        @Path("userId") userId: String,
        @Path("bankAccountId") bankAccountId: String
    ): List<TransactionDto>

    @GET("api/v1/users/{userId}/goals")
    suspend fun getGoals(@Path("userId") userId: String): List<GoalDto>

    @GET("api/v1/users/{userId}/goals/{goalId}")
    suspend fun getGoal(
        @Path("userId") userId: String,
        @Path("goalId") goalId: String
    ): GoalDto

    @POST("api/v1/users/{userId}/goals")
    suspend fun createGoal(
        @Path("userId") userId: String,
        @Body body: CreateGoalRequest
    ): GoalDto

    @PATCH("api/v1/users/{userId}/goals/{goalId}")
    suspend fun updateGoal(
        @Path("userId") userId: String,
        @Path("goalId") goalId: String,
        @Body body: Map<String, Boolean>
    ): GoalDto

    @DELETE("api/v1/users/{userId}/goals/{goalId}")
    suspend fun deleteGoal(
        @Path("userId") userId: String,
        @Path("goalId") goalId: String
    )

    @GET("api/v1/users/{userId}/round-up-rule")
    suspend fun getRoundUpRule(@Path("userId") userId: String): RoundUpRuleDto

    @PUT("api/v1/users/{userId}/round-up-rule")
    suspend fun updateRoundUpRule(
        @Path("userId") userId: String,
        @Body body: UpdateRoundUpRuleRequest
    ): RoundUpRuleDto

    @GET("api/v1/users/{userId}/reserve")
    suspend fun getReserveSummary(@Path("userId") userId: String): ReserveSummaryDto

    @GET("api/v1/users/{userId}/portfolio")
    suspend fun getPortfolio(@Path("userId") userId: String): PortfolioDto

    @PUT("api/v1/users/{userId}/portfolio/mode")
    suspend fun updateInvestmentMode(
        @Path("userId") userId: String,
        @Body body: UpdateInvestmentModeRequest
    ): PortfolioDto

    @GET("api/v1/users/{userId}/investment-orders")
    suspend fun getInvestmentOrders(@Path("userId") userId: String): List<InvestmentOrderDto>

    @POST("api/v1/users/{userId}/investment-orders/manual")
    suspend fun triggerManualInvestment(@Path("userId") userId: String): InvestmentOrderDto
}
