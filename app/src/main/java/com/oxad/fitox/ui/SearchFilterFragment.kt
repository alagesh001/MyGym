package com.oxad.fitox.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.oxad.fitox.R
import com.oxad.fitox.databinding.FragmentSearchFilterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFilterFragment : Fragment(), OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSearchFilterBinding

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
        binding = FragmentSearchFilterBinding.inflate(inflater, container, false)
        initUi()


        return binding.root

    }


    private fun initUi(){

       setclicklistener()
    }
    private fun setclicklistener(){
        binding.ageFilter.setOnClickListener(this)
        binding.genderFilter.setOnClickListener(this)
        binding.goalFilter.setOnClickListener(this)
        binding.trainerFilter.setOnClickListener(this)
        binding.dobFilter.setOnClickListener(this)
        binding.transacFilter.setOnClickListener(this)
        binding.attendanceFilter.setOnClickListener(this)


    }


    override fun onClick(p0: View?) {
        var bundle:Bundle? = null

        when(p0){
            binding.genderFilter -> {
                 bundle = Bundle().apply {
                    putString("type_is", "gender")
                }
            }
            binding.trainerFilter -> {
                 bundle = Bundle().apply {
                    putString("type_is", "trainer")
                }
            }
            binding.ageFilter -> {
                bundle = Bundle().apply {
                    putString("type_is", "age")
                }
            }
            binding.dobFilter -> {
                bundle = Bundle().apply {
                    putString("type_is", "dob")
                }
            }
            binding.goalFilter -> {
                bundle = Bundle().apply {
                    putString("type_is", "goal")
                }
            }
            binding.transacFilter ->{
                bundle = Bundle().apply {
                    putString("type_is", "transac")
                }
            }
            binding.attendanceFilter ->{
                bundle = Bundle().apply {
                    putString("type_is", "attendance")
                }
            }
        }
        bundle?.let {
            findNavController().navigate(R.id.action_searchfilfrag_to_filfrag,it)
        }

    }


}