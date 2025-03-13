package com.oxad.fitox.retrofit

import com.oxad.fitox.model.AdminLoginModel
import com.oxad.fitox.model.GetMyTrainersResponse
import com.oxad.fitox.model.GymGuideResponse
import com.oxad.fitox.model.MemberResponse
import com.oxad.fitox.model.UserRegisterModel
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
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String
    ): Call<UserRegisterModel>

    @GET("getmemberdata") // Adjust this to match your backend endpoint
    suspend fun getmymembers(
        @Query("gymid") gymid: Int,
        @Query("page") page: Int

    ): MemberResponse

    @GET("getdumbellworkout")
    suspend fun getWorkoutsByMuscleGroup(@Query("muscle_group") muscleGroup: String): GymGuideResponse


    @GET("gettrainerdata") // Adjust this to match your backend endpoint
    suspend fun getmytrainer(@Query("gymid") gymid: Int): GetMyTrainersResponse

    @GET("getgymlist") // Adjust this to match your backend endpoint
    fun loginAdmin(
        @Query("mobile") mob: Long,
        @Query("password") pass: String
    ): Call<AdminLoginModel>

    @FormUrlEncoded
    @POST("updatetable.php") // Adjust this to match your backend endpoint
    fun saveprofiledata(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("age") age: Int,
        @Field("gender") gender: String,
        @Field("type") type: Int,
        @Field("height") height: Double,
        @Field("weight") weight: Double,
    ): Call<UserRegisterModel>

    @FormUrlEncoded
    @POST("addnewcustomer") // Adjust this to match your backend endpoint
    suspend fun addmymember(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("gymid") gymid: Int,
        @Field("mob") mob: Long,
        @Field("paidmonths") paidmonths: Int,
        @Field("date") date: String,
        @Field("dob") dob: String,
        @Field("height") height: Float,
        @Field("weight") weight: Float,
        @Field("goal") goal: Int,
        @Field("trainer") trainerId: Long,
        @Field("gender") gender: String,
        @Field("referredBy") referredBy: String,
        @Field("amount_paid") amount_paid: Int,
        @Field("due_amount") due_amount: Int,


    ): UserRegisterModel
    @FormUrlEncoded
    @POST("updatetable") // Adjust this to match your backend endpoint
    suspend fun updatemymember(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("gymid") gymid: Int,
        @Field("mob") mob: Long,
        @Field("userid") userid: Long,
        @Field("dob") dob: String,
        @Field("height") height: Float,
        @Field("weight") weight: Float,
        @Field("goal") goal: Int,
        @Field("trainer") trainerId: Long,
        @Field("gender") gender: String,
        @Field("isRenew") isRenew: Int =0,


    ): UserRegisterModel
    @FormUrlEncoded
    @POST("updatetrainer") // Adjust this to match your backend endpoint
    suspend fun updatemytrainer(
        @Field("name") name: String,
        @Field("gymid") gymid: Int,
        @Field("mob") mob: Long,
        @Field("trainer") trainerId: Long,
        ): UserRegisterModel

    @FormUrlEncoded
    @POST("updatedueamt") // Adjust this to match your backend endpoint
    suspend fun duePay(
        @Field("name") name: String,
        @Field("gymid") gymid: Int,
        @Field("userid") userid: Long,
        @Field("amount_paid") amount_paid: Int,
        @Field("due_amount") due_amount: Int,
        ): UserRegisterModel

    @FormUrlEncoded
    @POST("updatetable") // Adjust this to match your backend endpoint
    suspend fun renewmymember(
        @Field("gymid") gymid: Int,
        @Field("paidmonths") paidmonths: Int,
        @Field("date") date: String,
        @Field("userid") userid: Long,
        @Field("isRenew") isRenew: Int =1,
        @Field("amount_paid") amount_paid: Int,
        @Field("due_amount") due_amount: Int,

    ): UserRegisterModel
    @FormUrlEncoded
    @POST("deletemycustomer") // Adjust this to match your backend endpoint
    suspend fun deletemymember(
        @Field("gymid") gymid: Int,
        @Field("userid") userid: Long,
    ): UserRegisterModel
    @FormUrlEncoded
    @POST("removetrainer") // Adjust this to match your backend endpoint
    suspend fun deletemytrainer(
        @Field("gymid") gymid: Int,
        @Field("coachid") coachid: Long,
    ): UserRegisterModel
    @FormUrlEncoded
    @POST("addmytrainer") // Adjust this to match your backend endpoint
    suspend fun addmytrainer(
        @Field("name") name: String,
        @Field("gymid") gymid: Int,
        @Field("mob") mob: Long,
    ): UserRegisterModel
    @FormUrlEncoded
    @POST("updategymownerpassword") // Adjust this to match your backend endpoint
    suspend fun updateGymPassword(
        @Field("gymid") gymid: Int,
        @Field("oldpass") oldpass: String,
        @Field("newpass") newpass: String,

        ): UserRegisterModel
}


// Response model
