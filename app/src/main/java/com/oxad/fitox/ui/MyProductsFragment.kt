package com.oxad.fitox.ui

import AddProductDialog
import ProductScreen
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.fitox.adapter.RenewalAdapter
import com.oxad.fitox.databinding.FragmentMyProductsBinding
import com.oxad.fitox.model.MyMemberListModel
import com.oxad.fitox.model.Product
import com.oxad.fitox.retrofit.ApiInterface
import com.oxad.fitox.retrofit.ApiManager
import com.oxad.fitox.utils.RsConstants
import com.oxad.fitox.viewmodel.MainViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MyProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProductsFragment : Fragment(), ApiInterface {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentMyProductsBinding
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
        return ComposeView(requireContext()).apply {
            setContent {
                val products = listOf(
                    Product(1, "Whey Protein", 3000.0, 2499.0, "https://m.media-amazon.com/images/I/710SxepIfiL.jpg"),
                    Product(2, "Fitness Tracker", 4999.0, 4299.0, "https://m.media-amazon.com/images/I/710SxepIfiL.jpg"),
                    Product(3, "Dumbbells", 1500.0, 1199.0, "https://m.media-amazon.com/images/I/710SxepIfiL.jpg")
                )
                ProductScreen(products, onAddClicked = {}, onEditClicked = {})
            }
        }
    }

    private fun initUi() {
        apiManager.getmymembers(mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId)


        binding.recyclerView.layoutManager = LinearLayoutManager(context)


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyProductsFragment().apply {

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
               /* if (myMemberListModelres.result.isNotEmpty()) {
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