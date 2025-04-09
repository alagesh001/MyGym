package com.oxad.fitox.model

import android.net.Uri

data class Product(
    val id: Int,
    val name: String,
    val actualPrice: Double,
    val offerPrice: Double,
    val imageUrl: String
)
data class ProductAdd(
    val name: String,
    val actualPrice: Double,
    val offerPrice: Double,
    val imageUri: Uri? = null
)