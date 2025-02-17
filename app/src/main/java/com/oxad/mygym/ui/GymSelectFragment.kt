package com.oxad.mygym.ui

import GymselectAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.mygym.R
import com.oxad.mygym.databinding.FragmentGymSelectBinding
import com.oxad.mygym.viewmodel.MainViewModel


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
        findNavController().navigate(R.id.action_gymSelectFragment_to_homeFragment)
    }
}