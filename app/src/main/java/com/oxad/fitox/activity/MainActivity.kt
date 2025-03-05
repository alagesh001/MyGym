package com.oxad.fitox.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.oxad.fitox.R
import com.oxad.fitox.database.AppDatabase
import com.oxad.fitox.databinding.ActivityMainBinding
import com.oxad.fitox.model.LoginResult
import com.oxad.fitox.viewmodel.MainViewModel

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
        mainViewModel.workoutDao = AppDatabase.getDatabase(application).workoutDao()

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