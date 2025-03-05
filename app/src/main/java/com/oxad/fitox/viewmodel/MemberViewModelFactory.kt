package com.oxad.fitox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oxad.fitox.retrofit.ApiService

class ViewModelFactory(
    private val apiService: ApiService,
    private val gymId: Int)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemberViewModel::class.java)) {
            return MemberViewModel(apiService, gymId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
