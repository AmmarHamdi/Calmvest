package com.calmvest.application.service

import com.calmvest.application.dto.CreateUserRequest
import com.calmvest.application.dto.UserDto
import com.calmvest.domain.model.KycStatus
import com.calmvest.domain.model.User
import com.calmvest.domain.model.UserId
import com.calmvest.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(request: CreateUserRequest): UserDto {
        require(request.email.isNotBlank()) { "Email must not be blank" }
        require(request.name.isNotBlank()) { "Name must not be blank" }
        require(!userRepository.existsByEmail(request.email)) {
            "User with email ${request.email} already exists"
        }

        val now = Instant.now()
        val user = User(
            id = UserId.generate(),
            email = request.email.lowercase().trim(),
            name = request.name.trim(),
            kycStatus = KycStatus.PENDING,
            createdAt = now,
            updatedAt = now
        )
        return userRepository.save(user).toDto()
    }

    @Transactional(readOnly = true)
    fun getUser(userId: UUID): UserDto {
        return userRepository.findById(UserId(userId))
            .map { it.toDto() }
            .orElseThrow { NoSuchElementException("User not found: $userId") }
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserDto> =
        userRepository.findAll().map { it.toDto() }

    private fun User.toDto() = UserDto(
        id = id.value,
        email = email,
        name = name,
        kycStatus = kycStatus,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
