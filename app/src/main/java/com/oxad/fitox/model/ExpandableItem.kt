package com.oxad.fitox.model

data class ExpandableItem(
    val title: String,
    val details: String,

    var isExpanded: Boolean = false // Track expansion state

)
