package com.example.consumer

import android.os.Parcel
import android.os.Parcelable

data class OrderItem(var name: String?, var quantity: Int, var totalItemPrice: Int ) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(quantity)
        parcel.writeInt(totalItemPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }
}
