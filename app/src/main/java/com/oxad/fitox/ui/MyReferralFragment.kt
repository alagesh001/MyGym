package com.oxad.fitox.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.databinding.FragmentMyReferralBinding
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyReferralFragment : Fragment() {

    private lateinit var referralDataStore: DataStoreManager
    private lateinit var editTextReferral: EditText
    private lateinit var buttonSave: Button
    private lateinit var binding: FragmentMyReferralBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
      //  val start = System.currentTimeMillis()
        binding = FragmentMyReferralBinding.inflate(inflater, container, false)

        //Log.d("FragmentTiming", "Inflation time: ${System.currentTimeMillis() - start} ms")
       /* Glide.with(requireContext())
            .load(R.drawable.bg_app2)
            .transition(DrawableTransitionOptions.withCrossFade(2000)) // 500ms fade-in// Or a URL: "https://yourimageurl.com"
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.root.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.root.background = placeholder
                }
            })*/
        referralDataStore = DataStoreManager(requireContext())
        editTextReferral = binding.editTextReferral
        buttonSave = binding.buttonSave

        // Load saved referral message
     /*   viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            referralDataStore.referralMessage.collect { savedMessage ->
                withContext(Dispatchers.Main) {
                    editTextReferral.text = savedMessage.toEditable()
                 //   Log.e("jhajfadsf", "Yes collect ")
                }


            }
        }*/

        // Save referral message when the button is clicked
        buttonSave.setOnClickListener {
            val newMessage = editTextReferral.text.toString()

            // Create a container to hold the EditText with margin
            val container = FrameLayout(requireContext()).apply {
                setPadding(32) // Apply padding around the EditText
            }

            // Create the password input field
            val passwordInput = EditText(requireContext()).apply {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                hint = "Enter password"
            }

            // Add the password input to the container
            container.addView(passwordInput, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(32, 16, 32, 16) // Apply margins around the EditText
            })

            // Create the AlertDialog
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Password")
                .setMessage("Please enter your password to save the changes.")
                .setView(container) // Set the container as the view
                .setPositiveButton("Confirm") { dialog, _ ->
                    val pass = passwordInput.text.toString()

                    if (pass.isNotEmpty()) {
                        // Proceed with the API call
                        lifecycleScope.launch {
                            val result = ApiRepository().updatebroadmsg(
                                mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,
                                pass,
                                newMessage,mainViewModel.a_keyIs
                            )
                            result.onSuccess { response ->
                                if (response.success) {
                                   // referralDataStore.saveReferralMessage(newMessage)
                                    Utils.showToast(requireContext(), response.message)
                                } else {
                                    Utils.showToast(requireContext(), response.message)
                                }
                            }

                            result.onFailure { exception ->
                                Utils.showToast(requireContext(), exception.message ?: "Something went wrong!")
                            }
                        }
                    } else {
                        Utils.showToast(requireContext(), "Password cannot be empty")
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


        return binding.root
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}
