package com.oxad.fitox.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.oxad.fitox.R
import com.oxad.fitox.adapter.MemberAdapter
import com.oxad.fitox.adapter.MemberSearchAdapter
import com.oxad.fitox.adapter.RenewalAdapter
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.databinding.FragmentMyClientBinding
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.model.Member
import com.oxad.fitox.utils.Utils

import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyClientFragment : Fragment(),MemberAdapter.MemberAdapterListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMyClientBinding
    private lateinit var renewalAdapter: RenewalAdapter
    private lateinit var memberAdapter: MemberAdapter
    private lateinit var memberSearchAdapter: MemberSearchAdapter

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
        binding = FragmentMyClientBinding.inflate(inflater, container, false)
        initUi()
        return binding.root


    }
    private fun initUi(){
//apiManager.getmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId)
        binding.circularProgressBar.visibility = VISIBLE
        binding.refreshTv.visibility = GONE

        binding.refreshTv.setOnClickListener {
            Log.d("djafjs","Yes cl")
            val membList = emptyList<Member>()
            memberAdapter.submitList(membList)
            binding.circularProgressBar.visibility = VISIBLE
            mainViewModel.refreshMembers()        }
activity?.let {
    memberAdapter = MemberAdapter(requireContext(),this)
    dataStoreManager = DataStoreManager(requireContext())
    binding.recyclerView.layoutManager = LinearLayoutManager(context)

    binding.recyclerView.adapter =  memberAdapter
    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
        mainViewModel.memberList.collect { members ->
            withContext(Dispatchers.Main) {
                if(members.isEmpty()){
                    binding.refreshTv.visibility = VISIBLE
                }else{
                    binding.refreshTv.visibility = GONE
                }

                binding.circularProgressBar.visibility = GONE
                memberAdapter.submitList(members)
            }
        }

    }
   /* memberSearchAdapter = MemberSearchAdapter()
    binding.recyclerViewSearch.layoutManager = LinearLayoutManager(context)
    binding.recyclerViewSearch.adapter = memberSearchAdapter*/

    // Observe LiveData
    mainViewModel.members.observe(viewLifecycleOwner) { members ->
        memberAdapter.submitList(members)

        // memberSearchAdapter.submitList(members)
    }
    mainViewModel.searchMembers("")
    binding.searchView.setOnClickListener {
        binding.searchView.isIconified = false
    }
    binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
             mainViewModel.searchMembers(query?:"")
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            mainViewModel.searchMembers(newText?:"")
            return true
        }
    })

}

        }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyClientFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun onDestroy() {
        super.onDestroy()
      //  Log.w("isdestroyc","Yes")
    }

    override fun onUpdateClick(member: Member,isRenew:Boolean) {
        val bundle = Bundle().apply {
            putParcelable("member_data", member)
            putBoolean("is_renew", isRenew)
        }
        findNavController().navigate(R.id.action_myclientfragment_to_addmyclientfragment,bundle)

    }

    override fun onDueClick(member: Member) {
        val bundle = Bundle().apply {
            putParcelable("member_data", member)
            putBoolean("is_due", true)
        }

        findNavController().navigate(R.id.action_myclientfragment_to_addmyclientfragment,bundle)
    }

    private lateinit var dataStoreManager: DataStoreManager
    override fun onWhatsappSend(member: Member) {
        viewLifecycleOwner.lifecycleScope.launch {
            val result =
                mainViewModel.apiRepoVM.getcustdata(
                    mainViewModel.a_keyIs,
                   member.userid
                )

            result.onSuccess { response ->
                if(response.success){

activity?.let {
    showInfoDialog(requireContext(),response.result.name,response.result.userid,response.result.password,response.result.referral_id,member.mobile,response.lgin_url)
}

                    //  findNavController().popBackStack()
                }else{

                    if(context !=null) {
                        Utils.showToast(requireContext(), response.message)
                    }
                }
            }

            result.onFailure { exception ->
                Utils.showToast(requireContext(), "Failed Network")
                exception.message ?: "Something went wrong!"
            }


        }

    }

    private fun showInfoDialog(context: Context, name: String,userid: Int, password: String, referralId: String,mobile:Long,lgin_url:String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_user_info, null)

        // Populate data dynamically
        dialogView.findViewById<TextView>(R.id.tvlginUrl).text = "Login Url: $lgin_url"
        dialogView.findViewById<TextView>(R.id.tvName).text = "Name: $name"

        dialogView.findViewById<TextView>(R.id.tvUserId).text = "User ID: $userid"
        dialogView.findViewById<TextView>(R.id.tvPassword).text = "Password: $password"
        dialogView.findViewById<TextView>(R.id.tvReferralId).text = "Referral ID: $referralId"

        // Create the dialog
        val dialog = AlertDialog.Builder(context, R.style.CustomDialogTheme)
            .setView(dialogView)
            .create()

        // Set actions
        dialogView.findViewById<Button>(R.id.btnShare).setOnClickListener {

            val loginUrl = lgin_url // Replace with your actual login URL
            val fullMsg = """
    Hi ${name}, 
    Here are your account details:
    
    User ID: ${userid}
    Password: ${password}
    Referral ID: ${referralId}
    
    You can log in using the link below:
    $loginUrl
    
    Use this Referral ID to invite others and enjoy exclusive benefits!
""".trimIndent()

            sendWhatsAppMessage(fullMsg, "91" + mobile.toString().replace(" ", ""))
            dialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onDietSchedule(userid: Long) {

        showDateTimePickerDialog(userid)
    }

    override fun onWhatsappClick(mobile: Long) {
        sendWhatsAppMessage("", "91" + mobile.toString().replace(" ", ""))

    }

    override fun oncallClick(mobile: Long) {
       activity?.let {
           initiatePhoneCall(requireContext(), "91" + mobile.toString().replace(" ", ""))
       }

    }
    private var phoneNumber: String = ""

    private val requestCallPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            makePhoneCall(phoneNumber) // If granted, proceed to make the call
        } else {
            activity?.let {

                Utils.showToast(requireContext(), "Call permission is required to make calls.")
            }
        }
    }
    private fun initiatePhoneCall(context: Context, number: String) {
        phoneNumber = number
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            makePhoneCall(phoneNumber)
        } else {
            // Request permission
            requestCallPermission.launch(android.Manifest.permission.CALL_PHONE)
        }
    }
    private fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        try {
            startActivity(intent)
        } catch (e: SecurityException) {
            activity?.let {
                Utils.showToast(requireContext(), "Call failed. Permission is missing.")
            }
        }
    }

    private fun showDateTimePickerDialog(userid: Long) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        activity?.let {
            // Show DatePickerDialog first
            val datePickerDialog = DatePickerDialog(
                ContextThemeWrapper(requireContext(), android.R.style.Theme_Holo_Light_Dialog),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // After date is picked, show TimePickerDialog
                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        { _, selectedHour, selectedMinute ->
                            val selectedDateTime = Calendar.getInstance()
                            selectedDateTime.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)

                            val mysqlDateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val formattedDateTime = mysqlDateTimeFormat.format(selectedDateTime.time)

                            // Send the formattedDateTime to the database
                            sendToDatabase(formattedDateTime,userid)
                        },
                        hour, minute, true // Use 24-hour format
                    )
                    timePickerDialog.show()
                },
                year, month, day
            )

            // Force spinner mode for the DatePickerDialog
            try {
                val datePicker = datePickerDialog.datePicker
                val method = datePicker.javaClass.getDeclaredMethod("setSpinnersShown", Boolean::class.java)
                method.invoke(datePicker, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            datePickerDialog.show()
        }
    }
    fun showSuccessDialog(context: Context, message: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_success, null)

        // Populate the success message
        dialogView.findViewById<TextView>(R.id.successMessage).text = message

        // Create and show the dialog
        val dialog = Dialog(context).apply {
            setContentView(dialogView)
            setCancelable(false) // Prevent closing the dialog by tapping outside
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        // Handle the OK button click
        dialogView.findViewById<Button>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    // Function to send data to the database
    private fun sendToDatabase(dateTime: String,userid: Long) {
        // Use your existing API or database interaction code here
        // Example:
      //  Log.d("DatesasTime", "Selected DateTime to send: $userid $dateTime")
        lifecycleScope.launch {
            val result =
                mainViewModel.apiRepoVM.updatedietSchedule(userid,dateTime)

            //Log.e("chadka",""+ Gson().toJson(result))
            result.onSuccess { response ->
                if(response.success){
                       activity?.let {
                           showSuccessDialog(requireContext(), "Successfully scheduled!")

                       }

                       // Utils.showToast(requireContext(), response.message)


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
        // Add network or database interaction code to save the dateTime string
    }
    override fun onDestroyView() {
        super.onDestroyView()
       // Log.e("chadkaa","destroy")
        viewLifecycleOwner.lifecycleScope.coroutineContext.cancel()
    }
    private fun sendWhatsAppMessage(message: String, phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$phoneNumber?text=" + Uri.encode(message))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
        }
    }
}