package com.calmvest.api.controller

import com.calmvest.application.dto.CreateUserRequest
import com.calmvest.application.dto.UserDto
import com.calmvest.application.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): List<UserDto> = userService.getAllUsers()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody request: CreateUserRequest): UserDto =
        userService.createUser(request)

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID): UserDto =
        userService.getUser(userId)
}
