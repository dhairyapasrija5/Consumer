package com.example.consumer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class OrderAdapter(private val orderList: ArrayList<OrderItem>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView: View
        itemView=layoutInflater.inflate(R.layout.order_item,parent,false)
        return OrderViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        holder.orderName.text=orderList[position].name
        holder.price.text="\u20B9"+ orderList[position].totalItemPrice.toString()
        holder.quantity.text=orderList[position].quantity.toString()

    }

    override fun getItemCount(): Int {

        return orderList.size
    }

   class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

       var orderName:TextView=itemView.findViewById(R.id.order_name)
       var price:TextView=itemView.findViewById(R.id.order_price)
       var quantity:TextView=itemView.findViewById(R.id.quantity)
   }

}