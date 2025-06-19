package com.oxad.fitox.retrofit

object ApiHelper {
    init {
        System.loadLibrary("native-lib") // Replace with your actual library name
    }

    external fun getretroUrL(): String
    external fun getUserregUrl(): String
    external fun enterInUrl(): String
    external fun lkeyFetch(): String
    external fun enterInMemberUrl(): String

    external fun gDatamembUrl(): String
    external fun vcoachUrl(): String
    external fun transCheckUrl(): String
    external fun gtDataUrl(): String
    external fun guTableUrl(): String
    external fun plusNewGymUrl(): String
    external fun plusNewCUrl(): String
    external fun gutrainerUrl(): String
    external fun gudueUrl(): String
    external fun minusCustUrl(): String
    external fun minusTrainerUrl(): String
    external fun plusTrainerUrl(): String
    external fun uCPassUrl(): String
    external fun uadmincontUrl(): String
    external fun addProductUrl(): String
    external fun getProductUrl(): String
    external fun updateProductUrl(): String
    external fun updateBroadmsgUrl(): String
    external fun updatedietscheduleurl(): String
    external fun getCustDataUrl(): String
    external fun getmarkAttendUrl(): String








}
