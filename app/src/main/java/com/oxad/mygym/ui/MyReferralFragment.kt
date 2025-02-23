package com.oxad.mygym.ui

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
import com.oxad.mygym.R
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.utils.Utils
import kotlinx.coroutines.launch

class MyReferralFragment : Fragment() {

    private lateinit var referralDataStore: DataStoreManager
    private lateinit var editTextReferral: EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_referral, container, false)

        referralDataStore = DataStoreManager(requireContext())
        editTextReferral = view.findViewById(R.id.editTextReferral)
        buttonSave = view.findViewById(R.id.buttonSave)

        // Load saved referral message
        viewLifecycleOwner.lifecycleScope.launch {
            referralDataStore.referralMessage.collect { savedMessage ->
                editTextReferral.text = savedMessage.toEditable()
                Log.e("jhajfadsf","Yes collect ")


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

        return view
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}
