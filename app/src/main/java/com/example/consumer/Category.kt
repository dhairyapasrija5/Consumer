package com.example.consumer

import android.os.Parcel
import android.os.Parcelable

data class Category(
    var Category: String?,
    var subcategoryList: ArrayList<Subcategory>,
    var subcategoryVisibilty: Boolean = false
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        TODO("subcategoryList"),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Category)
        parcel.writeByte(if (subcategoryVisibilty) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}
