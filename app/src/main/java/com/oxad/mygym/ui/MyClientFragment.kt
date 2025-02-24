package com.oxad.mygym.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.mygym.R
import com.oxad.mygym.adapter.MemberAdapter
import com.oxad.mygym.adapter.MemberSearchAdapter
import com.oxad.mygym.adapter.RenewalAdapter
import com.oxad.mygym.database.AppDatabase
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.model.Member
import com.oxad.mygym.model.MemberResponse

import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager

import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.cancel

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call

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

activity?.let {
    memberAdapter = MemberAdapter(requireContext(),this)
    dataStoreManager = DataStoreManager(requireContext())
    binding.recyclerView.layoutManager = LinearLayoutManager(context)

    binding.recyclerView.adapter =  memberAdapter
    viewLifecycleOwner.lifecycleScope.launch {
        mainViewModel.memberList.collectLatest { members ->
            memberAdapter.submitList(members)
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
    binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        Log.w("isdestroyc","Yes")
    }

    override fun onUpdateClick(member: Member,isRenew:Boolean) {
        val bundle = Bundle().apply {
            putParcelable("member_data", member)
            putBoolean("is_renew", isRenew)
        }

        findNavController().navigate(R.id.action_myclientfragment_to_addmyclientfragment,bundle)
    }
private lateinit var dataStoreManager: DataStoreManager
    override fun onWhatsappSend(member: Member) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (::dataStoreManager.isInitialized) {
                dataStoreManager.referralMessage.collect { savedMessage ->
                    val fullmsg = savedMessage + " The Referral Code is ${member.referral_id}."
                    sendWhatsAppMessage(fullmsg, "91" + member.mobile.toString().replace(" ", ""))
                }
            }
        }

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