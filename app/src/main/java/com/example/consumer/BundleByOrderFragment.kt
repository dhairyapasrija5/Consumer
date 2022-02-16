package com.example.consumer

import android.os.Parcel
import android.os.Parcelable

data class BundleByOrderFragment(var name: String?, var categoryList:ArrayList<Category>, var time: String?, var ID: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        TODO("categoryList"),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(time)
        parcel.writeString(ID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BundleByOrderFragment> {
        override fun createFromParcel(parcel: Parcel): BundleByOrderFragment {
            return BundleByOrderFragment(parcel)
        }

        override fun newArray(size: Int): Array<BundleByOrderFragment?> {
            return arrayOfNulls(size)
        }
    }
}