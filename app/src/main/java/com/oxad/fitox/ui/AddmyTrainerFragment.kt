package com.oxad.fitox.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oxad.fitox.R
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.databinding.FragmentAddmyTrainerBinding
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.model.TrainerModel
import com.oxad.fitox.retrofit.ApiManager
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
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
    private var trainerData: TrainerModel?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trainerData = arguments?.getParcelable("trainer_data",)
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
    private var role = 1
    private fun initUi(){
      // default Trainer


        if(trainerData!=null){
            binding.nameInput.text = trainerData?.name?.toEditable()
            binding.mobInput.text = trainerData?.mob?.toEditable()
            binding.submitButton.visibility = GONE
            binding.updateButton.visibility = VISIBLE
            binding.deleteButton.visibility = VISIBLE

        }else{
            binding.submitButton.visibility = VISIBLE
            binding.updateButton.visibility = GONE
        }
        setclicklistener()
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    private fun setclicklistener(){
        binding.submitButton.setOnClickListener(this)
        binding.updateButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {

        when (p0) {
            binding.submitButton -> dataAdd()
            binding.updateButton -> dataAdd(true)
            binding.deleteButton -> activity?.let{
                showCustomDialog(requireContext()) { deleteTrainer() }

            }



        }
    }
    private fun deleteTrainer(){
        lifecycleScope.launch {

            val result =     trainerData?.coachid?.let {
                ApiRepository().deletemytrainer(
                    mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,
                    it
                )
            }
            //Log.e("chadka",""+ Gson().toJson(result))
            result?.onSuccess { response ->
                if(response.success){

                    if(context !=null) {
                        mainViewModel.refreshMembers()
                        mainViewModel.loadTrainerData(DataStoreManager(requireContext()))

                        Utils.showToast(requireContext(), response.message)
                        findNavController().popBackStack()

                    }
                    findNavController().popBackStack()
                }else{
                    if(context !=null) {
                        Utils.showToast(requireContext(), response.message)
                    }
                }
            }

            result?.onFailure { exception ->
                Utils.showToast(requireContext(), "Failed")
                exception.message ?: "Something went wrong!"
            }

        }
    }
    private fun showCustomDialog(context: Context, onConfirm: () -> Unit) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_custom)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirm)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        btnConfirm.setOnClickListener {
            onConfirm() // Perform delete action
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
private fun dataAdd(isUpdate:Boolean=false){
    val selectedId = binding.roleGroup.checkedRadioButtonId
    if (selectedId == R.id.radio_dietician) {
        role = 2
    }else
    {   role = 1

    }
    if(binding.nameInput.text.isNotEmpty() && binding.mobInput.text.isNotEmpty() ){
        lifecycleScope.launch {
            val result = if(isUpdate){
                trainerData?.coachid?.let {
                    ApiRepository().updatemytrainer(binding.nameInput.text.toString(),binding.mobInput.text.toString().toLong(),mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,
                        it
                    )
                }

            }else{
                 ApiRepository().addmytrainer(binding.nameInput.text.toString(),mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.mobInput.text.toString().toLong(),role)

            }

            //Log.e("chadka",""+ Gson().toJson(result))
            result?.onSuccess { response ->
                if(response.success){
                    if(context !=null) {
                        mainViewModel.loadTrainerData(DataStoreManager(requireContext()))
                        Utils.showToast(requireContext(), response.message)
                        findNavController().popBackStack()

                    }
                }else{
                    if(context !=null) {
                        Utils.showToast(requireContext(), response.message)
                    }
                }
            }

            result?.onFailure { exception ->
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