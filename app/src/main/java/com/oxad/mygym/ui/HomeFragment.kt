package com.oxad.mygym.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.oxad.mygym.R
import com.oxad.mygym.databinding.FragmentHomeBinding
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.model.TrainerModel
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(),OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentHomeBinding
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
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        initUi()
        return binding.root    }
    private fun initUi(){
        mainViewModel.loadMembers()

        activity?.lifecycleScope?.launch {
          Log.e("storetraninda",""+Gson().toJson(getCoachesList()))

        }
        binding.title.text = getString(R.string.welcometitle,mainViewModel.gymResult[mainViewModel.currentGymSelected].GymName,mainViewModel.gymResult[mainViewModel.currentGymSelected].Location)

        setclicklistener()
        // Set new gradient colors

    }
    suspend fun getCoachesList(): List<TrainerModel> {
        return DataStoreManager(requireContext()).getCoaches().first()
    }
    private fun setclicklistener(){
        binding.mytrainerRl.setOnClickListener(this)
        binding.searchfilterRl.setOnClickListener(this)
        binding.myreferRl.setOnClickListener(this)
        binding.complaintRl.setOnClickListener(this)
        binding.myclientsRl.setOnClickListener(this)
        binding.clientaddIv.setOnClickListener(this)
        binding.traineraddIv.setOnClickListener(this)

    }
    override fun onClick(p0: View?) {
        var code = 1
        when (p0) {
            binding.myclientsRl -> code = 1
            binding.mytrainerRl -> code = 2
            binding.searchfilterRl -> code = 3
            binding.myreferRl -> code = 4
            binding.complaintRl -> code = 5
            binding.clientaddIv -> code =6
            binding.traineraddIv -> code = 7
        }
        openFragment(code)
    }

    private fun openFragment(code:Int){

        when(code)
        {
            1 ->  findNavController().navigate(R.id.action_homeFragment_to_myclientfragment)
            2 ->findNavController().navigate(R.id.action_homeFragment_to_myTrainerFragment)
            3 -> findNavController().navigate(R.id.action_homeFragment_to_searchfilfrag)
            4 -> findNavController().navigate(R.id.action_homeFragment_to_myreffrag)
            6 -> findNavController().navigate(R.id.action_homeFragment_to_addmyclientfragment)
            7 -> findNavController().navigate(R.id.action_homeFragment_to_addmytrainerfragment)


        }

    }

}