package com.oxad.fitox.database

import com.oxad.fitox.model.GetMyTrainersResponse
import com.oxad.fitox.model.GetMyTransacHist
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
    suspend fun duePay( name:String,gymid: Int,userid:Long,amount_paid:Int,due_amount:Int): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.duePay(name,gymid,userid, amount_paid,due_amount)        }


    }
    suspend fun renewmymembers( gymid: Int,paidmonths:Int,date:String,userid:Long,amount_paid:Int,due_amount:Int): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.renewmymember(gymid,paidmonths,date,userid, 1,amount_paid,due_amount)        }


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
    suspend fun addmymembers(name:String,email:String, gymid: Int,mob:Long,paidmonths:Int,date:String,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String,referredBy:String,amount_paid:Int,due_amount:Int): Result<UserRegisterModel> {
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
                referredBy,
                amount_paid,
                due_amount
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
    suspend fun getTransacHist(gymId: Int, page: Int): Result<GetMyTransacHist> {
        return safeApiCall {
            RetrofitClient.instance.getTransacHist(gymId, page)
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