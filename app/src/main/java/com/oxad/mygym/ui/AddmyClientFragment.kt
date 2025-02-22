package com.oxad.mygym.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.oxad.mygym.R
import com.oxad.mygym.adapter.RenewalAdapter
import com.oxad.mygym.database.ApiRepository
import com.oxad.mygym.databinding.FragmentAddmyClientBinding
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.model.Member
import com.oxad.mygym.model.MyMemberListModel
import com.oxad.mygym.model.UserRegisterModel
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager
import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.utils.Utils
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    private var memberData:Member?  = null
    private var isRenew:Boolean?  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberData = arguments?.getParcelable("member_data",)
        isRenew = arguments?.getBoolean("is_renew",)


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
   private  lateinit var datePreSet:Calendar
    private fun initUi(){

        Log.d("AddMyClientFragment", "Received Member: ${memberData?.name}, Age: ${memberData?.mobile}")
        if(memberData!=null){
            binding.deleteButton.visibility = VISIBLE
            if(isRenew == true){
                binding.nameInput.isEnabled = false
                binding.renewLl.visibility = VISIBLE
                binding.editLl.visibility = GONE
                binding.updateButton.visibility = GONE
                binding.renewButton.visibility = VISIBLE
            }else{
                binding.renewLl.visibility = GONE
                binding.editLl.visibility = VISIBLE
                binding.updateButton.visibility = VISIBLE
                binding.renewButton.visibility = GONE

            }
            binding.submitButton.visibility = GONE
            binding.nameInput.text = memberData?.name?.toEditable()
            binding.mobInput.text =  Editable.Factory.getInstance().newEditable(memberData?.mobile.toString())
            binding.emailInput.text = memberData?.email?.toEditable()

            if(memberData?.renewal_date !=null) {
                datePreSet = convertToCalendar(memberData!!.renewal_date)?:Calendar.getInstance()
            }
        }else{
            binding.deleteButton.visibility = GONE
            binding.updateButton.visibility = GONE
            binding.submitButton.visibility = VISIBLE
            datePreSet = Calendar.getInstance()
        }
        val datePicker: DatePicker =  binding.datePicker


        // Set listener for when the user changes the date


        datePicker.init(datePreSet.get(Calendar.YEAR), datePreSet.get(Calendar.MONTH), datePreSet.get(Calendar.DAY_OF_MONTH)) { _, _, _, _ ->
            updateDateText(datePicker,  binding.textViewSelectedDate)
        }
        updateDateText(datePicker,  binding.textViewSelectedDate)
        setclicklistener()




    }
    fun convertToCalendar(dateString: String, format: String = "yyyy-MM-dd"): Calendar? {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())

        return try {
            val date = dateFormat.parse(dateString) // Convert string to Date
            Calendar.getInstance().apply { time = date!! } // Set Date to Calendar
        } catch (e: Exception) {
            null // Return null if parsing fails
        }
    }
    private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    var dateIs:String = ""
    private fun updateDateText(datePicker: DatePicker, textView: TextView) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
        val mysqlDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = mysqlDateFormat.format(selectedDate.time)
        dateIs = formattedDate.format(selectedDate.time)
        Log.e("checkhere","yes $dateIs")
    }
    private fun setclicklistener(){
        binding.submitButton.setOnClickListener(this)
        binding.updateButton.setOnClickListener(this)
        binding.renewButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)


    }
    override fun onClick(p0: View?) {

        when (p0) {
            binding.submitButton -> {
                if (binding.nameInput.text.isNotEmpty() && dateIs != "" && binding.emailInput.text.isNotEmpty() && binding.mobInput.text.isNotEmpty() && binding.paidmonthsInput.text.isNotEmpty()) {

                    dataAdd(false)
                } else {
                    if (context != null) {
                        Utils.showToast(requireContext(), "Fill all the fields")
                    }

                }

            }

            binding.updateButton -> {
                if (binding.nameInput.text.isNotEmpty()  && binding.emailInput.text.isNotEmpty() && binding.mobInput.text.isNotEmpty() ) {
                    dataAdd(true)
                } else {
                    if (context != null) {
                        Utils.showToast(requireContext(), "Fill all the fields")
                    }
                }
            }

            binding.renewButton -> {
            if ( dateIs != "" && binding.paidmonthsInput.text.isNotEmpty()) {
                dataAdd(true)
            } else {
                if (context != null) {
                    Utils.showToast(requireContext(), "Fill all the fields")
                }
            }
        }
            binding.deleteButton -> activity?.let{
                showCustomDialog(requireContext()) { deleteMember() }

            }

        }
    }
    private fun deleteMember(){
        lifecycleScope.launch {

            val result =     memberData?.userid?.let {
                ApiRepository().deletemymembers(
                    mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,
                    it
                )
            }
            Log.e("chadka",""+ Gson().toJson(result))
            result?.onSuccess { response ->
                if(response.success){

                    if(context !=null) {
                        mainViewModel.refreshMembers()

                        Utils.showToast(requireContext(), response.message)
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
private fun dataAdd(isUpdate:Boolean){
    Log.e("chadka",""+ dateIs)

        lifecycleScope.launch {

            val result = if(!isUpdate){
                ApiRepository().addmymembers(binding.nameInput.text.toString(),binding.emailInput.text.toString(),mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.mobInput.text.toString().toLong(),binding.paidmonthsInput.text.toString().toInt(),dateIs)
            }else{
                if(isRenew == true){
                    memberData?.userid?.let {
                        ApiRepository().renewmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.paidmonthsInput.text.toString().toInt(),dateIs,
                            it
                        )
                    }
                }else{
                    memberData?.userid?.let {
                        ApiRepository().updatemymembers(binding.nameInput.text.toString(),binding.emailInput.text.toString(),mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.mobInput.text.toString().toLong(),
                            it
                        )
                    }
                }


            }
            Log.e("chadka",""+ Gson().toJson(result))
            result?.onSuccess { response ->
               if(response.success){

                   if(context !=null) {
                       mainViewModel.refreshMembers()

                       Utils.showToast(requireContext(), response.message)
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