package com.oxad.mygym.ui

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
import com.oxad.mygym.R
import com.oxad.mygym.adapter.ExpandableAdapter
import com.oxad.mygym.adapter.RenewalAdapter
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.databinding.FragmentMyTrainerBinding
import com.oxad.mygym.model.ExpandableItem
import com.oxad.mygym.model.MyMemberListModel
import com.oxad.mygym.model.TrainerModel
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager
import com.oxad.mygym.utils.RsConstants
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [MyTrainersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyTrainersFragment : Fragment(),ExpandableAdapter.TrainerListener{
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentMyTrainerBinding
    private lateinit var expandableAdapter: ExpandableAdapter
    private lateinit var apiManager: ApiManager
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTrainerBinding.inflate(inflater, container, false)
        initUi()
        return binding.root


    }

    private fun initUi() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        activity?.let {
            expandableAdapter = ExpandableAdapter(requireContext(),this)
            binding.recyclerView.adapter = expandableAdapter
            lifecycleScope.launch {
                mainViewModel.trainerList.collectLatest { trainers ->
                    activity?.let {
                        expandableAdapter.submitList(trainers)
                    }
                }


            }
        }



    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyTrainersFragment().apply {

            }
    }




    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onUpdate(trainerModel: TrainerModel) {
        val bundle = Bundle().apply {
            putParcelable("trainer_data", trainerModel)
        }

        findNavController().navigate(R.id.action_myTrainerFragment_to_addmytrainerfragment,bundle)
    }
}