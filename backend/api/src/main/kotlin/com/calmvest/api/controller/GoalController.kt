package com.calmvest.api.controller

import com.calmvest.application.dto.CreateGoalRequest
import com.calmvest.application.dto.GoalDto
import com.calmvest.application.dto.UpdateGoalRequest
import com.calmvest.application.service.GoalService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users/{userId}/goals")
class GoalController(
    private val goalService: GoalService
) {

    @GetMapping
    fun getGoals(@PathVariable userId: UUID): List<GoalDto> =
        goalService.getGoalsForUser(userId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createGoal(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: CreateGoalRequest
    ): GoalDto = goalService.createGoal(userId, request)

    @PutMapping("/{goalId}")
    fun updateGoal(
        @PathVariable userId: UUID,
        @PathVariable goalId: UUID,
        @Valid @RequestBody request: UpdateGoalRequest
    ): GoalDto = goalService.updateGoal(userId, goalId, request)
}
