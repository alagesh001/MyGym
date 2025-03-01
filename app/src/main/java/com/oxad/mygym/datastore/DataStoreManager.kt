package com.oxad.mygym.datastore

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oxad.mygym.model.TrainerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

private val Context.dataStore by preferencesDataStore(name = "coach_prefs")
private val Context.dataStoreKey by preferencesDataStore(name = "secure_user_prefs")

class DataStoreManager(private val context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    companion object {
        private val COACHES_KEY = stringPreferencesKey("coaches_list")
        private val REFERRAL_MESSAGE_KEY = stringPreferencesKey("referral_message")
                private val MOBILE_NUMBER = stringPreferencesKey("mobile_number")
                private val PASSWORD = stringPreferencesKey("password")
        private const val AES_MODE = "AES/GCM/NoPadding"
        private const val SECRET_KEY_ALIAS = "encrypted_key"
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


    // ✅ Save Mobile Number & Password (Encrypted)
    suspend fun saveUserCredentials(mobileNumber: String, password: String) {
        val encryptedNumber = encryptData(mobileNumber)
        val encryptedPassword = encryptData(password)

        context.dataStore.edit { prefs ->
            prefs[MOBILE_NUMBER] = encryptedNumber
            prefs[PASSWORD] = encryptedPassword
        }
    }

    // ✅ Retrieve Mobile Number (Decrypted or null)
    val mobileNumber: Flow<String?> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs -> prefs[MOBILE_NUMBER]?.let { decryptData(it) } }

    // ✅ Retrieve Password (Decrypted or null)
    val password: Flow<String?> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs -> prefs[PASSWORD]?.let { decryptData(it) } }

    // ✅ Clear Stored Credentials (Logout)
    suspend fun clearUserData() {
        context.dataStore.edit { it.clear() }
    }

    // ✅ Encrypt Data using AES-256 GCM
    private fun encryptData(data: String): String {
        val cipher = Cipher.getInstance(AES_MODE)
        val key = getSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        return Base64.encodeToString(iv + encryptedBytes, Base64.DEFAULT) // Store IV + Data
    }

    // ✅ Decrypt Data
    private fun decryptData(data: String): String {
        val decodedData = Base64.decode(data, Base64.DEFAULT)
        val key = getSecretKey()
        val cipher = Cipher.getInstance(AES_MODE)

        val iv = decodedData.copyOfRange(0, 12) // Extract IV
        val encryptedBytes = decodedData.copyOfRange(12, decodedData.size) // Extract Encrypted Data

        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }

    // ✅ Generate & Store Secret Key (AES-256)
    private fun getSecretKey(): SecretKey {
        val sharedPrefs = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
        var storedKey = sharedPrefs.getString(SECRET_KEY_ALIAS, null)

        if (storedKey == null) {
            val keyBytes = ByteArray(32) // 256-bit AES Key
            SecureRandom().nextBytes(keyBytes)
            storedKey = Base64.encodeToString(keyBytes, Base64.DEFAULT)
            sharedPrefs.edit().putString(SECRET_KEY_ALIAS, storedKey).apply()
        }

        val keyBytes = Base64.decode(storedKey, Base64.DEFAULT)
        return SecretKeySpec(keyBytes, "AES")
    }
}
