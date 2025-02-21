package com.oxad.mygym.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
    private var isLastPage = false
fun loadTrainerData(dataStoreManager: DataStoreManager){
    viewModelScope.launch {

        dataStoreManager.getCoaches().collect { coaches ->
            Log.d("DataStoreA", "Stored Coaches: $coaches")
            if(coaches.isEmpty()){
                Log.d("DataStoreA", "Not Stored")

                val result = ApiRepository().getTrainers(gymResult[currentGymSelected].GymId)
                Log.e("chadka",""+ Gson().toJson(result))
                result.onSuccess { response ->
                    if(response.success){
                        _trainerList.value = response.result
                        dataStoreManager.saveCoaches(response.result)

                    }else{

                    }
                }

                result.onFailure { exception ->
                    exception.message ?: "Something went wrong!"
                }
            }else{
                Log.d("DataStoreA", "Stored "+Gson().toJson(coaches))
                _trainerList.value = coaches

            }

            }

    }
}
    fun loadMembers() {
        if (isLastPage) return // Stop loading if last page reached

        viewModelScope.launch {
            val cachedMembers = memberDao.getAllMembers()

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

    fun refresh() {
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