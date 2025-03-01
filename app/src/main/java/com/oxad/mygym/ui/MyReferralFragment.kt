package com.oxad.mygym.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.oxad.mygym.R
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.databinding.FragmentMyReferralBinding
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyReferralFragment : Fragment() {

    private lateinit var referralDataStore: DataStoreManager
    private lateinit var editTextReferral: EditText
    private lateinit var buttonSave: Button
    private lateinit var binding: FragmentMyReferralBinding

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
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            referralDataStore.referralMessage.collect { savedMessage ->
                withContext(Dispatchers.Main) {
                    editTextReferral.text = savedMessage.toEditable()
                    Log.e("jhajfadsf", "Yes collect ")
                }


            }
        }

        // Save referral message when the button is clicked
        buttonSave.setOnClickListener {
            val newMessage = editTextReferral.text.toString()
            lifecycleScope.launch {
                referralDataStore.saveReferralMessage(newMessage)
                Utils.showToast(requireContext(),"Saved")
            }
        }

        return binding.root
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}
