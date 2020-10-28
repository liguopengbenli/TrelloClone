package com.lig.projemanage.models

import android.os.Parcel
import android.os.Parcelable


data class User(
    val id: String = "", // important to define pre value = no-argument constructor
    val name: String = "",
    val email: String= "",
    val image: String="",
    val mobile: Long = 0,
    val fcmToken: String = "",
    var selected: Boolean = false

): Parcelable { // settings plugn market - install parcelable for kotlin
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!
        //parcel.readBoolean()
    )


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(email)
        writeString(image)
        writeLong(mobile)
        writeString(fcmToken)
        //writeBoolean(selected)

    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}