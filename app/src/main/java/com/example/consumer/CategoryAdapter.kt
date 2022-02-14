package com.example.consumer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class CategoryAdapter(
    private val mainrecyclerview: RecyclerView,
    private val context: Context,
    private val categoryList: ArrayList<Category>
) :
    RecyclerView.Adapter<CategoryAdapter.MainViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val currentItem = categoryList[position]

        var subcategoryVisibility: Boolean = currentItem.subcategoryVisibilty
        holder.category.text = currentItem.Category
        holder.subcategoryRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.subcategoryRecyclerView.setHasFixedSize(true)
        holder.subcategoryRecyclerView.adapter = currentItem.Category?.let {
            SubcategoryAdapter(
                it,
                mainrecyclerview,
                position,
                context,
                currentItem.subcategoryList
            )
        }


        if (subcategoryVisibility) {

            holder.subcategoryLayout.visibility = View.VISIBLE
            holder.underline.visibility = View.GONE
            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)

        } else {
            holder.subcategoryLayout.visibility = View.GONE
            holder.underline.visibility = View.VISIBLE
            holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        }

        holder.categoryLayout.setOnClickListener {

            currentItem.subcategoryVisibilty = !currentItem.subcategoryVisibilty
            if(currentItem.subcategoryVisibilty) {

                holder.subcategoryLayout.visibility = View.VISIBLE
                holder.underline.visibility = View.GONE
                holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else{
                holder.subcategoryLayout.visibility = View.GONE
                holder.underline.visibility = View.VISIBLE
                holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
           // notifyItemChanged(position)

        }

    }

    override fun getItemCount(): Int {

        return categoryList.size
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val categoryLayout: RelativeLayout = itemView.findViewById(R.id.CategoryLayout)
        val category: TextView = itemView.findViewById(R.id.order_food)
        val subcategoryRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerview)
        val subcategoryLayout: ConstraintLayout =
            itemView.findViewById(R.id.foodSubcategory_expandableLayout)
        val underline: View = itemView.findViewById(R.id.underline_main)
        val arrow: ImageView = itemView.findViewById(R.id.categoryArrow)

    }

}
