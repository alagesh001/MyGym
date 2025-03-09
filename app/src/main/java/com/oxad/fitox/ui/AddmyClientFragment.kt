package com.oxad.fitox.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oxad.fitox.R
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.databinding.FragmentAddmyClientBinding
import com.oxad.fitox.model.Member
import com.oxad.fitox.model.TrainerModel
import com.oxad.fitox.retrofit.ApiManager
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
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
        memberData = arguments?.getParcelable("member_data")
        isRenew = arguments?.getBoolean("is_renew")


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

    private val handler = Handler(Looper.getMainLooper())
        private fun validateReferralId(members: List<Member>) {
            binding.referInput.addTextChangedListener(object : TextWatcher {
                private var lastInput: String? = null

                override fun afterTextChanged(s: Editable?) {
                    val referralId = s.toString().trim()
                    if (referralId.length == 10) {
                        if (lastInput != referralId) {
                            lastInput = referralId
                            handler.removeCallbacksAndMessages(null) // Debounce previous calls
                            handler.postDelayed({
                                checkReferralIdInList(members, referralId)
                            }, 500) // Delay validation for smoother UX
                        }
                    }else{
                        binding.referInput.setTextColor(Color.RED)

                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    private fun checkReferralIdInList(members: List<Member>, referralId: String) {
        val isValid = members.any { it.referral_id == referralId }
//Log.e("chjjada","memberss "+Gson().toJson(members))
       // Log.e("chjjada","referral_id "+referralId)

        if (isValid) {
            binding.referInput.setTextColor(Color.GREEN)
        } else {
            binding.referInput.setTextColor(Color.RED)
        }
    }
   private  lateinit var datePreSet:Calendar

    private   var dobPreSet:Calendar ? = null
   private var trainerData:List<TrainerModel> ? =null
    private fun initUi(){
        binding.totalAmountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateDueAmount()
            }

            override fun afterTextChanged(s: Editable) {
                // Not needed
            }
        })
        binding.amountPaidInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateDueAmount()
            }

            override fun afterTextChanged(s: Editable) {
                // Not needed
            }
        })
        val genderarray= resources.getStringArray(R.array.gender_types)
        val usertypearray= resources.getStringArray(R.array.user_types)

        activity?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                mainViewModel.trainerList.collect { trainers ->
                    trainerData = trainers
                    val adapter = ArrayAdapter(
                        requireContext(),
                       R.layout.spinner_item,
                        trainers.map { it.name })
                    adapter.setDropDownViewResource(R.layout.spinner_item)

                    binding.spinnerTypeTrainer.adapter = adapter

                }
            }

            // Create an ArrayAdapter with the custom layout
            val adapter2 = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,  // Custom layout for Spinner items
                genderarray
            )
            adapter2.setDropDownViewResource(R.layout.spinner_item)

            binding.spinnerTypeGender.adapter = adapter2

            val adapter3 = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,  // Custom layout for Spinner items
                usertypearray
            )
            adapter3.setDropDownViewResource(R.layout.spinner_item)

            binding.spinnerType.adapter = adapter3
        }

        binding.spinnerTypeGender.selectedItem.toString()
       // Log.d("AddMyClientFragment", "Received Member: ${memberData?.name}, Age: ${memberData?.mobile}")
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
                binding.referInput.isEnabled = false
                binding.mobInput.text =  Editable.Factory.getInstance().newEditable(memberData?.mobile.toString())
                binding.emailInput.text = memberData?.email?.toEditable()
                binding.heightInput.text = Editable.Factory.getInstance().newEditable(memberData?.height.toString())
                binding.weightInput.text = Editable.Factory.getInstance().newEditable(memberData?.weight?.toString())

                val selectionGoal = (memberData?.goal)?.minus(1)
              //  Log.e("adsfadf","pos $memberData?.goal")
                try {
                    binding.spinnerType.setSelection(selectionGoal?:0)

                }catch (e:Exception){
                    binding.spinnerType.setSelection(0)

                }

                val position = trainerData?.indexOfFirst { it.coachid == memberData?.coachid }
                if (position != null && position >= 0) {
                    try {
                        binding.spinnerTypeTrainer.setSelection(position)

                    }catch (e:Exception){
                        binding.spinnerTypeTrainer.setSelection(0)

                    }

                } else {
                    binding.spinnerTypeTrainer.setSelection(0)
                }




                binding.spinnerTypeGender.setSelection(genderarray.indexOf(memberData?.gender?:"Male"))
                binding.etDateOfBirth.text = memberData?.dob.toString().toEditable()
                if(memberData?.dob !=null) {
                    dobPreSet = convertToCalendar(memberData!!.dob)?:Calendar.getInstance()
                }
                binding.referInput.text = memberData?.referred_by?.toEditable()
            }
            binding.submitButton.visibility = GONE
            binding.nameInput.text = memberData?.name?.toEditable()


            if(memberData?.renewal_date !=null) {
                datePreSet = convertToCalendar(memberData!!.renewal_date)?:Calendar.getInstance()
            }
        }else{
            binding.deleteButton.visibility = GONE
            binding.updateButton.visibility = GONE
            binding.submitButton.visibility = VISIBLE
            datePreSet = Calendar.getInstance()
            mainViewModel.loadMembers()
            viewLifecycleOwner.lifecycleScope.launch {
                mainViewModel.memberList.collectLatest { members ->
                    validateReferralId(members)
                }
            }
        }
        val datePicker: DatePicker =  binding.datePicker


        // Set listener for when the user changes the date


        datePicker.init(datePreSet.get(Calendar.YEAR), datePreSet.get(Calendar.MONTH), datePreSet.get(Calendar.DAY_OF_MONTH)) { _, _, _, _ ->
            updateDateText(datePicker,  binding.textViewSelectedDate)
        }
        updateDateText(datePicker,  binding.textViewSelectedDate)
        setclicklistener()




    }
    private fun updateDueAmount() {
        val totalAmount = binding.totalAmountInput.text.toString().toIntOrNull() ?: 0
        val amountPaid = binding.amountPaidInput.text.toString().toIntOrNull() ?: 0
        val dueAmount = totalAmount - amountPaid

        binding.dueAmount.text = "Due amount: $dueAmount"
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
       // Log.e("checkhere","yes $dateIs")
    }
    private fun setclicklistener(){
        binding.submitButton.setOnClickListener(this)
        binding.updateButton.setOnClickListener(this)
        binding.renewButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)
        binding.etDateOfBirth.setOnClickListener(this)

    }
    override fun onClick(p0: View?) {

        when (p0) {
            binding.etDateOfBirth ->    showDatePickerDialog(binding.etDateOfBirth)

            binding.submitButton -> {
              //  Log.e("chadka",""+ binding.spinnerTypeGender.selectedItem.toString())

                if ( binding.etDateOfBirth.text.isNotEmpty() && binding.weightInput.text.isNotEmpty() && binding.heightInput.text.isNotEmpty() && binding.nameInput.text.isNotEmpty() && dateIs != "" && binding.emailInput.text.isNotEmpty() && binding.mobInput.text.isNotEmpty() && binding.paidmonthsInput.text.isNotEmpty()) {

                    dataAdd(false)
                } else {
                    if (context != null) {
                        Utils.showToast(requireContext(), "Fill all the fields")
                    }

                }

            }

            binding.updateButton -> {
                if (binding.etDateOfBirth.text.isNotEmpty() && binding.weightInput.text.isNotEmpty() && binding.heightInput.text.isNotEmpty() && binding.nameInput.text.isNotEmpty()  && binding.emailInput.text.isNotEmpty() && binding.mobInput.text.isNotEmpty() ) {
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
    private fun showDatePickerDialog(editText: EditText) {

        val calendar = dobPreSet ?: Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
activity?.let {
    val datePickerDialog =
        DatePickerDialog(
            ContextThemeWrapper(requireContext(), android.R.style.Theme_Holo_Light_Dialog), // Forces spinner mode
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth,selectedDay)
                val mysqlDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = mysqlDateFormat.format(selectedDate.time)
                editText.setText(formattedDate)
            },
            year, month, day
        )

    // Force spinner mode for devices >= Android 7 (Nougat)
    try {
        val datePicker = datePickerDialog.datePicker
        val method = datePicker.javaClass.getDeclaredMethod("setSpinnersShown", Boolean::class.java)
        method.invoke(datePicker, true)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // Show the DatePickerDialog
    datePickerDialog.show()
}
        // Use spinner mode by applying a custom theme

    }
/*    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
activity?.let {
    val datePickerDialog = DatePickerDialog(
        requireContext(),
        { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date and set it in EditText
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            editText.setText(formattedDate)
        },
        year, month, day
    )

    // Show the dialog
    datePickerDialog.show()
}

    }*/
    private fun deleteMember(){
        lifecycleScope.launch {

            val result =     memberData?.userid?.let {
                ApiRepository().deletemymembers(
                    mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,
                    it
                )
            }
            //Log.e("chadka",""+ Gson().toJson(result))
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
    if(trainerData?.isEmpty() == true){
        Utils.showToast(requireContext(), "No trainers found")
        return
    }
        lifecycleScope.launch {

            val result = if(!isUpdate){
                trainerData?.get(binding.spinnerTypeTrainer.selectedItemPosition)?.coachid
                    ?.let {
                        ApiRepository().addmymembers(
                            binding.nameInput.text.toString(),binding.emailInput.text.toString(),
                            mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.mobInput.text.toString().toLong(),
                            binding.paidmonthsInput.text.toString().toInt(),dateIs,
                            binding.etDateOfBirth.text.toString(),
                            binding.heightInput.text.toString().toFloat(),
                            binding.weightInput.text.toString().toFloat(),
                            binding.spinnerType.selectedItemPosition  + 1,
                            it,
                            binding.spinnerTypeGender.selectedItem.toString(),
                            binding.referInput.text.toString().trim()
                        )
                    }
            }else{
                if(isRenew == true){
                    memberData?.userid?.let {
                        ApiRepository().renewmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,binding.paidmonthsInput.text.toString().toInt(),dateIs,
                            it
                        )
                    }
                }else{
                    memberData?.userid?.let {
                        trainerData?.get(binding.spinnerTypeTrainer.selectedItemPosition)?.coachid?.let { it1 ->
                            ApiRepository().updatemymembers(
                                binding.nameInput.text.toString(),
                                binding.emailInput.text.toString(),
                                mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,
                                binding.mobInput.text.toString().toLong(),
                                it, binding.etDateOfBirth.text.toString(),
                                binding.heightInput.text.toString().toFloat(),
                                binding.weightInput.text.toString().toFloat(),
                                binding.spinnerType.selectedItemPosition + 1,
                                it1,
                                binding.spinnerTypeGender.selectedItem.toString(),

                                )
                        }
                    }
                }


            }
            //Log.e("chadka",""+ Gson().toJson(result))
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


    override fun onDestroyView() {
        super.onDestroyView()
        //Log.e("chadkaa","destroy")
        viewLifecycleOwner.lifecycleScope.coroutineContext.cancel()
    }
}