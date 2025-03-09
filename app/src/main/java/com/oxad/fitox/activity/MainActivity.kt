package com.oxad.fitox.activity

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
        this.let {
            Glide.with(it)
                .asBitmap()
                .load(R.drawable.gym_pic)
                .centerCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val drawable = BitmapDrawable(resources, resource).apply {
                            // Set initial alpha to 0 for fade-in effect
                            alpha = 0
                        }

                        // Set the drawable as the background
                        binding.bgViewIv.background = drawable

                        // Animate the alpha for fade-in
                        val fadeInAnimator = ValueAnimator.ofInt(0, 255).apply {
                            duration = 400 // 4 seconds fade-in duration
                            addUpdateListener { animator ->
                                drawable.alpha = animator.animatedValue as Int
                                binding.bgViewIv.invalidate() // Redraw the view
                            }
                        }
                        fadeInAnimator.start()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        binding.bgViewIv.background = placeholder
                    }
                })
        }
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