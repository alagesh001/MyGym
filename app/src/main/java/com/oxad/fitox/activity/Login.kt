package com.oxad.fitox.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.oxad.fitox.R
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.databinding.ActivityLoginBinding
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.model.AdminLoginModel
import com.oxad.fitox.retrofit.ApiInterface
import com.oxad.fitox.retrofit.ApiManager
import com.oxad.fitox.utils.RsConstants
import com.oxad.fitox.utils.Utils
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.Util
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Login : AppCompatActivity(),ApiInterface,OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiManager: ApiManager
    private lateinit var dataStoreManager: DataStoreManager
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        apiManager = ApiManager(this, this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        backpressInit()
        binding.blurView.setupWith(binding.root, RenderScriptBlur(this))
            .setBlurRadius(12f)
        dataStoreManager = DataStoreManager(application)
       mHash =  getAppSignature()
        lifecycleScope.launch(Dispatchers.IO) {
            Log.e("dafasfd","Check login ${dataStoreManager.isLoggedInFlow.first()}")
            if(!dataStoreManager.isLoggedInFlow.first()){

                withContext(Dispatchers.Main) {
                    binding.loginLay.visibility = VISIBLE
                    binding.btnLogin.isEnabled = true
                    initUI()
                }
            }else{
                val mob = dataStoreManager.mobileNumber.first()
                val passs = dataStoreManager.password.first()
                //  Log.d("UserSession", "Mobile Number: $mobile")
                if(mob !=null && passs !=null) {
                    binding.loginLay.visibility = GONE

                    withContext(Dispatchers.Main) {
                        binding.btnLogin.isEnabled = false
                        isLoggedIn = true
                        apiManager.loginAdmin(mob.toLong(), passs,mHash)
                        return@withContext
                    }
                }
            }



            }

           
            
            

    }
    private fun backpressInit(){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
              if(isRegisterLay){
                  registerLay(false)
              }else{
                  finish()
              }
            }
        })
    }
    private var mHash: String = ""

    private fun getAppSignature(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo!!.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }
            val md = MessageDigest.getInstance("SHA")
            md.update(signatures?.get(0)!!.toByteArray())

            Base64.encodeToString(md.digest(), Base64.NO_WRAP).also {
              //  mHash = it
             //    Log.e("haskesa", it)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun initUI() {
        setclicklistener()
        Glide.with(this@Login)
            .load(R.drawable.abcd)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(1500)) // 500ms fade-in// Or a URL: "https://yourimageurl.com"
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.bgViewLogin.apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP // Ensures the image is center-cropped
                        setImageDrawable(resource)
                    }                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.bgViewLogin.background = placeholder
                }
            })
    }

    private fun setclicklistener() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

    }

    override fun onSuccess(response: Any, serviceCode: Int) {
        when(serviceCode){
            RsConstants.ADMIN_LOGIN -> registerSuccess(response)
        }
    }

    override fun onError(error: String) {

        binding.loginLay.visibility = VISIBLE
        Utils.showToast(this,error)
        binding.btnLogin .isEnabled = true
        isLoggedIn = false
        initUI()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin ->login()
            binding.btnRegister -> registerLay(true)
            binding.btnSubmit -> newRegisterSubmit()
                binding.btnCancel -> registerLay(false)
        }
    }
    private var isRegisterLay =false
    private fun registerLay(value:Boolean){
        if(value) {
            isRegisterLay = true
            binding.registerLay.visibility = VISIBLE
            binding.loginLay.visibility = GONE
        }else{
            isRegisterLay = false
            binding.registerLay.visibility = GONE
            binding.loginLay.visibility = VISIBLE
        }


    }

    private fun  newRegisterSubmit(){
        val owner = binding.etOwner.text.toString().trim()
        val gymName = binding.etGymName.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val mobile = binding.etMobileRegister.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val months = binding.etMonths.text.toString().trim()

        if (owner.isNotEmpty() && gymName.isNotEmpty() && location.isNotEmpty()
            && mobile.isNotEmpty() && email.contains("@") && months.isNotEmpty()) {
            lifecycleScope.launch {
                val result =  ApiRepository().registerNewGym(owner,gymName,mobile,months,location,email
                )
                result.onSuccess { response ->
                    if(response.success){
                        Utils.showToast(this@Login,"Registration Successful!")
                        registerLay(false)
                    }else{
                        Utils.showToast(this@Login,response.message)
                    }
                }
                result.onFailure { exception ->
                    Utils.showToast(this@Login, "Failed")
                    exception.message ?: "Something went wrong!"
                    Log.e("mafdsa","${exception.message}")
                }
            }
        } else {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
        }

    }
    private fun login(){
    /*    apiManager.loginAdmin( 8148602102,"ags0071")
        binding.btnLogin .isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnLogin .isEnabled = true
        }, 3000)
        // Simulate network delay*/

        if(binding.etMobile.text.toString() != "" && binding.etPassword.text.toString() !="") {
            apiManager.loginAdmin( binding.etMobile.text.toString().toLong(),binding.etPassword.text.toString(),mHash)
            binding.btnLogin .isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnLogin .isEnabled = true
            }, 3000)


        }else{
            Utils.showToast(this,"Fill all fields.")
        }
    }
    private fun registerSuccess(response: Any){
        val adminLoginModel =  response as AdminLoginModel

        //Log.e("adfadf",""+ Gson().toJson(adminLoginModel))

        if(adminLoginModel.success) {
if(!isLoggedIn) {
    loginUser(binding.etMobile.text.toString(), binding.etPassword.text.toString())
}
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putParcelableArrayListExtra("gym_list",adminLoginModel.result)
            intent.putExtra("keyCall_Is",adminLoginModel.a_key)
            startActivity(intent)
            finish()

        }else{
                binding.loginLay.visibility = VISIBLE
            initUI()

        }


    }
   private fun loginUser(mobile: String, password: String) {
        lifecycleScope.launch {
            dataStoreManager.saveUserCredentials(mobile, password) // Securely store credentials
        }
    }
}
