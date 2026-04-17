package com.calmvest.core.data.fake

import com.calmvest.core.domain.model.User
import com.calmvest.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : UserRepository {

    private val fakeUser = User(
        id = "demo-user-id",
        email = "emma@example.com",
        firstName = "Emma",
        lastName = "Müller",
        createdAt = "2024-01-15"
    )

    override fun getUser(userId: String): Flow<Result<User>> = flow {
        emit(Result.success(fakeUser))
    }

    override suspend fun updateUser(
        userId: String,
        firstName: String,
        lastName: String
    ): Result<User> = Result.success(fakeUser.copy(firstName = firstName, lastName = lastName))
}
