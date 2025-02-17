package com.oxad.mygym.model

import android.os.Parcel
import android.os.Parcelable

data class ApiResponse(val s:String)
data class UserRegisterModel(
    val success: Boolean,
    val message: String,
    val isAdmin: Int

)
data class MyMemberListModel(

    val success: Boolean,

    val message: String,

    var result: ArrayList<MyClient>

)
data class MyClient(

    val name: String,
    val mobile: Long,
    val renewal_date: String

)
data class AdminLoginModel(

    val success: Boolean,

    val message: String,

    val result: ArrayList<LoginResult>

) : Parcelable {

    constructor(parcel: Parcel) : this(

        parcel.readByte() != 0.toByte(),

        parcel.readString() ?: "",

        parcel.createTypedArrayList(LoginResult.CREATOR) ?: arrayListOf()

    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeByte(if (success) 1 else 0)

        parcel.writeString(message)

        parcel.writeTypedList(result)

    }


    override fun describeContents(): Int {

        return 0

    }


    companion object CREATOR : Parcelable.Creator<AdminLoginModel> {

        override fun createFromParcel(parcel: Parcel): AdminLoginModel {

            return AdminLoginModel(parcel)

        }


        override fun newArray(size: Int): Array<AdminLoginModel?> {

            return arrayOfNulls(size)

        }

    }

}
data class LoginResult(

    val Id: Int,

    val GymName: String,

    val Location: String,

    val GymId: Int,
    val membercount: Int

) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Id)
        parcel.writeString(GymName)
        parcel.writeString(Location)
        parcel.writeInt(GymId)
        parcel.writeInt(membercount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginResult> {
        override fun createFromParcel(parcel: Parcel): LoginResult {
            return LoginResult(parcel)
        }

        override fun newArray(size: Int): Array<LoginResult?> {
            return arrayOfNulls(size)
        }
    }
}
