package com.oxad.mygym.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.oxad.mygym.database.ApiRepository
import com.oxad.mygym.database.AppDatabase
import com.oxad.mygym.database.MemberDao
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.model.LoginResult
import com.oxad.mygym.model.Member
import com.oxad.mygym.model.TrainerModel
import com.oxad.mygym.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class MainViewModel() : ViewModel() {
    var gymResult: ArrayList<LoginResult> = ArrayList()
    var currentGymSelected: Int = 0
    private val _memberList = MutableStateFlow<List<Member>>(emptyList())
    val memberList: StateFlow<List<Member>> = _memberList
    lateinit var memberDao: MemberDao
    private val _trainerList = MutableStateFlow<List<TrainerModel>>(emptyList())
    val trainerList: StateFlow<List<TrainerModel>> = _trainerList
    private var currentPage = 1
    private val limit = 5
     var isLastPage = false

    private val _searchQuery = MutableLiveData<String>()

    val members: LiveData<List<Member>> = _searchQuery.switchMap { query ->
        memberDao.searchMembersByName(query)

    /*    if (query.isNullOrBlank()) {
            MutableLiveData(emptyList())
        } else {
            memberDao.searchMembersByName(query)
        }*/
    }

    fun searchMembers(query: String) {
        _searchQuery.value = query
    }

    fun loadTrainerData(dataStoreManager: DataStoreManager){
    viewModelScope.launch {

                Log.d("DataStoreA", "Not Stored")

                val result = ApiRepository().getTrainers(gymResult[currentGymSelected].GymId)
                Log.e("chadka",""+ Gson().toJson(result))
                result.onSuccess { response ->
                    if(response.success){
                        _trainerList.value = response.result
                        dataStoreManager.saveCoaches(response.result)
                    }else{
                        _trainerList.value = emptyList()
                        dataStoreManager.saveCoaches(emptyList())
                    }
                }

                result.onFailure { exception ->
                    exception.message ?: "Something went wrong!"
                }
            }




}
    fun loadMembers() {

        viewModelScope.launch {
            val cachedMembers = memberDao.getAllMembers()
            Log.e("chadka","cachedMembers data"+ Gson().toJson(cachedMembers))

            if (cachedMembers.isNotEmpty()) {
                _memberList.value =  cachedMembers // Return cached data
            } else {

                val result = ApiRepository().getMembers(gymResult[currentGymSelected].GymId,currentPage)
                result.onSuccess { response ->
                   // Log.e("chadka",""+ Gson().toJson(response))

                    _memberList.value = (_memberList.value ?: emptyList()) + response.result
                    if (currentPage < response.totalPages) {
                        currentPage++ // Move to next page
                        loadMembers()
                    } else {
                        isLastPage = true // No more data
                        memberDao.insertMembers(_memberList.value)
                       // Log.e("chadka","get all data "+ Gson().toJson(memberDao.getAllMembers()))

                    }
                }

                result.onFailure { exception ->
                    exception.message ?: "Something went wrong!"
                }


            }



        }
    }

    fun refreshMembers() {
        viewModelScope.launch {
            memberDao.clearMembers()
            currentPage = 1
            isLastPage = false
            _memberList.value = emptyList()
            loadMembers() // Reload first page
        }
    }
    fun clearmembers() {
        viewModelScope.launch {
            memberDao.clearMembers()
            currentPage = 1
            isLastPage = false
            _memberList.value = emptyList()
        }
    }

}