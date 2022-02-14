package com.example.consumer

import java.util.*
import kotlin.collections.ArrayList

data class Subcategory(var Subcategory: String,var foodItemList: ArrayList<FoodItem>,var laundryItemList:ArrayList<LaundryItem>
,var rentalItemList:ArrayList<RentalItem>,var touristItemList:ArrayList<TouristItem>,var visibility:Boolean=false)

