package com.oxad.mygym.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oxad.mygym.model.TrainerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "coach_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        private val COACHES_KEY = stringPreferencesKey("coaches_list")
        private val REFERRAL_MESSAGE_KEY = stringPreferencesKey("referral_message")
    }

    // ✅ Save coaches list
    suspend fun saveCoaches(coaches: List<TrainerModel>) {
        val json = Gson().toJson(coaches) // Convert list to JSON
        context.dataStore.edit { preferences ->
            preferences[COACHES_KEY] = json
        }
    }

    // ✅ Retrieve coaches list
    fun getCoaches(): Flow<List<TrainerModel>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[COACHES_KEY] ?: "[]"
            val type = object : TypeToken<List<TrainerModel>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        }
    }
    suspend fun clearCoaches() {
        context.dataStore.edit { preferences ->
            preferences.remove(COACHES_KEY) // Removes the stored coaches
        }
    }
    suspend fun saveReferralMessage(message: String) {
        context.dataStore.edit { preferences ->
            preferences[REFERRAL_MESSAGE_KEY] = message
        }
    }

    // Function to get referral message
    val referralMessage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[REFERRAL_MESSAGE_KEY] ?: "Join my gym using this referral code!"
        }

}
