package com.oxad.fitox.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.fitox.adapter.RenewalAdapter
import com.oxad.fitox.databinding.FragmentMyOffersBinding
import com.oxad.fitox.model.MyMemberListModel
import com.oxad.fitox.retrofit.ApiInterface
import com.oxad.fitox.retrofit.ApiManager
import com.oxad.fitox.utils.RsConstants
import com.oxad.fitox.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MyOffersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyOffersFragment : Fragment(), ApiInterface {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentMyOffersBinding
    private lateinit var renewalAdapter: RenewalAdapter
    private lateinit var apiManager: ApiManager
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        apiManager = ApiManager(requireContext(), this)
        binding = FragmentMyOffersBinding.inflate(inflater, container, false)
        //initUi()
        return binding.root


    }

    private fun initUi() {
        apiManager.getmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId)


        binding.recyclerView.layoutManager = LinearLayoutManager(context)


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyOffersFragment().apply {

            }
    }

    override fun onSuccess(response: Any, serviceCode: Int) {
        when (serviceCode) {
            RsConstants.GET_MY_MEMBERS -> setmemberList(response)
        }
    }

    override fun onError(error: String) {
    }

    private fun setmemberList(response: Any) {
        val myMemberListModelres = response as MyMemberListModel


        if (myMemberListModelres.success) {
            if (context != null) {
                // Utils.showToast(requireContext(), "Success")
             /*   if (myMemberListModelres.result.isNotEmpty()) {
                    renewalAdapter = RenewalAdapter(myMemberListModelres.result)
                    binding.recyclerView.adapter = renewalAdapter
                }*/
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("isdestroyc", "Yes")
    }
}