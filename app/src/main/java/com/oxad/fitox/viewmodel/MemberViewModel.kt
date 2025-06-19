package com.oxad.fitox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.model.LoginResult
import com.oxad.fitox.model.Member
import com.oxad.fitox.model.MemberLoginModel
import com.oxad.fitox.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class MemberViewModel(private val apiService: ApiService,private val gymId: Int) : ViewModel() {

    var memResult: ArrayList<MemberLoginModel> = ArrayList()
    lateinit var apiRepoVM: ApiRepository

    fun setApiRepo(apiRepository: ApiRepository){
        apiRepoVM = apiRepository
    }
}
