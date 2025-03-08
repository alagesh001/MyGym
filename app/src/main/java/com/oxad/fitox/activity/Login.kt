package com.oxad.fitox.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.oxad.fitox.R
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
        binding.blurView.setupWith(binding.root, RenderScriptBlur(this))
            .setBlurRadius(12f)
        dataStoreManager = DataStoreManager(application)

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
                        apiManager.loginAdmin(mob.toLong(), passs)
                        return@withContext
                    }
                }
            }



            }

           
            
            

    }

    private fun initUI() {
        setclicklistener()
        Glide.with(this@Login)
            .load(R.drawable.bggymml)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade(1500)) // 500ms fade-in// Or a URL: "https://yourimageurl.com"
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.bgViewLogin.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.bgViewLogin.background = placeholder
                }
            })
    }

    private fun setclicklistener() {
        binding.btnLogin.setOnClickListener(this)
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
            apiManager.loginAdmin( binding.etMobile.text.toString().toLong(),binding.etPassword.text.toString())
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
