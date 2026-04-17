package com.calmvest.domain.repository

import com.calmvest.domain.model.User
import com.calmvest.domain.model.UserId
import java.util.Optional

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UserId): Optional<User>
    fun findByEmail(email: String): Optional<User>
    fun findAll(): List<User>
    fun existsByEmail(email: String): Boolean
}
