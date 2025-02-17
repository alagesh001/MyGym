package com.oxad.mygym.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
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
        if(binding.etMobile.text.toString() != "" && binding.etPassword.text.toString() !="") {

            apiManager.loginAdmin( binding.etMobile.text.toString().toLong(),binding.etPassword.text.toString())
        }else{
            Utils.showToast(this,"Fill all fields.")
        }
    }
    private fun registerSuccess(response: Any){
        val adminLoginModel =  response as AdminLoginModel


        if(adminLoginModel.success) {
            Utils.showToast(this,"Success")
            val intent = Intent(this, MainActivity::class.java)
            intent.putParcelableArrayListExtra("gym_list",adminLoginModel.result)
            startActivity(intent)
            finish()

        }


    }
}
