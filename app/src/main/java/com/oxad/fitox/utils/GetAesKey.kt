package com.oxad.fitox.utils

import android.util.Base64
import android.util.Log
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// Generate a 256-bit AES Key (Run Once & Store Safely)
fun generateAESKey(): String {
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(256)
    val secretKey = keyGen.generateKey()
    return Base64.encodeToString(secretKey.encoded, Base64.DEFAULT) // Save this securely!
}

// Encrypt Function
fun encryptAES(data: String, secretKey: String, iv: String): String {
    val keyBytes = Base64.decode(secretKey, Base64.DEFAULT)
    val secretKeySpec = SecretKeySpec(keyBytes, "AES")
    val ivSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec)

    val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
}

// Generate a Random IV (Use this for each encryption)
fun generateIV(): String {
    val iv = ByteArray(16) // AES uses 16-byte IV
    java.security.SecureRandom().nextBytes(iv)
    return Base64.encodeToString(iv, Base64.DEFAULT) // Send IV along with the encrypted data
}

// Example Usage
fun encryptHere(value:String):String {
    //val secretKey = generateAESKey() // Generate and store securely!
    //val iv = generateIV() // New IV for each encryption
    val secretKey = "4gLr0RG835dSTNsEO0IUAyXG3Hf8vnBs7bpsyERPulQ=" // Generate and store securely!
    val iv = "ViAUchQdzQmO0lQaThADXA==" // New IV for each encryption
    val encryptedText = encryptAES(value, secretKey, iv)
    Log.e("Keyschecka","Value: $value")
    Log.e("Keyschecka","Encrypted: $encryptedText")
    Log.e("Keyschecka","Secret Key: $secretKey") // Store this safely!
    Log.e("Keyschecka","IV: $iv") // Send IV along with encrypted data
    return encryptedText
}
