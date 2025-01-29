package com.mifos.core.datastore

import com.mifos.core.datastore.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserPreferencesRepository {
    val userInfo: Flow<UserData>

    suspend fun updateUser(user: UserData): Result<Unit>

    suspend fun logOut(): Unit
}
