package com.oxad.mygym.retrofit

import com.oxad.mygym.model.AdminLoginModel
import com.oxad.mygym.model.MemberResponse
import com.oxad.mygym.model.MyMemberListModel
import com.oxad.mygym.model.UserRegisterModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// User data model


// API service interface
interface ApiService {
    @FormUrlEncoded
    @POST("registerusers.php") // Adjust this to match your backend endpoint
    fun registerUser(@Field("name") name: String,
                     @Field("email") email: String): Call<UserRegisterModel>
    @GET("getmemberdata") // Adjust this to match your backend endpoint
    suspend  fun getmymembers(@Query("gymid") gymid: Int,
                     @Query("page") page: Int

    ):MemberResponse
    @GET("getgymlist") // Adjust this to match your backend endpoint
    fun loginAdmin(@Query("mobile") mob: Long,
                     @Query("password") pass: String): Call<AdminLoginModel>
    @FormUrlEncoded
    @POST("updatetable.php") // Adjust this to match your backend endpoint
    fun saveprofiledata(@Field("name") name: String,
                     @Field("email") email: String,
                        @Field("age") age: Int,
                        @Field("gender") gender: String,
                        @Field("type") type: Int,
                        @Field("height") height: Double,
                        @Field("weight") weight: Double,): Call<UserRegisterModel>

@FormUrlEncoded
@POST("addnewcustomer") // Adjust this to match your backend endpoint
fun addmymember(@Field("name") name: String,
                    @Field("email") email: String,
                    @Field("gymid") gymid: Int,
                    @Field("mob") mob: Long,
                    @Field("paidmonths") paidmonths: Int,): Call<UserRegisterModel>
}
// Response model
