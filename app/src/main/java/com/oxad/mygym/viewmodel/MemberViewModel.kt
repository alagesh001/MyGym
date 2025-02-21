package com.oxad.mygym.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.oxad.mygym.model.Member
import com.oxad.mygym.retrofit.ApiService
import com.oxad.mygym.utils.MemberPagingSource
import kotlinx.coroutines.flow.Flow

class MemberViewModel(private val apiService: ApiService,private val gymId: Int) : ViewModel() {

    val memberList: Flow<PagingData<Member>> = Pager(
        config = PagingConfig(
            pageSize = 5, // Load 5 items per page
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MemberPagingSource(apiService,gymId) }
    ).flow.cachedIn(viewModelScope)
}
