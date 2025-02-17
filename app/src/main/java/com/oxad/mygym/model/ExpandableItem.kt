package com.oxad.mygym.model

data class ExpandableItem(
    val title: String,
    val details: String,
    val myCoach: Boolean=false,

    var isExpanded: Boolean = false // Track expansion state

)
