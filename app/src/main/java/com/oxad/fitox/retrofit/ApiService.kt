package com.oxad.fitox.retrofit

import com.oxad.fitox.model.AdminContUpdateModel
import com.oxad.fitox.model.AdminLoginModel
import com.oxad.fitox.model.CustResponse
import com.oxad.fitox.model.DashDataModel
import com.oxad.fitox.model.GetMyTrainersResponse
import com.oxad.fitox.model.GetMyTransacHist
import com.oxad.fitox.model.GymGuideResponse
import com.oxad.fitox.model.MarkAttendModel
import com.oxad.fitox.model.MemberResponse
import com.oxad.fitox.model.ProductResponse
import com.oxad.fitox.model.UserRegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

// User data model


// API service interface
interface ApiService {
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    fun registerUser(
        @Url url: String,
        @Field("name") name: String,
        @Field("email") email: String
    ): Call<UserRegisterModel>
    @Multipart
    @POST
    suspend fun updateProducts(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Part("name") name: RequestBody,
        @Part("offerprice") offerprice: RequestBody,
        @Part("actualprice") actualprice: RequestBody,
        @Part("gymid") gymid: RequestBody,
        @Part("productId") pid: RequestBody,

        @Part pic: MultipartBody.Part?
    ): UserRegisterModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun deleteProduct(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Field("gymid") gid: Int,
        @Field("productId") pid: Int,
        @Field("isDelete") isDelete: Int = 1

    ): UserRegisterModel
    @Multipart
    @POST
    suspend fun addProducts(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Part("name") name: RequestBody,
        @Part("offerprice") offerprice: RequestBody,
        @Part("actualprice") actualprice: RequestBody,
        @Part("gymid") gymid: RequestBody,
        @Part pic: MultipartBody.Part
    ): UserRegisterModel
    @GET // Adjust this to match your backend endpoint
    suspend fun getmymembers(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Query("gymid") gymid: Int,
        @Query("page") page: Int

    ): MemberResponse

    @GET
    suspend fun getWorkoutsByMuscleGroup(        @Url url: String,
                                                 @Query("muscle_group") muscleGroup: String): GymGuideResponse

    @GET
    suspend fun getproductsgym(        @Url url: String,
                                       @Query("gymid") gymid: Int
    ): ProductResponse
    @GET
    suspend fun getCustData(        @Url url: String,
                                    @Header("x-api-key") apiKey: String,

                                    @Query("userid") userid: Long
    ): CustResponse
    @GET
    suspend fun getDashData(        @Url url: String,
                                    @Header("x-api-key") apiKey: String,

                                    @Query("gymid") gymid: Int
    ): DashDataModel


    @GET // Adjust this to match your backend endpoint
    suspend fun getTransacHist(
        @Url url: String,

        @Query("gymid") gymid: Int,
        @Query("page") page: Int,
@Query("memid") memberId:String,
        @Query("t_date") date:String

    ): GetMyTransacHist
    @GET // Adjust this to match your backend endpoint
    suspend fun getmytrainer(
        @Url url: String,

        @Query("gymid") gymid: Int): GetMyTrainersResponse

    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    fun loginAdmin(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Field("hashKey") hashIs: String,
        @Field("mobile") mob: Long,
        @Field("password") pass: String
    ): Call<AdminLoginModel>

    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    fun saveprofiledata(
        @Url url: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("age") age: Int,
        @Field("gender") gender: String,
        @Field("type") type: Int,
        @Field("height") height: Double,
        @Field("weight") weight: Double,
    ): Call<UserRegisterModel>
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun registerNewGym(
        @Url url: String,
        @Field("owner") owner: String,
        @Field("gymname") gymname: String,
        @Field("mob") mob: String,
        @Field("plantype") paidmonths: String ,
        @Field("location") location: String,
        @Field("email") email: String,
        ): UserRegisterModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun addmymember(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("gymid") gymid: Int,
        @Field("mob") mob: Long,
        @Field("paidmonths") paidmonths: Int?,
        @Field("paiddays") paiddays: Int?,
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
    @POST // Adjust this to match your backend endpoint
    suspend fun updatemymember(
        @Url url: String,
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
    @POST // Adjust this to match your backend endpoint
    suspend fun updatedietSchedule(
        @Url url: String,
        @Field("userid") userid: Long,
        @Field("diet_schedule") diet_schedule: String,
    ): UserRegisterModel


    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun updatemytrainer(
        @Url url: String,
        @Field("name") name: String,
        @Field("gymid") gymid: Int,
        @Field("mob") mob: Long,
        @Field("trainer") trainerId: Long,
        ): UserRegisterModel

    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun duePay(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Field("name") name: String,
        @Field("gymid") gymid: Int,
        @Field("userid") userid: Long,
        @Field("amount_paid") amount_paid: Int,
        @Field("due_amount") due_amount: Int,
        ): UserRegisterModel

    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun renewmymember(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Field("gymid") gymid: Int,
        @Field("paidmonths") paidmonths: Int?,
        @Field("paiddays") paiddays: Int?,
        @Field("date") date: String,
        @Field("userid") userid: Long,
        @Field("isRenew") isRenew: Int =1,
        @Field("amount_paid") amount_paid: Int,
        @Field("due_amount") due_amount: Int,

    ): UserRegisterModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun deletemymember(
        @Url url: String,
        @Field("gymid") gymid: Int,
        @Field("userid") userid: Long,
    ): UserRegisterModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun deletemytrainer(
        @Url url: String,
        @Field("gymid") gymid: Int,
        @Field("coachid") coachid: Long,
    ): UserRegisterModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun addmytrainer(
        @Url url: String,
        @Field("name") name: String,
        @Field("gymid") gymid: Int,
        @Field("mob") mob: Long,
        @Field("role") role: Int,

        ): UserRegisterModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun updateGymPassword(
        @Url url: String,
        @Field("gymid") gymid: Int,
        @Field("oldpass") oldpass: String,
        @Field("newpass") newpass: String,

        ): UserRegisterModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun markAttendance(
        @Url url: String,
        @Field("userid") userid: Int,
        @Field("gymid") gymid: Int,

        ): MarkAttendModel
    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun updateAdminMob(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Field("gymid") gymid: Int,
        @Field("admin_cont") admincontact: String,

        ): AdminContUpdateModel

    @FormUrlEncoded
    @POST // Adjust this to match your backend endpoint
    suspend fun updatebroadmsg(
        @Url url: String,
        @Header("x-api-key") apiKey: String,
        @Field("gymid") gymid: Int,
        @Field("pass") oldpass: String,
        @Field("broadmsg") newpass: String,

        ): UserRegisterModel
}


// Response model
