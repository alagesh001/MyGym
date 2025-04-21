package com.oxad.fitox.database

import android.app.Activity
import android.app.ProgressDialog
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ApiRepository(contextA: Context) {
    var progressDialog: ProgressDialog? = null
    var contextB: Context  = contextA

    suspend fun showLoading() {
        Log.e("initAVV",""+contextB)
        withContext(Dispatchers.Main) {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(contextB)
                progressDialog?.setMessage("Please wait...")
                progressDialog?.setCancelable(false)
            }
            progressDialog?.show()
        }


    }

    fun hideLoading() {

        progressDialog?.dismiss()
    }
        suspend fun getMembers(gymId: Int, page: Int,aKeyIs:String): Result<MemberResponse> {
         ///  showLoading();
            val result =
             safeApiCall {
                RetrofitClient.instance.getmymembers(ApiHelper.gDatamembUrl(),aKeyIs,gymId, page)
            }
          //  hideLoading()
            return result
        }
    suspend fun getWorkoutsByMuscleGroup(muscleGroup: String): Result<GymGuideResponse> {
       // showLoading();
        val result =
             safeApiCall { RetrofitClient.instance.getWorkoutsByMuscleGroup(ApiHelper.vcoachUrl(),muscleGroup) }

      //  hideLoading()
        return result

    }
    suspend fun getProducts(gymId: Int): Result<ProductResponse> {
      //  showLoading();
        val result =
             safeApiCall { RetrofitClient.instance.getproductsgym(ApiHelper.getProductUrl(),gymId) }

       // hideLoading()
        return result

    }
    suspend fun updatemymembers(name:String,email:String, gymid: Int,mob:Long,userid:Long,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.updatemymember(ApiHelper.guTableUrl(),name,email,gymid,mob,userid,dob,height,weight, goal, trainerId,gender)        }
        hideLoading()
        return result


    }
    suspend fun updatemytrainer( name:String,mob:Long,gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.updatemytrainer(ApiHelper.gutrainerUrl(),name,gymid,mob,trainerId)
            }
            hideLoading()
        return result




    }
    suspend fun updatedietSchedule( user_id:Long,datetime:String): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.updatedietSchedule(ApiHelper.updatedietscheduleurl(),user_id,datetime)
            }
            hideLoading()
        return result


    }
    suspend fun duePay( name:String,gymid: Int,userid:Long,amount_paid:Int,due_amount:Int,aKeyIs:String): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.duePay(ApiHelper.gudueUrl(),aKeyIs,name,gymid,userid, amount_paid,due_amount)        }
            hideLoading()
        return result




    }
    suspend fun renewmymembers( gymid: Int,paidmonths:Int,date:String,userid:Long,amount_paid:Int,due_amount:Int,aKeyIs:String): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.renewmymember(ApiHelper.guTableUrl(),aKeyIs,gymid,paidmonths,date,userid, 1,amount_paid,due_amount)        }
            hideLoading()
        return result




    }
    suspend fun deletemymembers( gymid: Int,userid:Long): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.deletemymember(ApiHelper.minusCustUrl(),gymid,userid)      }
            hideLoading()
        return result



    }
    suspend fun deletemytrainer( gymid: Int,trainerId:Long): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.deletemytrainer(ApiHelper.minusTrainerUrl(),gymid,trainerId)
            }
            hideLoading()
        return result



    }
    suspend fun registerNewGym(owner:String,gymname:String, mob:String,plantype:String,location:String,email:String): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.registerNewGym(ApiHelper.plusNewGymUrl(),
                    owner,
                    gymname,
                    mob,
                    plantype,
                    location,
                    email,
                )
            }
            hideLoading()
        return result




    }
    suspend fun addmymembers(name:String,email:String, gymid: Int,mob:Long,paidmonths:Int,date:String,dob:String,height:Float,weight:Float,goal:Int,trainerId:Long,gender:String,referredBy:String,amount_paid:Int,due_amount:Int,aKeyIs:String): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.addmymember(ApiHelper.plusNewCUrl(),aKeyIs,
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

            hideLoading()
        return result



    }
    suspend fun addmytrainer(name:String, gymid: Int,mob:Long,role:Int): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.addmytrainer(ApiHelper.plusTrainerUrl(),name,gymid,mob,role)
            }
            hideLoading()
        return result




    }
    suspend fun addProduct(
        aKeyIs:String,name: String,
        offerPrice: Int,
        actualPrice: Int,
        gymId: Int,
        imageFile: Uri,
        context: Context
    ): Result<UserRegisterModel> {
        showLoading();
            val namePart = createPartFromString(name)
            val offerPricePart = createPartFromString(offerPrice.toString())
            val actualPricePart = createPartFromString(actualPrice.toString())
            val gymIdPart = createPartFromString(gymId.toString())
            val imagePart = prepareFilePart("pic", imageFile, context)

        val result = safeApiCall {
                RetrofitClient.instance.addProducts(
                    ApiHelper.addProductUrl(),aKeyIs,
                    namePart,
                    offerPricePart,
                    actualPricePart,
                    gymIdPart,
                    imagePart
                )
            }
        hideLoading()
        return result
}
    suspend fun updateProduct(
        aKeyIs:String, name: String,
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

      //  Log.d("MultipartData", "Name Part: ${namePart}")
     //   Log.d("MultipartData", "Offer Price Part: ${offerPricePart}")
       // Log.d("MultipartData", "Actual Price Part: ${actualPricePart}")
      //  Log.d("MultipartData", "Gym ID Part: ${gymIdPart}")

        imagePart?.let {
           // Log.d("MultipartData", "Image Part: ${it}")
        }
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.updateProducts(
                    ApiHelper.updateProductUrl(),aKeyIs,
                    namePart,
                    offerPricePart,
                    actualPricePart,
                    gymIdPart,
                    pid,
                    imagePart
                )
            }
            hideLoading()
        return result


    }

    suspend fun deleteProduct( aKeyIs:String,
        gymId: Int,
        pid: Int,
    ): Result<UserRegisterModel> {
      //  Log.d("deldfacx", "gis $gymId pid $pid")
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.deleteProduct(
                    ApiHelper.updateProductUrl(),aKeyIs,
                    gymId,
                    pid,
                )
            }
            hideLoading()
        return result


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
        showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.updateGymPassword(ApiHelper.uCPassUrl(),gymid,oldpass,newpass)
            }
            hideLoading()
        return result




    }
    suspend fun updatebroadmsg( gymid: Int,pass:String,broadmsg:String,aKeyIs:String): Result<UserRegisterModel> {
        showLoading();
        val result =
            safeApiCall {

                RetrofitClient.instance.updatebroadmsg(ApiHelper.updateBroadmsgUrl(),aKeyIs,gymid,pass,broadmsg)
            }
            hideLoading()
        return result




    }
    suspend fun getTransacHist(gymId: Int, page: Int): Result<GetMyTransacHist> {
        //showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.getTransacHist(ApiHelper.transCheckUrl(),gymId, page)
            }
        //    hideLoading()
        return result


    }
    suspend fun getTrainers(gymId: Int): Result<GetMyTrainersResponse> {
       // showLoading();
        val result =
            safeApiCall {
                RetrofitClient.instance.getmytrainer(gtDataUrl(),gymId)
            }
          //  hideLoading()
        return result


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