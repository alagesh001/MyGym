package com.oxad.fitox.activity

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.widget.ImageView
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
        statusBarWhiteIcons(window)
        setContentView(binding.root)
        mainViewModel.memberDao = AppDatabase.getDatabase(application).memberDao()
        mainViewModel.workoutDao = AppDatabase.getDatabase(application).workoutDao()
        initUi()
    }
    @Suppress("DEPRECATION")
    fun statusBarWhiteIcons(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.statusBarColor = Color.TRANSPARENT

            // Use WindowInsetsController from decorView (modern way)
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                0, // Remove LIGHT_STATUS_BARS => white icons
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // No LIGHT_STATUS_BAR => white icons

            window.statusBarColor = Color.TRANSPARENT
        }
    }
    private fun initUi(){
        this.let {
            Glide.with(it)
                .load(R.drawable.aaa)
                .centerCrop()
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        binding.bgViewIv.apply {
                            scaleType = ImageView.ScaleType.CENTER_CROP // Ensures the image is center-cropped
                            setImageDrawable(resource)
                        }
                      /*  val drawable = BitmapDrawable(resources, resource).apply {
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
                        fadeInAnimator.start()*/
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        binding.bgViewIv.background = placeholder
                    }
                })
        }
        val gymList: ArrayList<LoginResult>? = intent.getParcelableArrayListExtra("gym_list")
        val a_keyis: String = intent.getStringExtra("keyCall_Is").toString()

        if (gymList != null) {
            mainViewModel.gymResult = gymList
            mainViewModel.a_keyIs = a_keyis

        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}