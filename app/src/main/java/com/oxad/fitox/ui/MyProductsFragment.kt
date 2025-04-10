package com.oxad.fitox.ui

import ProductScreen
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oxad.fitox.R
import com.oxad.fitox.adapter.RenewalAdapter
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.databinding.FragmentGymprofileBinding
import com.oxad.fitox.databinding.FragmentMyProductsBinding
import com.oxad.fitox.datastore.DataStoreManager
import com.oxad.fitox.model.MyMemberListModel
import com.oxad.fitox.model.Product
import com.oxad.fitox.retrofit.ApiInterface
import com.oxad.fitox.retrofit.ApiManager
import com.oxad.fitox.utils.RsConstants
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

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
    val products = listOf(
        Product(1, "Whey Protein", 3000.0, 2499.0, "https://m.media-amazon.com/images/I/710SxepIfiL.jpg"),
        Product(2, "Fitness Tracker", 4999.0, 4299.0, "https://m.media-amazon.com/images/I/710SxepIfiL.jpg"),
        Product(3, "Dumbbells", 1500.0, 1199.0, "https://m.media-amazon.com/images/I/710SxepIfiL.jpg")
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProductsBinding.inflate(inflater, container, false)
        return binding.root


    }
    private val viewModel: MainViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (!viewModel.gymProductOpenedFirsttime) {
            viewModel.gymProductOpenedFirsttime = true
            viewLifecycleOwner.lifecycleScope.launch {
                delay(15)
                val composeView = binding.composeStub.inflate().findViewById<ComposeView>(R.id.composeView)
                composeView.setContent {
                    DelayedComposeWithPlaceholder()
                }
                //   delay(10) // 1-second delay

            }
        }else{
            val composeView = binding.composeStub.inflate().findViewById<ComposeView>(R.id.composeView)
            composeView.setContent {
                var showDialog by remember { mutableStateOf(false) }

                ProductScreen(products, showDialog = showDialog,
                    onShowDialogChange = { showDialog = it }, onAddClicked = {name, actual, offer, imageUri -> addProductApiCall(name,actual,offer,imageUri)}, onEditClicked = {})
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun DelayedComposeWithPlaceholder() {
        var isLoaded by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(500) // Simulate loading
            isLoaded = true
        }

        if (isLoaded) {
            ProductScreen(products, showDialog = showDialog,
                onShowDialogChange = { showDialog = it }, onAddClicked = {name, actual, offer, imageUri -> addProductApiCall(name,actual,offer,imageUri)}, onEditClicked = {})
        } else {
            ProgressBar() // Show placeholder UI
        }
    }
     fun addProductApiCall(name:String,
                                  actual:Int,
                                  offer:Int,
                                  imageUri: Uri?){
        lifecycleScope.launch {

if(imageUri!=null) {
    activity?.let {
        val result =      ApiRepository().addProduct(
            name,
            offer,
            actual,
            mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId,
            imageUri,
            requireContext()
        )

        result.onSuccess { response ->
            if(response.success){

                if(context !=null) {
                //    mainViewModel.refreshMembers()
                 //   mainViewModel.loadTrainerData(DataStoreManager(requireContext()))

                    Utils.showToast(requireContext(), response.message)
                    //findNavController().popBackStack()

                }
               // findNavController().popBackStack()
            }else{
                if(context !=null) {
                    Utils.showToast(requireContext(), response.message)
                }
            }
        }

        result.onFailure { exception ->
            Utils.showToast(requireContext(), "Failed")
            exception.message ?: "Something went wrong!"
        }

    }



}
        }


    }
    fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path ?: "")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
    fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("temp", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream?.copyTo(output)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    @Composable
    fun ProgressBar() {
        var isLoading by remember { mutableStateOf(true) }
        var progress by remember { mutableFloatStateOf(0.0f) }
        var showDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            // Simulate progress
            while (progress < 1.0f) {
                progress += 0.01f
                delay(2)
            }
            isLoading = false
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = {
                        progress // Pass a Float value (0.0f to 1.0f)
                    },
                    Modifier.size(70.dp),
                    color =Color(LocalContext.current.getColor(R.color.tertiary_color)),
                    strokeWidth = 8.dp, // Customize the thickness
                    trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor, // Background track color
                )

            }
        } else {
            ProductScreen(products, showDialog = showDialog,
                onShowDialogChange = { showDialog = it }, onAddClicked = {name, actual, offer, imageUri -> addProductApiCall(name,actual,offer,imageUri)}, onEditClicked = {})
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