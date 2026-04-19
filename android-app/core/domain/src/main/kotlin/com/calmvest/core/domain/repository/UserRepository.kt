package com.calmvest.core.domain.repository

import com.calmvest.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: String): Flow<Result<User>>
    suspend fun updateUser(userId: String, firstName: String, lastName: String): Result<User>
}
