package com.oxad.mygym.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.oxad.mygym.R
import com.oxad.mygym.database.AppDatabase
import com.oxad.mygym.databinding.ActivityMainBinding
import com.oxad.mygym.model.LoginResult
import com.oxad.mygym.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.memberDao = AppDatabase.getDatabase(application).memberDao()


        initUi()
    }

    private fun initUi(){
        val gymList: ArrayList<LoginResult>? = intent.getParcelableArrayListExtra("gym_list")
        if (gymList != null) {
            mainViewModel.gymResult = gymList
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}