package com.oxad.mygym.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.mygym.R
import com.oxad.mygym.activity.MainActivity
import com.oxad.mygym.adapter.RenewalAdapter
import com.oxad.mygym.databinding.FragmentHomeBinding
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.model.AdminLoginModel
import com.oxad.mygym.model.MyClient
import com.oxad.mygym.model.MyMemberListModel
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager
import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.utils.Utils
import com.oxad.mygym.viewmodel.MainViewModel

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
    private lateinit var myMemberListModel: MyMemberListModel
    private lateinit var apiManager: ApiManager
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
apiManager.getmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId)

     /*   myMemberListModel = MyMemberListModel(
            success = true,
            message = "Data Loaded Successfully",
            result = arrayListOf(
                MyClient("John Doe", 1234567890, "2025-03-01"),
                MyClient("Jane Smith", 9876543210, "2025-04-15"),
                MyClient("Mike Johnson", 1122334455, "2025-06-10")
            )
        )*/

        binding.recyclerView.layoutManager = LinearLayoutManager(context)


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
        val myMemberListModelres =  response as MyMemberListModel


        if(myMemberListModelres.success) {
            if(context !=null) {
                Utils.showToast(requireContext(), "Success")
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