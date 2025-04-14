package com.oxad.fitox.database

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import com.oxad.fitox.model.GetMyTrainersResponse
import com.oxad.fitox.model.GetMyTransacHist
import com.oxad.fitox.model.GymGuideResponse
import com.oxad.fitox.model.MemberResponse
import com.oxad.fitox.model.ProductResponse
import com.oxad.fitox.model.TrainerModel
import com.oxad.fitox.model.UserRegisterModel
import com.oxad.fitox.retrofit.ApiHelper
import com.oxad.fitox.retrofit.ApiHelper.gtDataUrl
import com.oxad.fitox.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ApiRepository() {
        suspend fun getMembers(gymId: Int, page: Int,aKeyIs:String): Result<MemberResponse> {
            return safeApiCall {
                RetrofitClient.instance.getmymembers(ApiHelper.gDatamembUrl(),aKeyIs,gymId, page)
            }
        }
    suspend fun getWorkoutsByMuscleGroup(muscleGroup: String): Result<GymGuideResponse> {
        return safeApiCall { RetrofitClient.instance.getWorkoutsByMuscleGroup(ApiHelper.vcoachUrl(),muscleGroup) }

    }
    suspend fun getProducts(gymId: Int): Result<ProductResponse> {
        return safeApiCall { RetrofitClient.instance.getproductsgym(ApiHelper.getProductUrl(),gymId) }

    }
    suspend fun updatemymembers(name:String,email:String, gymid: Int,mob:Long,userid:Long,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updatemymember(ApiHelper.guTableUrl(),name,email,gymid,mob,userid,dob,height,weight, goal, trainerId,gender)        }

    }
    suspend fun updatemytrainer( name:String,mob:Long,gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updatemytrainer(ApiHelper.gutrainerUrl(),name,gymid,mob,trainerId)
        }


    }
    suspend fun updatedietSchedule( user_id:Long,datetime:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updatedietSchedule(ApiHelper.updatedietscheduleurl(),user_id,datetime)
        }
    }
    suspend fun duePay( name:String,gymid: Int,userid:Long,amount_paid:Int,due_amount:Int): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.duePay(ApiHelper.gudueUrl(),name,gymid,userid, amount_paid,due_amount)        }


    }
    suspend fun renewmymembers( gymid: Int,paidmonths:Int,date:String,userid:Long,amount_paid:Int,due_amount:Int): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.renewmymember(ApiHelper.guTableUrl(),gymid,paidmonths,date,userid, 1,amount_paid,due_amount)        }


    }
    suspend fun deletemymembers( gymid: Int,userid:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.deletemymember(ApiHelper.minusCustUrl(),gymid,userid)      }

    }
    suspend fun deletemytrainer( gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.deletemytrainer(ApiHelper.minusTrainerUrl(),gymid,trainerId)
        }

    }
    suspend fun registerNewGym(owner:String,gymname:String, mob:String,plantype:String,location:String,email:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.registerNewGym(ApiHelper.plusNewGymUrl(),
                owner,
                gymname,
                mob,
                plantype,
                location,
                email,
            )
        }


    }
    suspend fun addmymembers(name:String,email:String, gymid: Int,mob:Long,paidmonths:Int,date:String,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String,referredBy:String,amount_paid:Int,due_amount:Int): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.addmymember(ApiHelper.plusNewCUrl(),
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
    suspend fun addmytrainer(name:String, gymid: Int,mob:Long,role:Int): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.addmytrainer(ApiHelper.plusTrainerUrl(),name,gymid,mob,role)
        }


    }
    suspend fun addProduct(
        name: String,
        offerPrice: Int,
        actualPrice: Int,
        gymId: Int,
        imageFile: Uri,
        context: Context
    ): Result<UserRegisterModel> {

            val namePart = createPartFromString(name)
            val offerPricePart = createPartFromString(offerPrice.toString())
            val actualPricePart = createPartFromString(actualPrice.toString())
            val gymIdPart = createPartFromString(gymId.toString())
            val imagePart = prepareFilePart("pic", imageFile, context)

            return safeApiCall {
                RetrofitClient.instance.addProducts(
                    ApiHelper.addProductUrl(),
                    namePart,
                    offerPricePart,
                    actualPricePart,
                    gymIdPart,
                    imagePart
                )
            }
}
    suspend fun updateProduct(
        name: String,
        offerPrice: Int,
        actualPrice: Int,
        gymId: Int,
        imageFile: Uri?,
        pid: Int,
        context: Context
    ): Result<UserRegisterModel> {

        val namePart = createPartFromString(name)
        val offerPricePart = createPartFromString(offerPrice.toString())
        val actualPricePart = createPartFromString(actualPrice.toString())
        val gymIdPart = createPartFromString(gymId.toString())
        val imagePart = imageFile?.let { prepareFilePart("pic", it, context) }
        val pid =  createPartFromString(pid.toString())

        Log.d("MultipartData", "Name Part: ${namePart}")
        Log.d("MultipartData", "Offer Price Part: ${offerPricePart}")
        Log.d("MultipartData", "Actual Price Part: ${actualPricePart}")
        Log.d("MultipartData", "Gym ID Part: ${gymIdPart}")

        imagePart?.let {
            Log.d("MultipartData", "Image Part: ${it}")
        }
        return safeApiCall {
            RetrofitClient.instance.updateProducts(
                ApiHelper.updateProductUrl(),
                namePart,
                offerPricePart,
                actualPricePart,
                gymIdPart,
                pid,
                imagePart
            )
        }
    }

    suspend fun deleteProduct(
        gymId: Int,
        pid: Int,
    ): Result<UserRegisterModel> {
      //  Log.d("deldfacx", "gis $gymId pid $pid")

        return safeApiCall {
            RetrofitClient.instance.deleteProduct(
                ApiHelper.updateProductUrl(),
                gymId,
                pid,
            )
        }
    }
    fun createPartFromString(value: String): RequestBody {
            return value.toRequestBody("text/plain".toMediaTypeOrNull())
        }

        fun prepareFilePart(partName: String, uri: Uri, context: Context): MultipartBody.Part {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
        }
    suspend fun updategympass( gymid: Int,oldpass:String,newpass:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updateGymPassword(ApiHelper.uCPassUrl(),gymid,oldpass,newpass)
        }


    }
    suspend fun updatebroadmsg( gymid: Int,pass:String,broadmsg:String): Result<UserRegisterModel> {
        return safeApiCall {
            RetrofitClient.instance.updatebroadmsg(ApiHelper.updateBroadmsgUrl(),gymid,pass,broadmsg)
        }


    }
    suspend fun getTransacHist(gymId: Int, page: Int): Result<GetMyTransacHist> {
        return safeApiCall {
            RetrofitClient.instance.getTransacHist(ApiHelper.transCheckUrl(),gymId, page)
        }
    }
    suspend fun getTrainers(gymId: Int): Result<GetMyTrainersResponse> {
        return safeApiCall {
            RetrofitClient.instance.getmytrainer(gtDataUrl(),gymId)
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