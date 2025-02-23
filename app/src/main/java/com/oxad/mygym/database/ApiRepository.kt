package com.oxad.mygym.database

import android.util.Log
import com.google.gson.Gson
import com.oxad.mygym.model.GetMyTrainersResponse
import com.oxad.mygym.model.LoginResult
import com.oxad.mygym.model.Member
import com.oxad.mygym.model.MemberResponse
import com.oxad.mygym.model.UserRegisterModel
import com.oxad.mygym.retrofit.RetrofitClient

class ApiRepository() {
        suspend fun getMembers(gymId: Int, page: Int): Result<MemberResponse> {
            return try {
                val response = RetrofitClient.instance.getmymembers(gymId, page)
                Result.success(response) // Return success
            } catch (e: Exception) {
                Result.failure(e) // Return failure with exception
            }
        }
    suspend fun updatemymembers(name:String,email:String, gymid: Int,mob:Long,userid:Long,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String): Result<UserRegisterModel> {
        return try {
            val response = RetrofitClient.instance.updatemymember(name,email,gymid,mob,userid,dob,height,weight, goal, trainerId,gender)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }

    }
    suspend fun updatemytrainer( name:String,mob:Long,gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        return try {
            val response = RetrofitClient.instance.updatemytrainer(name,gymid,mob,trainerId)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }

    }
    suspend fun renewmymembers( gymid: Int,paidmonths:Int,date:String,userid:Long): Result<UserRegisterModel> {
        return try {
            val response = RetrofitClient.instance.renewmymember(gymid,paidmonths,date,userid)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }

    }
    suspend fun deletemymembers( gymid: Int,userid:Long): Result<UserRegisterModel> {
        return try {
            val response = RetrofitClient.instance.deletemymember(gymid,userid)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }

    }
    suspend fun deletemytrainer( gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        return try {
            Log.e("cadfaaasf","gid $gymid tid $trainerId")
            val response = RetrofitClient.instance.deletemytrainer(gymid,trainerId)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }

    }
    suspend fun addmymembers(name:String,email:String, gymid: Int,mob:Long,paidmonths:Int,date:String,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String,referredBy:String): Result<UserRegisterModel> {
        return try {
            val response = RetrofitClient.instance.addmymember(name,email,gymid,mob,paidmonths,date,dob,height,weight, goal, trainerId,gender,referredBy)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }

    }
    suspend fun addmytrainer(name:String, gymid: Int,mob:Long): Result<UserRegisterModel> {
        return try {
            val response = RetrofitClient.instance.addmytrainer(name,gymid,mob)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }

    }
    suspend fun getTrainers(gymId: Int): Result<GetMyTrainersResponse> {
        return try {
            val response = RetrofitClient.instance.getmytrainer(gymId)
            Result.success(response) // Return success
        } catch (e: Exception) {
            Result.failure(e) // Return failure with exception
        }
    }
}