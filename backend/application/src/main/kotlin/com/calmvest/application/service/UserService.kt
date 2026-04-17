package com.calmvest.application.service

import com.calmvest.application.dto.CreateUserRequest
import com.calmvest.application.dto.UserDto
import com.calmvest.domain.model.KycStatus
import com.calmvest.domain.model.User
import com.calmvest.domain.model.UserId
import com.calmvest.domain.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    fun createUser(request: CreateUserRequest): UserDto {
        val email = request.email.lowercase().trim()
        require(!userRepository.existsByEmail(email)) {
            "User with email $email already exists"
        }

        log.info("Creating user with email {}", email)

        val now = Instant.now()
        val user = User(
            id = UserId.generate(),
            email = email,
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
