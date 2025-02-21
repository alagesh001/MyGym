package com.oxad.mygym.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.oxad.mygym.R
import com.oxad.mygym.adapter.RenewalAdapter
import com.oxad.mygym.database.ApiRepository
import com.oxad.mygym.databinding.FragmentAddmyClientBinding
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.model.MyMemberListModel
import com.oxad.mygym.model.UserRegisterModel
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager
import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.utils.Utils
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddmyClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddmyClientFragment : Fragment(),OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddmyClientBinding
    private lateinit var apiManager: ApiManager
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddmyClientBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }
    private fun initUi(){
        setclicklistener()
    }
    private fun setclicklistener(){
        binding.submitButton.setOnClickListener(this)


    }
    override fun onClick(p0: View?) {

        when (p0) {
            binding.submitButton -> dataAdd()



        }
    }
private fun dataAdd(){
    if(binding.nameInput.text.isNotEmpty() && binding.emailInput.text.isNotEmpty() && binding.mobInput.text.isNotEmpty() && binding.paidmonthsInput.text.isNotEmpty()){
        lifecycleScope.launch {
            val result = ApiRepository().addmymembers(binding.nameInput.text.toString(),binding.emailInput.text.toString(),mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.mobInput.text.toString().toLong(),binding.paidmonthsInput.text.toString().toInt())
            Log.e("chadka",""+ Gson().toJson(result))
            result.onSuccess { response ->
               if(response.success){
                   if(context !=null) {
                       mainViewModel.refreshMembers()

                       Utils.showToast(requireContext(), "Added Successfully!")
                   }
               }else{
                   if(context !=null) {
                       Utils.showToast(requireContext(), response.message)
                   }
               }
            }

            result.onFailure { exception ->
                exception.message ?: "Something went wrong!"
            }
        }
    }else{
        if(context != null)
        {        Utils.showToast(requireContext(),"Fill all the fields")}
    }
}

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddmyClientFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}