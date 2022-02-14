package com.example.consumer

import java.util.*
import kotlin.collections.ArrayList

data class Orders(var roomID:String, var date:String, var time: String, var items:ArrayList<OrderItem>, var billingPrice:Long, var confirmation: Boolean )
