package com.oxad.mygym.retrofit

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.oxad.mygym.model.AdminLoginModel
import com.oxad.mygym.utils.RsConstants.Companion.REGISTER_USER
import com.oxad.mygym.utils.RsConstants.Companion.SAVE_USER
import com.oxad.mygym.utils.SessionManager
import com.oxad.mygym.model.UserRegisterModel
import com.oxad.mygym.utils.RsConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager(context: Context,apiInterface: ApiInterface) {
private val contextA = context
    private val apiInterfaceIs = apiInterface
    private val sessionManager = SessionManager(contextA)
    fun loginAdmin( mob: Long, pass: String) {
        Log.w("checkresponss","call api registeruser }")

        RetrofitClient.instance.loginAdmin(mob,pass)
            .enqueue(object : Callback<AdminLoginModel> {

                override fun onResponse(call: Call<AdminLoginModel>, response: Response<AdminLoginModel>) {
                    Log.w("checkresponss","call api response ${Gson().toJson(response.body())}")

                    if(response.body()!=null) {
                        if (response.body()!!.success) {
                            //sessionManager.saveLogin(profileUrl, name, email)
                            apiInterfaceIs.onSuccess(response.body()!!, RsConstants.ADMIN_LOGIN)
                        } else {
                            apiInterfaceIs.onError(response.body()!!.message)
                        }
                    }

                }

                override fun onFailure(call: Call<AdminLoginModel>, t: Throwable) {
                    Log.e("checkresponss", "Error: ${t.message}")
                    Toast.makeText(contextA, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
     fun registerUser( profileUrl:String,name: String, email: String) {
         Log.w("checkresponss","call api registeruser }")

        RetrofitClient.instance.registerUser(name,email)
            .enqueue(object : Callback<UserRegisterModel> {

                override fun onResponse(call: Call<UserRegisterModel>, response: Response<UserRegisterModel>) {
                    Log.w("checkresponss","call api response ${Gson().toJson(response.body())}")

                    if(response.body()!=null) {
                        if (response.body()!!.success) {
                                sessionManager.saveLogin(profileUrl, name, email)
                            apiInterfaceIs.onSuccess(response.body()!!, REGISTER_USER)
                        } else {
                            apiInterfaceIs.onError(response.body()!!.message)
                        }
                    }

                }

                override fun onFailure(call: Call<UserRegisterModel>, t: Throwable) {
                    Log.e("checkresponss", "Error: ${t.message}")
                    Toast.makeText(contextA, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    fun saveUserData(name: String, email: String,age: Int, gender: String,weight: Double, height: Double,type: Int) {
        Log.e("checkresponss", "saveUserData: $age $gender $weight $height $type $name $email")

        RetrofitClient.instance.saveprofiledata(name,email,age,gender,type,height, weight)
            .enqueue(object : Callback<UserRegisterModel> {
                override fun onResponse(call: Call<UserRegisterModel>, response: Response<UserRegisterModel>) {
                    Log.e("checkresponss", "saveUserData: ${response.body()}")

                    if(response.body()!=null) {
                        if (response.body()!!.success) {
                            sessionManager.setUserName(name)
                            apiInterfaceIs.onSuccess(response.body()!!, SAVE_USER)
                        } else {
                            apiInterfaceIs.onError(response.body()!!.message)
                        }
                    }

                }

                override fun onFailure(call: Call<UserRegisterModel>, t: Throwable) {
                    Log.e("checkresponss", "Error: ${t.message}")
                    Toast.makeText(contextA, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
interface ApiInterface{
    fun onSuccess(response:Any,serviceCode:Int)
    fun onError(error:String)
}