package com.oxad.fitox.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.database.MemberDao
import com.oxad.fitox.database.WorkoutDao
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.model.LoginResult
import com.oxad.fitox.model.Member
import com.oxad.fitox.model.Product
import com.oxad.fitox.model.ProductRes
import com.oxad.fitox.model.ProductResponse
import com.oxad.fitox.model.TrainerModel
import com.oxad.fitox.model.Transaction
import com.oxad.fitox.model.Workout
import com.oxad.fitox.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel() : ViewModel() {
    var gymResult: ArrayList<LoginResult> = ArrayList()
    var a_keyIs: String = ""

    var currentGymSelected: Int = 0
    private val _memberList = MutableStateFlow<List<Member>>(emptyList())
    val memberList: StateFlow<List<Member>> = _memberList
    var totalTransacPage = 1
    private val _transacList = MutableStateFlow<List<Transaction>>(emptyList())
    val transacList: StateFlow<List<Transaction>> = _transacList

    var totalOldDataPage = 1
    private val _olddataList = MutableStateFlow<List<Member>>(emptyList())
    val oldDataList: StateFlow<List<Member>> = _olddataList

    lateinit var memberDao: MemberDao
    lateinit var workoutDao: WorkoutDao
    var gymCoachOpenedFirsttime = false
    var gymProfileOpenedFirsttime = false
    var gymProductOpenedFirsttime = false

    private val _trainerList = MutableStateFlow<List<TrainerModel>>(emptyList())
    val trainerList: StateFlow<List<TrainerModel>> = _trainerList
    var dieticianData: TrainerModel ?= null

    private var currentPage = 1
    private val limit = 5
     var isLastPage = false

    private val _searchQuery = MutableLiveData<String>()
    private val _workouts = MutableStateFlow<List<Workout>>(emptyList())
    val workouts: StateFlow<List<Workout>> = _workouts
    lateinit var dataStoreManagerVM:DataStoreManager
    lateinit var apiRepoVM: ApiRepository
    private val _productData = MutableStateFlow<List<Product>>(emptyList())
    val productData: StateFlow<List<Product>> = _productData
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _isExpanded = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean> = _isExpanded
    fun setApiRepo(apiRepository: ApiRepository){
        apiRepoVM = apiRepository
    }
    fun fetchgymGuide(muscleGroup:String) {
        viewModelScope.launch {
            val cachedWorkouts = workoutDao.getWorkoutstype(muscleGroup)
           //Log.e("jdacjadf","cachwor  "+Gson().toJson(cachedWorkouts))

            _isLoading.value = true
            if (cachedWorkouts.isNotEmpty()) {
              //  Log.e("jdacjadf","Not Empty $muscleGroup  ")
                _isLoading.value = false

                _workouts.value =  cachedWorkouts // Return cached data
                //Log.e("jdacjadf","cachwor AA "+Gson().toJson(workouts))

            } else {
               // Log.e("jdacjadf"," Empty $muscleGroup ")

                try {
                   // Log.e("jdacjadf","Calling api  ")

                    val result = apiRepoVM.getWorkoutsByMuscleGroup("")
                    result.onSuccess { response ->
                        if(response.result.isNotEmpty()) {
                           // _workouts.value = response.result
                            fetchgymGuide(muscleGroup)
                            workoutDao.insertWorkouts(response.result)
                        }
                    }
                    result.onFailure { exception ->
                        _workouts.value = emptyList() // Handle error case

                    }

                  //      Log.e("jdacjadf","ApiRepository "+Gson().toJson(workouts))
                } catch (e: Exception) {
                    _workouts.value = emptyList() // Handle error case
                  //   Log.e("jdacjadf","ApiRepository catch "+e.message)

                } finally {
                     //Log.e("jdacjadf","ApiRepository finally ")

                    _isLoading.value = false
                }

        }

        }

    }
    fun loadProductsData() {
        viewModelScope.launch {
            try {
                val result = apiRepoVM.getProducts(gymResult[currentGymSelected].GymId)
                result.onSuccess { response ->
                   // Log.e("onSuccessdsf","CC "+Gson().toJson(response.result))

                    _productData.value = response.result
                  //  Log.e("onSuccessdsf","sstsAA ")

                }
                result.onFailure { exception ->
                    _productData.value = emptyList() // Handle error case

                }

                //    Log.e("jdacjadf","res "+Gson().toJson(workouts))
            } catch (e: Exception) {
                _productData.value = emptyList() // Handle error case
                // Log.e("jdacjadf","catch "+e.message)

            } finally {
                // Log.e("jdacjadf","finally ")

               // _isLoading.value = false
            }
        }
    }
    fun fetchWorkouts(muscleGroup: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = apiRepoVM.getWorkoutsByMuscleGroup(muscleGroup)
                result.onSuccess { response ->
                    _workouts.value = response.result

                }
                result.onFailure { exception ->
                    _workouts.value = emptyList() // Handle error case

                }

            //    Log.e("jdacjadf","res "+Gson().toJson(workouts))
            } catch (e: Exception) {
                _workouts.value = emptyList() // Handle error case
               // Log.e("jdacjadf","catch "+e.message)

            } finally {
               // Log.e("jdacjadf","finally ")

                _isLoading.value = false
            }
        }
    }
    val members: LiveData<List<Member>> = _searchQuery.switchMap { query ->
        memberDao.searchMembersByName(query,gymResult[currentGymSelected].GymId)

    /*    if (query.isNullOrBlank()) {
            MutableLiveData(emptyList())
        } else {
            memberDao.searchMembersByName(query)
        }*/
    }
    suspend fun getMaleMembers(): List<Member> {
        return withContext(Dispatchers.IO) {
            memberList.value.filter { it.gender == "Male" }
        }
    }
    suspend fun getTotalMemberList(): List<Member> {
        return withContext(Dispatchers.IO) {
            memberList.value
        }
    }
    suspend fun getTrainerList(): List<TrainerModel> {
        return withContext(Dispatchers.IO) {
            trainerList.value
        }
    }
    suspend fun getFemaleMembers(): List<Member> {
        return withContext(Dispatchers.IO) {
            memberList.value.filter { it.gender == "Female" }
        }
    }
    fun searchMembers(query: String) {
        _searchQuery.value = query
    }

    fun loadTrainerData(dataStoreManager: DataStoreManager){

    viewModelScope.launch {
        dataStoreManagerVM  = dataStoreManager
              //  Log.d("DataStoreA", "Not Stored")

                val result = apiRepoVM.getTrainers(gymResult[currentGymSelected].GymId)
               // Log.e("chadka",""+ Gson().toJson(result))
                result.onSuccess { response ->
                    if(response.success){
                        _trainerList.value = response.result
                        dieticianData = response.result.find { it.role == "dietician" }

                        dataStoreManagerVM.saveCoaches(response.result)
                    }else{
                        _trainerList.value = emptyList()
                        dataStoreManagerVM.saveCoaches(emptyList())
                    }
                }

                result.onFailure { exception ->
                    exception.message ?: "Something went wrong!"
                }
            }




}

    fun loadOldMemberHistory(page:Int) {
        //  Log.e("caaca","A page check $page totalpage: $totalTransacPage")
        // Log.e("checktloa","viewmodrel api  $page")
       // Log.e("adfasdf","API page $page")

        if(page == 1){
            _olddataList.value = emptyList()
        }
        if(page in 1..totalOldDataPage) {
            // Log.e("caaca","Apicall")

            viewModelScope.launch {

                val result =apiRepoVM.getOldMembers(gymResult[currentGymSelected].GymId,page,a_keyIs)
                result.onSuccess { response ->

                    if (response.success) {
                     //   Log.e("adfasdf","Success page $page")

                        _olddataList.value = response.result
                        totalOldDataPage = response.totalPages
                       // Log.e("adfasdf","Total page $totalOldDataPage")

                        //  Log.e("chejkaf","Apicall resp ${Gson().toJson(_transacList.value )}")

                    }else{
                        _olddataList.value = emptyList()
                    }
                }
                result.onFailure { exception ->
                    exception.message ?: "Something went wrong!"
                    //  _transacList.value = emptyList()
                }


            }
        }
    }
    fun loadTransacHistoryMember(userid:String,context: Context) {
        //  Log.e("caaca","A page check $page totalpage: $totalTransacPage")
        // Log.e("checktloa","viewmodrel api  $page")
            // Log.e("caaca","Apicall")

            viewModelScope.launch {

                val result = apiRepoVM.getTransacHist(gymResult[currentGymSelected].GymId, -1,userid)
                result.onSuccess { response ->

                    if (response.success) {
                        _transacList.value = response.result
                        totalTransacPage = response.total_pages
                        //  Log.e("chejkaf","Apicall resp ${Gson().toJson(_transacList.value )}")

                    }else{
                        _transacList.value = emptyList()
                        Utils.showToast(context,response.message)
                    }
                }
                result.onFailure { exception ->
                    exception.message ?: "Something went wrong!"
                    //  _transacList.value = emptyList()
                }


            }

    }
    fun loadTransacHistory(page:Int) {
      //  Log.e("caaca","A page check $page totalpage: $totalTransacPage")
       // Log.e("checktloa","viewmodrel api  $page")

        if(page == 1){
            _transacList.value = emptyList()
        }
if(page in 1..totalTransacPage) {
   // Log.e("caaca","Apicall")

    viewModelScope.launch {

        val result = apiRepoVM.getTransacHist(gymResult[currentGymSelected].GymId, page,"11")
        result.onSuccess { response ->

            if (response.success) {
                _transacList.value = response.result
                totalTransacPage = response.total_pages
              //  Log.e("chejkaf","Apicall resp ${Gson().toJson(_transacList.value )}")

            }else{
                    _transacList.value = emptyList()
            }
        }
        result.onFailure { exception ->
            exception.message ?: "Something went wrong!"
          //  _transacList.value = emptyList()
        }


    }
}
    }
    fun loadMembers() {

        viewModelScope.launch {
            val cachedMembers = memberDao.getAllMembers(gymResult[currentGymSelected].GymId)
           // Log.e("chadka","cachedMembers dataStore "+ Gson().toJson(cachedMembers))

            if (cachedMembers.isNotEmpty()) {
                _memberList.value =  cachedMembers // Return cached data
            } else {
                val result =apiRepoVM.getMembers(gymResult[currentGymSelected].GymId,currentPage,a_keyIs)
                if(currentPage ==1) {
                    _memberList.value = emptyList()
                }
                result.onSuccess { response ->
                   // Log.e("chadka", "" + Gson().toJson(response))


                    if (response.success) {
                        _memberList.value = (_memberList.value ?: emptyList()) + response.result
                       // Log.e("chadka","abcd AAA "+ Gson().toJson( _memberList.value))

                      //  Log.e("chadka","abcd BBB "+ Gson().toJson(response.result))

                        if (currentPage < response.totalPages) {
                            currentPage++ // Move to next page
                            loadMembers()
                        } else {
                            isLastPage = true // No more data
                            memberDao.insertMembers(_memberList.value)

                            // Log.e("chadka","get all data "+ Gson().toJson(memberDao.getAllMembers()))

                        }
                    }else{
                        _memberList.value = emptyList()
                    }
                }
                result.onFailure { exception ->
                    exception.message ?: "Something went wrong!"
                    if(currentPage == 1) {
                        _memberList.value = emptyList()
                    }
                }


            }



        }
    }

    fun refreshMembers() {
        viewModelScope.launch {
            memberDao.clearMembers(gymResult[currentGymSelected].GymId)
            currentPage = 1
            isLastPage = false
            _memberList.value = emptyList()
            loadMembers() // Reload first page
        }
    }
    fun clearmembers(isswipe: Boolean = false) {

        viewModelScope.launch {
            memberDao.clearMembers(gymResult[currentGymSelected].GymId)
            currentPage = 1
            isLastPage = false
            _memberList.value = emptyList()
            if (isswipe){
                loadMembers()
            }

        }
    }

}