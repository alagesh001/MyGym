package com.oxad.mygym.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.TableInfo
import com.oxad.mygym.R
import com.oxad.mygym.adapter.MemberAdapter
import com.oxad.mygym.adapter.RenewalAdapter
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.databinding.FragmentSearchFilterBinding
import com.oxad.mygym.datastore.DataStoreManager
import com.oxad.mygym.model.Member
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        }
        bundle?.let {
            findNavController().navigate(R.id.action_searchfilfrag_to_filfrag,it)
        }

    }


}