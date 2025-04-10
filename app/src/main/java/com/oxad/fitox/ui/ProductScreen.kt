import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.oxad.fitox.model.ProductAdd
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import androidx.compose.material.icons.filled.AddAPhoto
@Composable
fun ProductScreen(
    products: List<Product>,
    onAddClicked: () -> Unit,
    onEditClicked: (Product) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick ={ showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items (products) { product ->
                ProductItem(product = product, onEditClicked = onEditClicked)
            }
        }
    }
    if (showDialog) {
        AddProductDialog(
            onAdd = { name, actual, offer, imageUri ->
                // Save or upload product here
            },
            onDismiss = { showDialog = false  }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AddProductDialog(
    onAdd: (String, String, String, Uri?) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var actualPrice by remember { mutableStateOf("") }
    var offerPrice by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var lastCompressedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

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
        title = { Text("Add Product") },
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

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") }
                )

                OutlinedTextField(
                    value = actualPrice,
                    onValueChange = { actualPrice = it },
                    label = { Text("Actual Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = offerPrice,
                    onValueChange = { offerPrice = it },
                    label = { Text("Offer Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank() && actualPrice.isNotBlank() && offerPrice.isNotBlank()) {
                    onAdd(name, actualPrice, offerPrice, imageUri ?: lastCompressedImageUri)
                }
            }) {
                Text("Add")
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
fun ProductItem(product: Product, onEditClicked: (Product) -> Unit) {
    Card(
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

            IconButton(onClick = { onEditClicked(product) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Product")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    val products = listOf(
        Product(1, "Whey Protein", 3000.0, 2499.0, "https://via.placeholder.com/100"),
        Product(2, "Fitness Tracker", 4999.0, 4299.0, "https://via.placeholder.com/100"),
        Product(3, "Dumbbells", 1500.0, 1199.0, "https://via.placeholder.com/100")
    )

    ProductScreen(products = products, onAddClicked = {}, onEditClicked = {})
}
