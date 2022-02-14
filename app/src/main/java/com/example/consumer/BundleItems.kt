package com.example.consumer

import android.os.Parcel
import android.os.Parcelable

data class BundleItems(var categorylist:ArrayList<Category>, var itemList:ArrayList<OrderItem>):Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("categorylist"),
        TODO("itemList")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BundleItems> {
        override fun createFromParcel(parcel: Parcel): BundleItems {
            return BundleItems(parcel)
        }

        override fun newArray(size: Int): Array<BundleItems?> {
            return arrayOfNulls(size)
        }
    }
}
