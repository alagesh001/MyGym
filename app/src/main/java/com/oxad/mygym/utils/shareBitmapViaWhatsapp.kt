package com.oxad.mygym.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


fun shareBitmapToWhatsApp(context: Context, bitmap: Bitmap, phoneNumber: String) {
    try {
        // 1️⃣ Ensure Cache Directory Exists
        val cachePath = File(context.cacheDir, "images")  // ✅ Use internal cacheDir
        cachePath.mkdirs() // Create folder if it doesn't exist

        val file = File(cachePath, "shared_image.png")

        // 2️⃣ Save Bitmap as PNG
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

        // 3️⃣ Get URI using FileProvider
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        Log.e("Adfadfa","Sending")

        // 4️⃣ Create WhatsApp Intent
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra("jid", "$phoneNumber@s.whatsapp.net") // WhatsApp requires @s.whatsapp.net
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // 5️⃣ Start WhatsApp
        context.startActivity(intent)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

