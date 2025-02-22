package com.oxad.mygym.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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

data class MemberResponse(
    val success: Boolean,
    @SerializedName("total_records") val totalRecords: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("current_page") val currentPage: Int,
    val limit: Int,
    val result: List<Member>,
    val message: String

)



@Entity(tableName = "members")
data class Member(
    @PrimaryKey val mobile: Long,
    val name: String,
    val renewal_date: String,
    val email: String,
    val userid: Long,
    var isExpanded: Boolean = false
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(mobile)
        parcel.writeString(name)
        parcel.writeString(renewal_date)
        parcel.writeString(email)
        parcel.writeLong(userid)
        parcel.writeByte(if (isExpanded) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Member> {
        override fun createFromParcel(parcel: Parcel): Member {
            return Member(parcel)
        }

        override fun newArray(size: Int): Array<Member?> {
            return arrayOfNulls(size)
        }
    }
}


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
data class GetMyTrainersResponse(   val success: Boolean,

                                    val message: String,

                                    val result: ArrayList<TrainerModel>)
data class TrainerModel(

    val coachid: Int,

    val name: String,

    val mob: String,
    var isExpanded: Boolean = false

    )