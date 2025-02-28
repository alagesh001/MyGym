package com.oxad.mygym.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.oxad.mygym.databinding.ActivityLoginBinding
import com.oxad.mygym.model.AdminLoginModel
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager
import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.utils.Utils
import eightbitlab.com.blurview.RenderScriptBlur

class Login : AppCompatActivity(),ApiInterface,OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiManager: ApiManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        apiManager = ApiManager(this, this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.blurView.setupWith(binding.root, RenderScriptBlur(this))
            .setBlurRadius(12f)

initUI()
    }

    private fun initUI() {
        setclicklistener()
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
        Utils.showToast(this,error)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin ->login()
        }
    }
    private fun login(){

        apiManager.loginAdmin( 8148602102,"ags0071")
        binding.btnLogin .isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnLogin .isEnabled = true
        }, 3000)
        // Simulate network delay

       /* if(binding.etMobile.text.toString() != "" && binding.etPassword.text.toString() !="") {
            apiManager.loginAdmin( 8148602102,"ags0071")
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin .isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                binding.progressBar.visibility = View.GONE
                binding.btnLogin .isEnabled = true
            }, 3000)


        }else{
            Utils.showToast(this,"Fill all fields.")
        }*/
    }
    private fun registerSuccess(response: Any){
        val adminLoginModel =  response as AdminLoginModel


        if(adminLoginModel.success) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putParcelableArrayListExtra("gym_list",adminLoginModel.result)
            startActivity(intent)
            finish()

        }


    }
}
