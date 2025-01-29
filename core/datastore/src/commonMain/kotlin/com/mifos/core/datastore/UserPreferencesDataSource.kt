package com.mifos.core.datastore


import com.mifos.core.datastore.model.UserData
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.decodeValueOrNull
import kotlinx.coroutines.withContext
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.ExperimentalSerializationApi


private const val USER_DATA = "userData"

class UserPreferencesDataSource(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher,
) {

    @OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
    private val _userInfo = MutableStateFlow(
        settings.decodeValue(
            key = USER_DATA,
            serializer = UserData.serializer(),
            defaultValue = settings.decodeValueOrNull(
                key = USER_DATA,
                serializer = UserData.serializer(),
            ) ?: UserData.DEFAULT,
        ),
    )

    val userInfo = _userInfo

    suspend fun updateUserInfo(user: UserData) {
        withContext(dispatcher) {
            settings.putUserPreference(user)
            _userInfo.value = user
        }
    }

    suspend fun clearInfo() {
        withContext(dispatcher) {
            settings.clear()
        }
    }
}


@OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
private fun Settings.putUserPreference(user: UserData) {
    encodeValue(
        key = USER_DATA,
        serializer = UserData.serializer(),
        value = user,
    )
}
