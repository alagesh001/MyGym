package com.oxad.fitox.database

import com.oxad.fitox.model.GetMyTrainersResponse
import com.oxad.fitox.model.GymGuideResponse
import com.oxad.fitox.model.MemberResponse
import com.oxad.fitox.model.UserRegisterModel
import com.oxad.fitox.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository() {
        suspend fun getMembers(gymId: Int, page: Int): Result<MemberResponse> {
            return safeApiCall {
                RetrofitClient.instance.getmymembers(gymId, page)
            }
        }
    suspend fun getWorkoutsByMuscleGroup(muscleGroup: String): Result<GymGuideResponse> {
        return safeApiCall { RetrofitClient.instance.getWorkoutsByMuscleGroup(muscleGroup) }

    }
    suspend fun updatemymembers(name:String,email:String, gymid: Int,mob:Long,userid:Long,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updatemymember(name,email,gymid,mob,userid,dob,height,weight, goal, trainerId,gender)        }

    }
    suspend fun updatemytrainer( name:String,mob:Long,gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updatemytrainer(name,gymid,mob,trainerId)
        }


    }
    suspend fun renewmymembers( gymid: Int,paidmonths:Int,date:String,userid:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.renewmymember(gymid,paidmonths,date,userid)        }


    }
    suspend fun deletemymembers( gymid: Int,userid:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.deletemymember(gymid,userid)      }

    }
    suspend fun deletemytrainer( gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.deletemytrainer(gymid,trainerId)
        }

    }
    suspend fun addmymembers(name:String,email:String, gymid: Int,mob:Long,paidmonths:Int,date:String,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String,referredBy:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.addmymember(
                name,
                email,
                gymid,
                mob,
                paidmonths,
                date,
                dob,
                height,
                weight,
                goal,
                trainerId,
                gender,
                referredBy
            )
        }


    }
    suspend fun addmytrainer(name:String, gymid: Int,mob:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.addmytrainer(name,gymid,mob)
        }


    }
    suspend fun updategympass( gymid: Int,oldpass:String,newpass:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updateGymPassword(gymid,oldpass,newpass)
        }


    }
    suspend fun getTrainers(gymId: Int): Result<GetMyTrainersResponse> {
        return safeApiCall {
            RetrofitClient.instance.getmytrainer(gymId)
        }
    }
    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(apiCall())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}