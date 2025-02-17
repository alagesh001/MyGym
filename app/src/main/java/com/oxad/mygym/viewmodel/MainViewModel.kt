package com.oxad.mygym.viewmodel

import androidx.lifecycle.ViewModel
import com.oxad.mygym.model.LoginResult

class MainViewModel : ViewModel() {

    var gymResult: ArrayList<LoginResult> = ArrayList()
    var currentGymSelected: Int = 0

}