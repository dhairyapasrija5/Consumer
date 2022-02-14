package com.example.consumer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SubcategoryAdapter(
    private val category: String,
    private val mainRecyclerview: RecyclerView,
    private val parentPosition: Int,
    private val context: Context,
    private val subcategoryItems: ArrayList<Subcategory>
) :
    RecyclerView.Adapter<SubcategoryAdapter.MyViewHolder>() {

    lateinit var foodItemList:ArrayList<FoodItem>
    private lateinit var laundryItemList:ArrayList<LaundryItem>
    private lateinit var rentalItemList:ArrayList<RentalItem>
    private lateinit var touristItemList:ArrayList<TouristItem>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.subcategory_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val currentItem = subcategoryItems[position]
        holder.subcategory.text = currentItem.Subcategory
        val visibility: Boolean = currentItem.visibility

        foodItemList= arrayListOf()
        laundryItemList= arrayListOf()
        rentalItemList= arrayListOf()
        touristItemList= arrayListOf()

        if (category == "Food") {

            foodItemList=currentItem.foodItemList
        } else if (category == "Laundry") {


              laundryItemList=currentItem.laundryItemList

        } else {

            if (currentItem.Subcategory == "Rentals") {

                   rentalItemList=currentItem.rentalItemList

            } else {

                    touristItemList=currentItem.touristItemList

            }
        }

        holder.itemRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.itemRecyclerView.setHasFixedSize(true)
        holder.itemRecyclerView.adapter = ItemAdapter(category, currentItem.Subcategory, foodItemList,laundryItemList,rentalItemList,touristItemList)


        if (visibility) {
            holder.constraintLayout2.visibility = View.VISIBLE
            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)

        } else {
            holder.constraintLayout2.visibility = View.GONE
            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }

        holder.constraintLayout1.setOnClickListener {

            currentItem.visibility = !currentItem.visibility
            if(currentItem.visibility)
            {
                holder.constraintLayout2.visibility=View.VISIBLE
                holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else {
                holder.constraintLayout2.visibility = View.GONE
                holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
             // notifyItemChanged(position)
            // mainRecyclerview.adapter?.notifyItemChanged(parentPosition)
        }

    }

    override fun getItemCount(): Int {

        return subcategoryItems.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subcategory: TextView = itemView.findViewById(R.id.subcategory)
        val itemRecyclerView: RecyclerView = itemView.findViewById(R.id.item_recycler_view)
        val constraintLayout1: ConstraintLayout = itemView.findViewById(R.id.constraint_layout)
        val constraintLayout2: LinearLayout = itemView.findViewById(R.id.item_layout)
        val arrow: ImageView = itemView.findViewById(R.id.arrow)
    }

}