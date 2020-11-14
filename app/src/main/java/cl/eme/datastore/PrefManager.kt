package cl.eme.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UserStatus {
    STARTED, LEVEL_1, LEVEL_2, VERIFIED
}

class PrefManager(context: Context) {

    private val dataStore = context.createDataStore(name = "pref_name")

    suspend fun setUserStatus(userStatus: UserStatus) {
        dataStore.edit { preferences ->

            preferences[USER_STATUS] = when (userStatus) {
                UserStatus.STARTED -> 1
                UserStatus.LEVEL_1 -> 2
                UserStatus.LEVEL_2 -> 3
                UserStatus.VERIFIED -> 4
            }
        }
    }

    val userStatusFlow: Flow<UserStatus> = dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        when (it[USER_STATUS] ?: 1) {
            1 -> UserStatus.STARTED
            2 -> UserStatus.LEVEL_1
            3 -> UserStatus.LEVEL_2
            4 -> UserStatus.VERIFIED
            else -> UserStatus.STARTED
        }
    }

    companion object {
        val USER_STATUS = preferencesKey<Int>("user_status")
    }
}