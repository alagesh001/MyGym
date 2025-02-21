package com.oxad.mygym.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.mygym.R
import com.oxad.mygym.adapter.MemberAdapter
import com.oxad.mygym.adapter.MemberSearchAdapter
import com.oxad.mygym.adapter.RenewalAdapter
import com.oxad.mygym.database.AppDatabase
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.model.MemberResponse

import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager

import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.viewmodel.MainViewModel

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
class MyClientFragment : Fragment(),ApiInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMyClientBinding
    private lateinit var renewalAdapter: RenewalAdapter
    private lateinit var apiManager: ApiManager
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
apiManager = ApiManager(requireContext(),this)
        binding = FragmentMyClientBinding.inflate(inflater, container, false)
        initUi()
        return binding.root


    }
    private fun initUi(){
//apiManager.getmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId)

activity?.let {
    memberAdapter = MemberAdapter()

    binding.recyclerView.layoutManager = LinearLayoutManager(context)

    binding.recyclerView.adapter =  memberAdapter
    mainViewModel.loadMembers()
  /*  lifecycleScope.launch {
        mainViewModel.memberList.collectLatest { members ->
            memberAdapter.submitList(members)
        }

    }*/
   /* memberSearchAdapter = MemberSearchAdapter()
    binding.recyclerViewSearch.layoutManager = LinearLayoutManager(context)
    binding.recyclerViewSearch.adapter = memberSearchAdapter*/

    // Observe LiveData
    mainViewModel.members.observe(viewLifecycleOwner) { members ->
        memberAdapter.submitList(members)

        // memberSearchAdapter.submitList(members)
    }
    mainViewModel.searchMembers("")

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

    override fun onSuccess(response: Any, serviceCode: Int) {
        when(serviceCode){
            RsConstants.GET_MY_MEMBERS -> setmemberList(response)
        }
    }

    override fun onError(error: String) {
    }
    private fun setmemberList(response: Any){
        val myMemberListModelres =  response as MemberResponse


        if(myMemberListModelres.success) {
            if(context !=null) {
               // Utils.showToast(requireContext(), "Success")
                if (myMemberListModelres.result.isNotEmpty()) {
                    renewalAdapter = RenewalAdapter(myMemberListModelres.result)
                    binding.recyclerView.adapter = renewalAdapter
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("isdestroyc","Yes")
    }


}