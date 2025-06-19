package com.oxad.fitox.ui

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.google.gson.Gson
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
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
    private lateinit var memberListSet: List<Member>
private var isSearching:Boolean = false
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
    private fun initUi() {
//apiManager.getmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId)
        // binding.circularProgressBar.visibility = VISIBLE
        //  binding.refreshTv.visibility = GONE
        binding.downloadiv.setOnClickListener{

                if (::memberListSet.isInitialized) {
                    activity?.let {
                        exportWithLoadingAndShare(requireContext(),memberListSet)

                }
            }
        }
        binding.expirySort.setOnClickListener {
            if (::memberListSet.isInitialized) {
                val sortedMembers = memberListSet.sortedBy { it.renewal_date }

                // Submit the sorted list to the adapter
                memberAdapter.submitList(sortedMembers)
            }
        }

        binding.nameSort.setOnClickListener {
            if (::memberListSet.isInitialized) {
                val sortedMembers =
                    memberListSet.sortedBy { it.name.lowercase(Locale.ROOT) } // Ensures case-insensitive sorting

                memberAdapter.submitList(sortedMembers)
            }

    }

        binding.refreshTv.setOnClickListener {
            val membList = emptyList<Member>()
            memberAdapter.submitList(membList)
          //  binding.circularProgressBar.visibility = VISIBLE
            mainViewModel.refreshMembers()        }
activity?.let {
    memberAdapter = MemberAdapter(requireContext(),this,mainViewModel.gymResult[mainViewModel.currentGymSelected].is_active)

    dataStoreManager = DataStoreManager(requireContext())
    binding.recyclerView.layoutManager = LinearLayoutManager(context)

    binding.recyclerView.adapter =  memberAdapter
    binding.swipeRefreshLayout.setOnRefreshListener {
        isSearching  = false
        if(context!=null) {
            if (isNetworkAvailable(requireContext())) {
                mainViewModel.clearmembers(true)
            } else {
                Utils.showToast(requireContext(),"No Network Connection")
                // No internet connection
            }

        }
       // val memList = emptyList<Member>()
       // memberAdapter.submitList(memList)

    }

    viewLifecycleOwner.lifecycleScope.launch {
        mainViewModel.memberList.collect { members ->
           // Log.e("chadksaa", "cachedMembers  RamCollectinggggggggggggg")
            memberListSet = members

            if (members.isEmpty()) {
                memberAdapter.submitList(emptyList()) // Clear adapter
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
                memberAdapter.submitList(members)
            }
        }
    }
    mainViewModel.members.observe(viewLifecycleOwner) { members ->
        if(isSearching){
            memberAdapter.submitList(members)
        }
}
        /*   viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
               mainViewModel.memberList.collect { members ->
                   withContext(Dispatchers.Main) {
                       members.forEach({
                           if(it.name == "Gomathi Ram"){
                               Log.e("chadksaa","cachedMembers Gomathi Ram ")

                           }

                       })

                       *//*if(members.isEmpty()){
                    binding.refreshTv.visibility = VISIBLE
                }else{
                    binding.refreshTv.visibility = GONE
                }*//*
               if(members.isEmpty()){

               }else{

                   binding.swipeRefreshLayout.isRefreshing = false // Hide refresh indicator
               }
                memberListSet = members
                //binding.circularProgressBar.visibility = GONE
                memberAdapter.submitList(members)
            }
        }

    }
   *//* memberSearchAdapter = MemberSearchAdapter()
    binding.recyclerViewSearch.layoutManager = LinearLayoutManager(context)
    binding.recyclerViewSearch.adapter = memberSearchAdapter*//*

    // Observe LiveData
    mainViewModel.members.observe(viewLifecycleOwner) { members ->
        memberAdapter.submitList(members)

        // memberSearchAdapter.submitList(members)
    }*/
    mainViewModel.searchMembers("")
    binding.searchView.setOnClickListener {
        binding.searchView.isIconified = false
    }
    binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            Log.e("dacjkax","yes onQueryTextSubmit - $query")
            isSearching  = true

            mainViewModel.searchMembers(query?:"")
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Log.e("dacjkax","yes onQueryTextChange - $newText")
            isSearching  = true

            mainViewModel.searchMembers(newText?:"")
            return true
        }
    })

}

        }


    fun exportWithLoadingAndShare(context: Context, users: List<Member>) {
        val dialog = ProgressDialog(context).apply {
            setMessage("Exporting CSV file...")
            setCancelable(false)
            show()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Define file path
                val file = File(context.getExternalFilesDir(null), "gym_members.csv")
                // Export data to CSV
                exportUsersToCsv(file, users)

                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    Toast.makeText(context, "Export complete!", Toast.LENGTH_SHORT).show()
                    // Share the CSV file
                    shareCsvFile(context, file)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    Log.e("CSVExportError", e.message.orEmpty())
                    Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun exportUsersToCsv(file: File, users: List<Member>) {
        // Create FileWriter and BufferedWriter
        BufferedWriter(FileWriter(file)).use { writer ->
            // Write header row
            writer.write(
                listOf(
                    "ID", "Mobile", "Name", "Renewal Date", "Email", "User ID", "Referral ID",
                    "Height", "Weight", "DOB", "AdminId", "Goal", "Gender", "Referred By",
                    "Due Amount", "Attendance Count", "Last Attend", "Gym ID"
                ).joinToString(",") + "\n"
            )

            // Write data rows
            users.forEachIndexed { index, user ->
                writer.write(
                    listOf(
                        (index + 1).toString(),
                        user.mobile,
                        user.name,
                        user.renewal_date,
                        user.email,
                        user.userid.toString(),
                        user.referral_id ?: "",
                        user.height.toString(),
                        user.weight.toString(),
                        user.dob,
                        user.coachid.toString(),
                        user.goal.toString(),
                        user.gender ?: "Male",
                        user.referred_by ?: "",
                        user.due_amount.toString(),
                        user.attendance_count.toString(),
                        user.last_attend,
                        user.gymid.toString()
                    ).joinToString(",") + "\n"
                )
            }
        }
    }

    private fun shareCsvFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Open or Share CSV File"))
    }



    /*    private fun exportWithLoadingAndShare(context: Context, users: List<Member>) {
        val dialog = ProgressDialog(context).apply {
            setMessage("Exporting Excel file...")
            setCancelable(false)
            show()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Export the Excel file
                val file = File(context.getExternalFilesDir(null), "my_gym_members.xlsx")
                exportUsersToExcel(context, users)

                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    Toast.makeText(context, "Export complete!", Toast.LENGTH_SHORT).show()
                    shareExcelFile(context, file)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    Log.e("sdafsv",""+e.message)
                    Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun exportUsersToExcel(context: Context, users: List<Member>) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Gym Members")

        // Header row
        val header = sheet.createRow(0)
        val headers = listOf(
            "ID", "Mobile", "Name", "Renewal Date", "Email", "User ID", "Referral ID",
            "Height", "Weight", "DOB", "AdminId", "Goal", "Gender", "Referred By",
            "Due Amount", "Attendance Count", "Last Attend", "Gym ID"
        )

        headers.forEachIndexed { index, title ->
            header.createCell(index).setCellValue(title)
        }

        // Data rows
        users.forEachIndexed { index, user ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue((index + 1).toDouble()) // Excel ID starting from 1
            row.createCell(1).setCellValue(user.mobile.toDouble())
            row.createCell(2).setCellValue(user.name)
            row.createCell(3).setCellValue(user.renewal_date)
            row.createCell(4).setCellValue(user.email)
            row.createCell(5).setCellValue(user.userid.toDouble())
            row.createCell(6).setCellValue(user.referral_id ?: "")
            row.createCell(7).setCellValue(user.height.toDouble())
            row.createCell(8).setCellValue(user.weight.toDouble())
            row.createCell(9).setCellValue(user.dob)
            row.createCell(10).setCellValue(user.coachid.toDouble())
            row.createCell(11).setCellValue(user.goal.toDouble())
            row.createCell(12).setCellValue(user.gender ?: "Male")
            row.createCell(13).setCellValue(user.referred_by ?: "")
            row.createCell(14).setCellValue(user.due_amount.toDouble())
            row.createCell(15).setCellValue(user.attendance_count.toDouble())
            row.createCell(16).setCellValue(user.last_attend)
            row.createCell(17).setCellValue(user.gymid.toDouble())
        }

        val file = File(context.getExternalFilesDir(null), "my_gym_members.xlsx")
        FileOutputStream(file).use { outputStream ->
            workbook.write(outputStream)
        }
        workbook.close()
    }
    fun shareExcelFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Open or Share Excel File"))
    }*/
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


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
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
    private fun showShareDialog(context: Context, mobile:String, userName: String, expiryDate: String,dueAmount: String,type: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_message_wtsapp, null)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Share Options")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .create()
when(type){
    1 ->{
        dialogView.findViewById<TextView>(R.id.btn_share_custom).visibility = VISIBLE
    }
        2 ->{
            dialogView.findViewById<TextView>(R.id.btn_share_renewal).visibility = VISIBLE

        }
            3->{
                dialogView.findViewById<TextView>(R.id.btn_share_expiry).visibility = VISIBLE

            }
                4->{
                    dialogView.findViewById<TextView>(R.id.btn_share_due).visibility = VISIBLE

                }

}
        dialogView.findViewById<TextView>(R.id.btn_share_renewal).setOnClickListener {
            val msg = "Hello $userName, your gym membership is expiring on $expiryDate. Please renew soon!"
            sendWhatsAppMessage(msg, "91" + mobile.replace(" ", ""))
        }

        dialogView.findViewById<TextView>(R.id.btn_share_expiry).setOnClickListener {
            val msg = "Hi $userName, your gym membership expired on $expiryDate. Renew now to continue your fitness journey!"
            sendWhatsAppMessage(msg, "91" + mobile.replace(" ", ""))
        }
        dialogView.findViewById<TextView>(R.id.btn_share_due).setOnClickListener {
            val msg = "Hi $userName, you have ₹$dueAmount pending for your gym membership. Kindly clear your dues to avoid interruption."
            sendWhatsAppMessage(msg, "91" + mobile.replace(" ", ""))
        }

        dialogView.findViewById<TextView>(R.id.btn_share_custom).setOnClickListener {
            val msg = "Hey $userName, don’t miss your next session. Stay fit!"
            sendWhatsAppMessage(msg, "91" + mobile.replace(" ", ""))
        }

        dialog.show()
    }

    override fun onWhatsappClick(mobile: Long,userName: String, expiryDate: String,dueAmount: Int,type:Int) {
       activity?.let {
           showShareDialog(requireContext(),mobile.toString(),userName,expiryDate,dueAmount.toString(),type)
       }
    }

    override fun oncallClick(mobile: Long) {
       activity?.let {
           initiatePhoneCall(requireContext(), "91" + mobile.toString().replace(" ", ""))
       }

    }
    fun showAttendanceDialog(context: Context, isMarked: Boolean, attendanceNumber: String,attendanceMsg:String) {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_attendance, null)
        val statusIcon = dialogView.findViewById<ImageView>(R.id.statusIcon)
        val attendanceMessage = dialogView.findViewById<TextView>(R.id.attendanceMessage)
        val attendanceCount = dialogView.findViewById<TextView>(R.id.attendanceCount)

        // Set values based on attendance status
        if (isMarked) {
            statusIcon.setImageResource(R.drawable.checked) // Use your check icon resource
            attendanceMessage.text = attendanceMsg
        } else {
            statusIcon.setImageDrawable(null)
            attendanceMessage.text = attendanceMsg
        }

        // Set attendance number
        attendanceCount.text = attendanceNumber

        // Build and show the dialog
        AlertDialog.Builder(context, R.style.CustomDialogTheme)
            .setView(dialogView)
            .setCancelable(true)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    override fun onMarkAttendClick(userid: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            val result =
                mainViewModel.apiRepoVM.markAttendance(
                    userid.toInt(),
                    mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId
                )

            result.onSuccess { response ->
                activity?.let {
                    showAttendanceDialog(requireContext(),response.success,response.a_count,response.message)
                }
              /*  if(response.success){
                    activity?.let {
                        showAttendanceDialog(requireContext(),response.success,response.a_count,response.message)
                    }

                    //  findNavController().popBackStack()`
                }else{

                    if(context !=null) {
                        Utils.showToast(requireContext(), response.message)
                    }
                }*/
            }

            result.onFailure { exception ->
                Utils.showToast(requireContext(), "Failed Network")
                exception.message ?: "Something went wrong!"
            }


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