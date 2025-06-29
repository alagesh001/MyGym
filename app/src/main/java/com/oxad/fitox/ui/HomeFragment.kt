package com.oxad.fitox.ui

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.oxad.fitox.R
import com.oxad.fitox.databinding.FragmentHomeBinding
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.model.TrainerModel
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(),OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private var isbgApplied = false
private lateinit var navController:NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initUi()
        return binding.root    }
    private fun initUi(){
if(mainViewModel.gymResult[mainViewModel.currentGymSelected].is_active == 3){
    binding.myreferRl.visibility = VISIBLE
    binding.productAdd.visibility = VISIBLE
    binding.adminTv.text = "Admins/Dietician"
}else{
    binding.myreferRl.visibility = GONE
    binding.productAdd.visibility = GONE
    binding.adminTv.text = "Admins"

}
     /*   context?.let {
           Glide.with(it)
               .asBitmap()
                .load(R.drawable.gym_pic)
                .centerCrop()
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val drawable = BitmapDrawable(context?.resources, resource).apply {
                            // Set initial alpha to 0 for fade-in effect
                            alpha = 0
                        }

                        // Set the drawable as the background
                        binding.bgViewIv.background = drawable

                        // Animate the alpha for fade-in
                        val fadeInAnimator = ValueAnimator.ofInt(0, 255).apply {
                            duration = 100 // 4 seconds fade-in duration
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
        }*/
        activity?.lifecycleScope?.launch {
        //  Log.e("storetraninda",""+Gson().toJson(getCoachesList()))

        }
        binding.title.text = getString(R.string.welcometitle,mainViewModel.gymResult[mainViewModel.currentGymSelected].GymName,mainViewModel.gymResult[mainViewModel.currentGymSelected].Location)

        setclicklistener()
        // Set new gradient colors

    }
    suspend fun getCoachesList(): List<TrainerModel> {
        return DataStoreManager(requireContext()).getCoaches().first()
    }
    private fun setclicklistener(){
        binding.mytrainerRl.setOnClickListener(this)
        binding.searchfilterRl.setOnClickListener(this)
        binding.myreferRl.setOnClickListener(this)
        binding.virtualCoach.setOnClickListener(this)
        binding.myclientsRl.setOnClickListener(this)
        binding.clientaddIv.setOnClickListener(this)
        binding.traineraddIv.setOnClickListener(this)
        binding.settingsIv.setOnClickListener(this)
        binding.productAdd.setOnClickListener(this)
     //   binding.transacRl.setOnClickListener(this)
        binding.inactiveRl.setOnClickListener(this)

        binding.transacRl.setOnClickListener(this)
        binding.dashRl.setOnClickListener(this)



    }
    override fun onClick(p0: View?) {
        var code = 1
        when (p0) {
            binding.myclientsRl -> code = 1
            binding.mytrainerRl -> code = 2
            binding.searchfilterRl -> code = 3
            binding.myreferRl -> code = 4
            binding.virtualCoach -> {
                code = 5
                val startTime = System.currentTimeMillis()
                navController.navigate(R.id.action_homeFragment_to_vCoachFrag,null, NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build())
                val endTime = System.currentTimeMillis()
           //     Log.d("FragmentTiming", "Coach Navigation took: ${endTime - startTime} ms")
            }
            binding.clientaddIv -> code =6
            binding.traineraddIv -> code = 7
            binding.settingsIv -> code =8
            binding.productAdd -> code =9
          //  binding.transacRl -> code =10
            binding.inactiveRl -> code = 11
            binding.transacRl -> code =12
            binding.dashRl -> code =13
        }
        openFragment(code)
    }
    private fun showShareDialogUpgrade(gymId:String,mobIs:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Upgrade")
            .setMessage("Kindly contact admin to upgrade.")
            .setCancelable(false)
            .setPositiveButton("Contact") { dialog, _ ->
                shareInWhatsappUpgrade(gymId,mobIs)
            }
            .setNegativeButton("Cancel")  {dialog, _ ->
                dialog.dismiss()
                //findNavController().popBackStack()
            }
            .show()
    }
    private fun shareInWhatsappUpgrade(gymId:String,mobIs:String){
        val currentDateTime = LocalDateTime.now()

// Format the date and time
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val confirmationMessage = "Hi Fitox Team, Gymid - $gymId, I would like to upgrade my Fitox account."
        sendWhatsAppMessageUpgrade(confirmationMessage, "91$mobIs")
    }


    private fun sendWhatsAppMessageUpgrade(message: String, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = ("https://wa.me/$phoneNumber?text=" + Uri.encode(message)).toUri()
            startActivity(intent)
        } catch (e: Exception) {
            activity?.let { Toast.makeText(requireContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show()}
        }
       // findNavController().popBackStack()

    }
    private fun openFragment(code:Int){

        when(code)
        {
            1 ->  {
              //  val startTime = System.currentTimeMillis()
                val bundle = Bundle().apply {
                    putInt("type", 0)         // Example of passing an integer
                }

                navController.navigate(
                    R.id.action_homeFragment_to_myclientfragment,
                    bundle,
                    NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .build()
                )
              //  val endTime = System.currentTimeMillis()
               // Log.d("FragmentTiming", "Client took: ${endTime - startTime} ms")

            }
            2 ->navController.navigate(R.id.action_homeFragment_to_myTrainerFragment)
            3 -> navController.navigate(R.id.action_homeFragment_to_searchfilfrag)
            4 -> {
              //  val startTime = System.currentTimeMillis()
                navController.navigate(R.id.action_homeFragment_to_myreffrag,null, NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build())
              //  val endTime = System.currentTimeMillis()
             //   Log.d("FragmentTiming", "Navigation took: ${endTime - startTime} ms")
            }
            5 -> {
              /*    val startTime = System.currentTimeMillis()
                navController.navigate(R.id.action_homeFragment_to_vCoachFrag,null, NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .build())
                  val endTime = System.currentTimeMillis()
                   Log.d("FragmentTiming", "Coach Navigation took: ${endTime - startTime} ms")*/
            }
            6 -> navController.navigate(R.id.action_homeFragment_to_addmyclientfragment)
            7 -> navController.navigate(R.id.action_homeFragment_to_addmytrainerfragment)
            8 -> {


                val startTime = System.currentTimeMillis()

             /* activity?.supportFragmentManager?.beginTransaction()
                  ?.replace(R.id.gymprofilefragment, GymprofileFragment()) // Replace existing fragment
                    ?.addToBackStack(null) // Adds to back stack so the user can go back
                    ?.commit()*/
                navController.navigate(
                    R.id.action_homeFragment_to_gymprofilefragment, null, NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .build()
                )
                val endTime = System.currentTimeMillis()
              //  Log.d("FragmentTiming", "Coach Navigation took: ${endTime - startTime} ms")
            }
            9 -> navController.navigate(R.id.action_homeFragment_to_myProductFragment)
            10 -> navController.navigate(R.id.action_homeFragment_to_myTransacFrag)
            11 -> {
                if(mainViewModel.gymResult[mainViewModel.currentGymSelected].is_active == 1) {
                    activity?.let { Utils.showToast(requireContext(),"Kindly Upgrade to see Inactive Clients.")

                        showShareDialogUpgrade(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId.toString(),"8595509389")
                    }
                        return
                }
                    val bundle = Bundle().apply {
                    putInt("type", 1)         // Example of passing an integer
                }

                navController.navigate(
                    R.id.action_homeFragment_to_myclientfragment,
                    bundle,
                    NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .build()
                )
            }
            12 ->{
                    Bundle().apply {
                        putString("type_is", "transac")
                    }.let {
            findNavController().navigate(R.id.action_homeFragment_to_filfrag,it)
        }
            }
            13 ->{
                Bundle().apply {
                    putString("type_is", "dashboard")
                }.let {
                    findNavController().navigate(R.id.action_homeFragment_to_filfrag,it)
                }
            }



        }

    }

}