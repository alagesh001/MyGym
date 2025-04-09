package com.oxad.fitox.ui

import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.oxad.fitox.R
import com.oxad.fitox.adapter.ExpandableAdapter
import com.oxad.fitox.databinding.FragmentMyTrainerBinding
import com.oxad.fitox.model.TrainerModel
import com.oxad.fitox.retrofit.ApiManager
import com.oxad.fitox.viewmodel.MainViewModel
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
        binding.passwordInput.text = mainViewModel.dieticianData?.password?.toString()
        binding.passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.togglePassword.setImageResource(R.drawable.eye_off)
        binding.togglePassword.setOnClickListener {
            if (binding.passwordInput.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                binding.passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.togglePassword.setImageResource(R.drawable.eye_off)
            } else {
                binding.passwordInput.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.togglePassword.setImageResource(R.drawable.eye_24)
            }
        }
        binding.btnTrainer.setOnClickListener {
            binding.recyclerView.visibility = View.VISIBLE
            binding.dieticianInfoContainer.visibility = View.GONE
            activity?.let {
                binding.btnDietician.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                binding.btnTrainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondary_color));
                binding.btnDietician.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                binding.btnTrainer.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                binding.btnTrainer.scaleX = 1.1f;
                binding.btnTrainer.scaleY = 1.1f;

                binding.btnDietician.scaleX = 1.0f;
                binding.btnDietician.scaleY = 1.0f;
            }

        }
        binding.btnDietician.setOnClickListener {
            binding.recyclerView.visibility = View.GONE
            binding.dieticianInfoContainer.visibility = View.VISIBLE
            activity?.let {
            binding.btnDietician.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.secondary_color));
            binding.btnTrainer.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
            binding.btnDietician.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.btnTrainer.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                binding.btnDietician.scaleX = 1.1f;
                binding.btnDietician.scaleY = 1.1f;

                binding.btnTrainer.scaleX = 1.0f;
                binding.btnTrainer.scaleY = 1.0f;

                }
        }
        binding.dieticianName.text = mainViewModel.dieticianData?.name
        binding.dieticianId.text = mainViewModel.dieticianData?.coachid.toString()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        activity?.let {
            expandableAdapter = ExpandableAdapter(requireContext(),this)
            binding.recyclerView.adapter = expandableAdapter
            lifecycleScope.launch {
                mainViewModel.trainerList.collectLatest { trainers ->
                    activity?.let {
                        expandableAdapter.submitList(trainers)

                        // You can now use 'dieticians' separately
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