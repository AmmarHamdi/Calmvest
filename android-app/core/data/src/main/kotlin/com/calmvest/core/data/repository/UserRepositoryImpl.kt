package com.calmvest.core.data.repository

import com.calmvest.core.data.remote.api.CalmvestApi
import com.calmvest.core.data.remote.dto.toDomain
import com.calmvest.core.domain.model.User
import com.calmvest.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: CalmvestApi
) : UserRepository {

    override fun getUser(userId: String): Flow<Result<User>> = flow {
        emit(runCatching { api.getUser(userId).toDomain() })
    }

    override suspend fun updateUser(userId: String, firstName: String, lastName: String): Result<User> =
        runCatching {
            api.updateUser(
                userId,
                mapOf("first_name" to firstName, "last_name" to lastName)
            ).toDomain()
        }
}
