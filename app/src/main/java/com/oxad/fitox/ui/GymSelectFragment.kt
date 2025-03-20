package com.oxad.fitox.ui

import GymselectAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.fitox.R
import com.oxad.fitox.databinding.FragmentGymSelectBinding
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GymSelectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GymSelectFragment : Fragment(),GymselectAdapter.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private lateinit var binding: FragmentGymSelectBinding
    private var gymselectAdapter: GymselectAdapter? = null
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGymSelectBinding.inflate(inflater, container, false)
        initUi()
        return binding.root     }
    private fun initUi(){
       // Log.e("checkfra","Yes")

        activity?.lifecycleScope?.launch {
            mainViewModel.clearmembers()
            if(context!=null) {
                DataStoreManager(requireContext()).clearCoaches()
            }
        }
        binding.recyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        gymselectAdapter = GymselectAdapter(mainViewModel.gymResult,this)
        binding.recyclerView.setAdapter(gymselectAdapter)

    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GymSelectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemSelected(item: Int) {
        mainViewModel.currentGymSelected = item
        val renewalDateString = mainViewModel.gymResult[mainViewModel.currentGymSelected].renewaldate
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val renewalDate = dateFormat.parse(renewalDateString)?.time

        activity?.let { ctx ->
            if (renewalDate != null) {
                if(renewalDate < System.currentTimeMillis()){
                    Utils.showToast(ctx,"Kindly renew to enter.")
                }else{
                    mainViewModel.loadTrainerData(DataStoreManager(ctx))
                    findNavController().navigate(R.id.action_gymSelectFragment_to_homeFragment)
                }
            }
        }

    }
}