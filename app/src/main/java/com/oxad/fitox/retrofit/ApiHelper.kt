package com.oxad.fitox.retrofit

object ApiHelper {
    init {
        System.loadLibrary("native-lib") // Replace with your actual library name
    }

    external fun getZeepLiveThimblesCoinIvKey(): String
}
