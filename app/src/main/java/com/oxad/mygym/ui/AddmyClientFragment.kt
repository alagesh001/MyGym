package com.oxad.mygym.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oxad.mygym.R
import com.oxad.mygym.databinding.FragmentAddmyClientBinding
import com.oxad.mygym.databinding.FragmentMyClientBinding
import com.oxad.mygym.retrofit.ApiInterface
import com.oxad.mygym.retrofit.ApiManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddmyClientFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddmyClientFragment : Fragment(),ApiInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAddmyClientBinding
    private lateinit var apiManager: ApiManager

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
        apiManager = ApiManager(requireContext(),this)
        binding = FragmentAddmyClientBinding.inflate(inflater, container, false)
       // initUi()
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddmyClientFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSuccess(response: Any, serviceCode: Int) {
    }

    override fun onError(error: String) {
    }
}