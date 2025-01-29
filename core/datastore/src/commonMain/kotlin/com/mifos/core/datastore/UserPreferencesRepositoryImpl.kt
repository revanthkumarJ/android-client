package com.mifos.core.datastore

import com.mifos.core.datastore.model.UserData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


class UserPreferencesRepositoryImpl(
    private val preferenceManager: UserPreferencesDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    unconfinedDispatcher: CoroutineDispatcher,
) : UserPreferencesRepository {
    private val unconfinedScope = CoroutineScope(unconfinedDispatcher)
    override val userInfo: Flow<UserData>
        get() = preferenceManager.userInfo

    override suspend fun updateUser(user: UserData): Result<Unit> {
        return try {
            val result = preferenceManager.updateUserInfo(user)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logOut() {
        preferenceManager.clearInfo()
    }
}