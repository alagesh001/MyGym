package com.oxad.fitox.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.oxad.fitox.datastore.DataStoreManager

class MainAndroidViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application) // ✅ Safe Context

    /*fun doSomething() {
        val context = getApplication<Application>().applicationContext
        Toast.makeText(context, "Hello from ViewModel!", Toast.LENGTH_SHORT).show()
    }*/
}