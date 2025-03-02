package com.oxad.mygym.utils

import android.content.Context
import android.widget.Toast
import kotlin.coroutines.CoroutineContext

class Utils {
    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}