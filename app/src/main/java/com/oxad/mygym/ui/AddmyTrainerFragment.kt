package com.oxad.mygym.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.oxad.mygym.database.ApiRepository
import com.oxad.mygym.databinding.FragmentAddmyClientBinding
import com.oxad.mygym.databinding.FragmentAddmyTrainerBinding
import com.oxad.mygym.databinding.FragmentMyTrainerBinding
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.model.UserRegisterModel
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager
import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.utils.Utils
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [AddmyTrainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddmyTrainerFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddmyTrainerBinding
    private lateinit var apiManager: ApiManager
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddmyTrainerBinding.inflate(inflater, container, false)
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
    if(binding.nameInput.text.isNotEmpty() && binding.mobInput.text.isNotEmpty() ){
        lifecycleScope.launch {
            val result = ApiRepository().addmytrainer(binding.nameInput.text.toString(),mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.mobInput.text.toString().toLong())
            Log.e("chadka",""+ Gson().toJson(result))
            result.onSuccess { response ->
                if(response.success){
                    if(context !=null) {
                        mainViewModel.loadTrainerData(DataStoreManager(requireContext()))
                        Utils.showToast(requireContext(), "Trainer Added Successfully!")
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
        }    }else{
        if(context != null)
        {
            Utils.showToast(requireContext(), "Fill all the fields")
        }
    }
}

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddmyTrainerFragment().apply {

            }
    }




}