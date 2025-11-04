package com.example.proyectologin005d.data.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionDataStore(val context: Context) {
    private val USER_KEY = stringPreferencesKey("current_user")

    val currentUser: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_KEY]
        }

    suspend fun saveUser(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = username
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }
}
