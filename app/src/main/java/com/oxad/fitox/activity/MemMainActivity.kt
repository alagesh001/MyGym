package com.oxad.fitox.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.oxad.fitox.R
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.databinding.ActivityMainBinding
import com.oxad.fitox.model.MemberLoginModel
import com.oxad.fitox.viewmodel.MainViewModel
import com.oxad.fitox.viewmodel.MemberViewModel

class MemMainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val memberViewModel: MemberViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mem_main)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val repo = ApiRepository(this)
        memberViewModel.setApiRepo(repo)
      initUi()
    }


    private fun initUi(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_mem_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}