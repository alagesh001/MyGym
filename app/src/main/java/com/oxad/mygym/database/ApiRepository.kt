package com.oxad.mygym.database

import com.oxad.mygym.model.Member
import com.oxad.mygym.model.MemberResponse
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



}