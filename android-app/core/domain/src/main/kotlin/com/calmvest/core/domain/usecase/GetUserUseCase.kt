package com.calmvest.core.domain.usecase

import com.calmvest.core.domain.model.User
import com.calmvest.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<Result<User>> =
        userRepository.getUser(userId)
}
