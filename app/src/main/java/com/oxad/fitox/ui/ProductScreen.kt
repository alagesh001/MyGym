package com.oxad.fitox.ui

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.oxad.fitox.model.Product
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.launch
var isEdit:Boolean = false
var selectedProduct:Product ? =null

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ProductScreen(
    mainVM:MainViewModel,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onEditClicked: (Product) -> Unit
) {
  //  var showDialog by remember { mutableStateOf(false) }
    val products by mainVM.productData.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick ={
                isEdit = false
                onShowDialogChange(true)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        LazyColumn ( modifier = Modifier
            .padding(padding)
            .padding(16.dp)){
            val safeProducts = products ?: emptyList()
            if (safeProducts.isNotEmpty()) {
                items(safeProducts) { product ->
                    ProductItem(
                        onShowDialogChange = onShowDialogChange,
                        mainVM = mainVM,
                        onDismiss = { onShowDialogChange(false) },
                        product = product,
                        onEditClicked = onEditClicked
                    )
                }
            } else {
                item {
                    Text(
                        text = "No products available",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }
    if (showDialog ) {
        AddProductDialog(
            isEdit,
            onShowDialogChange,
            mainVM,
            onDismiss = { onShowDialogChange(false) }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AddProductDialog(
    isEdit:Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    mainVM:MainViewModel,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var offerPrice by remember { mutableStateOf(0) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var lastCompressedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var actualPriceInput by remember { mutableStateOf("") }
    var offerPriceInput by remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    if(isEdit){
        name = selectedProduct?.name.toString()
        actualPriceInput = selectedProduct?.actualPrice?.toInt().toString()
        offerPriceInput = selectedProduct?.offerPrice?.toInt().toString()
    }

    // Crop result handler
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = result.data?.let { UCrop.getOutput(it) }
            resultUri?.let { uri ->
                Log.e("adfjkakjfa","uri $uri")
                val realUri = uri
                compressImage(context, uri) { compressedUri ->
                    imageUri = compressedUri
                    Log.e("adfjkakjfa","compressedUri $imageUri")

                    if (compressedUri != null) {
                        Log.e("adfjkakjfa","lastCompressedImageUri $lastCompressedImageUri")

                        lastCompressedImageUri = compressedUri
                    }else{
                        imageUri = realUri
                    }
                }
            }
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val destinationUri = Uri.fromFile(
                File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
            )
            val uCrop = UCrop.of(uri, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512)
            cropLauncher.launch(uCrop.getIntent(context))
        }
    }

    // Dialog UI
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (isEdit) "Edit Product" else "Add Product")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        if(isEdit){
                            Image(
                                painter = rememberAsyncImagePainter(selectedProduct?.imageUrl),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }else{
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.AddAPhoto,
                                    contentDescription = "Add Image",
                                    tint = Color.DarkGray,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text("Tap to select image", color = Color.DarkGray, fontSize = 12.sp)
                            }
                        }

                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") }
                )


                OutlinedTextField(
                    value = actualPriceInput,
                    onValueChange = { actualPriceInput = it.filter { char -> char.isDigit() } },
                    label = { Text("Actual Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = offerPriceInput,
                    onValueChange = { offerPriceInput = it.filter { char -> char.isDigit() } },
                    label = { Text("Offer Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank() && actualPriceInput.isNotBlank() && offerPriceInput.isNotBlank()) {
                    val actualPrice: Int = actualPriceInput.toInt()
                    val offerPrice: Int = offerPriceInput.toInt()
                    val pid: Int = selectedProduct?.id?:0

                    if(isEdit){
                        updateProductApiCall(onShowDialogChange,context,mainVM,name,actualPrice,offerPrice,pid,imageUri?:lastCompressedImageUri,lifecycleOwner)

                    }else{
                        addProductApiCall(onShowDialogChange,context,mainVM,name,actualPrice,offerPrice,imageUri?:lastCompressedImageUri,lifecycleOwner)

                    }

                }
            }) {
                    Text(text = if (isEdit) "Update" else "Add Product")
                }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }

    )
}

// Compression function
@RequiresApi(Build.VERSION_CODES.P)
fun compressImage(context: Context, uri: Uri, onResult: (Uri?) -> Unit) {
    try {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)

        // Create output file
        val file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        var quality = 50 // Start with moderate quality
        var success = false

        while (quality > 10) {
            val outputStream = FileOutputStream(file)
            success = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            // Check file size
            if (file.length() < 10 * 1024) break // Success if below 10KB
            quality -= 10 // Reduce quality and try again
        }
        Log.e("adfjkakjfa","file size ${file.length()}")

        val compressedUri = if (success) Uri.fromFile(file) else null
        onResult(compressedUri)

    } catch (e: Exception) {
        e.printStackTrace()
        onResult(null)
    }
}





@Composable
fun ProductItem( onShowDialogChange: (Boolean) -> Unit,
                 mainVM:MainViewModel,
                 onDismiss: () -> Unit,product: Product, onEditClicked: (Product) -> Unit) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    Card(
        onClick = {
            selectedProduct = product
            isEdit = true
            onShowDialogChange(true)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text("Price: ₹${product.actualPrice}", textDecoration = TextDecoration.LineThrough)
                Text("Offer: ₹${product.offerPrice}", color = Color(0xFF2E7D32))
            }

            IconButton(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .size(48.dp) // Ensure a consistent button size
                    .clip(CircleShape) // Make the button circular
                    .background(Color(0xFFFDECEC)) // Light red background for delete action
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red, // Red tint for the icon
                    modifier = Modifier
                        .size(24.dp) // Icon size inside the button
                )
            }
            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = {
                        Text(text = "Confirm Delete")
                    },
                    text = {
                        Text("Are you sure you want to delete this product? This action cannot be undone.")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                           // onDeleteConfirmed()
                            lifecycleOwner.lifecycleScope.launch {
                                //   Log.d("MultipartDataa", "pid Part: ${pid}")

                                val result =      ApiRepository().deleteProduct(
                                    mainVM.gymResult[mainVM.currentGymSelected].GymId,
                                    product.id,
                                )

                                result.onSuccess { response ->
                                    if(response.success){
                                        mainVM.loadProductsData()
                                        onShowDialogChange(false)
                                        if(context !=null) {
                                            //    mainViewModel.refreshMembers()
                                            //   mainViewModel.loadTrainerData(DataStoreManager(requireContext()))

                                            Utils.showToast(context, response.message)
                                            //findNavController().popBackStack()

                                        }
                                        // findNavController().popBackStack()
                                    }else{
                                        if(context !=null) {
                                            Utils.showToast(context, response.message)
                                        }
                                    }
                                }

                                result.onFailure { exception ->
                                    Utils.showToast(context, "Failed")
                                    exception.message ?: "Something went wrong!"
                                }






                            }

                            showConfirmDialog = false
                        }) {
                            Text("Delete", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showConfirmDialog = false
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
fun addProductApiCall( onShowDialogChange: (Boolean) -> Unit,context:Context,mainVM: MainViewModel,name:String,
                      actual:Int,
                      offer:Int,
                      imageUri: Uri?,lifecycleOwner: LifecycleOwner){
    lifecycleOwner.lifecycleScope.launch {

        if(imageUri!=null) {
                val result =      ApiRepository().addProduct(
                    name,
                    offer,
                    actual,
                    mainVM.gymResult[mainVM.currentGymSelected].GymId,
                    imageUri,
                    context
                )

                result.onSuccess { response ->
                    if(response.success){
                        mainVM.loadProductsData()
                        onShowDialogChange(false)
                        if(context !=null) {
                            //    mainViewModel.refreshMembers()
                            //   mainViewModel.loadTrainerData(DataStoreManager(requireContext()))

                            Utils.showToast(context, response.message)
                            //findNavController().popBackStack()

                        }
                        // findNavController().popBackStack()
                    }else{
                        if(context !=null) {
                            Utils.showToast(context, response.message)
                        }
                    }
                }

                result.onFailure { exception ->
                    Utils.showToast(context, "Failed")
                    exception.message ?: "Something went wrong!"
                }





        }else{
            Utils.showToast(context, "Image not selected")

        }
    }


}
fun updateProductApiCall( onShowDialogChange: (Boolean) -> Unit,context:Context,mainVM: MainViewModel,name:String,
                       actual:Int,
                       offer:Int,pid:Int,
                       imageUri: Uri?,lifecycleOwner: LifecycleOwner){
    lifecycleOwner.lifecycleScope.launch {
     //   Log.d("MultipartDataa", "pid Part: ${pid}")

            val result =      ApiRepository().updateProduct(
                name,
                offer,
                actual,
                mainVM.gymResult[mainVM.currentGymSelected].GymId,
                imageUri,
pid,
                context
            )

            result.onSuccess { response ->
                if(response.success){
                    mainVM.loadProductsData()
                    onShowDialogChange(false)
                    if(context !=null) {
                        //    mainViewModel.refreshMembers()
                        //   mainViewModel.loadTrainerData(DataStoreManager(requireContext()))

                        Utils.showToast(context, response.message)
                        //findNavController().popBackStack()

                    }
                    // findNavController().popBackStack()
                }else{
                    if(context !=null) {
                        Utils.showToast(context, response.message)
                    }
                }
            }

            result.onFailure { exception ->
                Utils.showToast(context, "Failed")
                exception.message ?: "Something went wrong!"
            }






    }


}
@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    val products = listOf(
        Product(1, "Whey Protein", 3000.0, 2499.0, "https://via.placeholder.com/100"),
        Product(2, "Fitness Tracker", 4999.0, 4299.0, "https://via.placeholder.com/100"),
        Product(3, "Dumbbells", 1500.0, 1199.0, "https://via.placeholder.com/100")
    )

   // com.oxad.fitox.ui.ProductScreen(products = products, onAddClicked = {}, onEditClicked = {})
}
