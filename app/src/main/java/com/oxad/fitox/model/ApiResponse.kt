package com.oxad.fitox.model

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.semantics.Role
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ApiResponse(val s:String)
data class UserRegisterModel(
    val success: Boolean,
    val message: String,
    val isAdmin: Int,
    val mobile: Long = 0,
    val userid: Long,
    val pass: String,
    val ref_id: String,
    val login_url: String




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
    val referral_id: String? = "",
    var isExpanded: Boolean = false,
    val height: Float = 0f,
    val weight: Float = 0f,
    val dob: String,
    val coachid: Long,
    val goal: Int = 1,
    val gender: String? = "Male",
    val referred_by: String? = "",
    val due_amount: Int = 0,
    val attendance_count: Int = 0,
    val last_attend: String,


    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",

        parcel.readString() ?: "",

        parcel.readString() ?: "",

        parcel.readLong(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString() ?: "",

        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(mobile)
        parcel.writeString(name)
        parcel.writeString(renewal_date)
        parcel.writeString(email)
        parcel.writeLong(userid)
        parcel.writeString(referral_id)
        parcel.writeByte(if (isExpanded) 1 else 0)
        parcel.writeFloat(height)
        parcel.writeFloat(weight)
        parcel.writeString(dob)
        parcel.writeLong(coachid)
        parcel.writeInt(goal)
        parcel.writeString(gender)
        parcel.writeString(referred_by)
        parcel.writeInt(due_amount)
        parcel.writeInt(attendance_count)
        parcel.writeString(last_attend)


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
data class CustResponse(
    val success: Boolean,
    val result: CustResData,
    val message: String,
    val lgin_url:String


)
data class CustResData(
    val name: String,
    val userid: Int,
    val password: String,
    val referral_id: String,

    )
data class ProductResponse(
    val success: Boolean,
    val result: List<Product>,
    val message: String

)

data class ProductRes(
    val productId: Int,
    val productName: String,
    val pic: String,
    val offerPrice: Int,
    val actualPrice: Int
)
data class MyClient(

    val name: String,
    val mobile: Long,
    val renewal_date: String

)
data class AdminLoginModel(

    val success: Boolean,

    val message: String,
    val a_key: String,

    val result: ArrayList<LoginResult>

) : Parcelable {

    constructor(parcel: Parcel) : this(

        parcel.readByte() != 0.toByte(),

        parcel.readString() ?: "",
        parcel.readString() ?: "",

        parcel.createTypedArrayList(LoginResult.CREATOR) ?: arrayListOf()

    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeByte(if (success) 1 else 0)

        parcel.writeString(message)
        parcel.writeString(a_key)

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
    val membercount: Int,
    val renewaldate: String,
    val mob: String,
    val admin_cont: String

) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",


        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Id)
        parcel.writeString(GymName)
        parcel.writeString(Location)
        parcel.writeInt(GymId)
        parcel.writeInt(membercount)
        parcel.writeString(renewaldate)
        parcel.writeString(mob)
        parcel.writeString(admin_cont)

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
data class GymGuideResponse(
    val success: Boolean,
    val result: List<Workout>,
    val message: String

)
@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey val id: Int,
    val name: String,
    val category: String,
    val description: String,
    val muscle_group: String,
    val difficulty: String,
    val duration: Int
)
data class GetMyTransacHist(   val success: Boolean,
                              val total_pages: Int,

                                    val message: String,

                                    val result: ArrayList<Transaction>)
data class Transaction(
    val id: Int,
    val name: String,
    val amount: Int,
    val date: String,
    val type: Int
)
data class GetMyTrainersResponse(   val success: Boolean,

                                    val message: String,

                                    val result: ArrayList<TrainerModel>)
data class TrainerModel(

    val coachid: Long,

    val name: String,

    val mob: String,
    val role: String,
    val password: String,

    var isExpanded: Boolean = false

    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",

        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(coachid)
        parcel.writeString(name)
        parcel.writeString(mob)
        parcel.writeString(role)
        parcel.writeString(password)

        parcel.writeByte(if (isExpanded) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TrainerModel> {
        override fun createFromParcel(parcel: Parcel): TrainerModel {
            return TrainerModel(parcel)
        }

        override fun newArray(size: Int): Array<TrainerModel?> {
            return arrayOfNulls(size)
        }
    }

}