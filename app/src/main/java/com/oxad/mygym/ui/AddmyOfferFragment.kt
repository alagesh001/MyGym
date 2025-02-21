package com.oxad.mygym.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.oxad.mygym.databinding.FragmentAddmyClientBinding
import com.oxad.mygym.databinding.FragmentAddmyProductBinding
import com.oxad.mygym.databinding.FragmentAddmyTrainerBinding
import com.oxad.mygym.databinding.FragmentMyTrainerBinding
import com.oxad.mygym.model.UserRegisterModel
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager
import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.utils.Utils
import com.oxad.mygym.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AddmyOfferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddmyOfferFragment : Fragment(), ApiInterface, View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddmyProductBinding
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
        apiManager = ApiManager(requireContext(), this)
        binding = FragmentAddmyProductBinding.inflate(inflater, container, false)
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
      apiManager.addmymembers(binding.nameInput.text.toString(),binding.emailInput.text.toString(),mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.mobInput.text.toString().toLong(),binding.paidmonthsInput.text.toString().toInt())
    }else{
        if(context != null)
        {
            Utils.showToast(requireContext(), "Fill all the fields")
        }
    }
}

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddmyOfferFragment().apply {

            }
    }

    override fun onSuccess(response: Any, serviceCode: Int) {
        when(serviceCode){
            RsConstants.ADD_MY_MEMBERS -> addmymemberResponse(response)
        }
    }

    override fun onError(error: String) {
    }

    private fun addmymemberResponse(response: Any) {
        val userRegisterModel = response as UserRegisterModel


        if (userRegisterModel.success) {
            if (context != null) {
                Utils.showToast(requireContext(), "Added successfully")
            }


        }
    }


}