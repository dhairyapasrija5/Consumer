package com.example.consumer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.lang.Exception

class ItemAdapter(
    private val category: String,
    private val subcategory: String,
    private val foodItemList: ArrayList<FoodItem>,
    private val laundryItemList: ArrayList<LaundryItem>,
    private val rentalItemList: ArrayList<RentalItem>,
    private val touristItemList: ArrayList<TouristItem>,
    private val communicator: RoomServiceFragment = RoomServiceFragment()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var price: Long = 0
    private var _communicator: Communicator? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        _communicator = communicator
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView: View
        if (viewType == 0) {
            itemView = layoutInflater.inflate(R.layout.food_item, parent, false)
            return FoodItemViewHolder(itemView, communicator)
        } else if (viewType == 1) {
            itemView = layoutInflater.inflate(R.layout.laundry_item, parent, false)
            return LaundryItemViewHolder(itemView, communicator)
        } else if (viewType == 2) {
            itemView = layoutInflater.inflate(R.layout.rental_item, parent, false)
            return RentalItemViewHolder(itemView, communicator)
        } else {
            itemView = layoutInflater.inflate(R.layout.tourist_item, parent, false)
            return TouristItemViewHolder(itemView, communicator)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =

        if (category == "Food") {
            var holder1: FoodItemViewHolder = holder as FoodItemViewHolder
            //holder1.foodImage.setImageResource(foodItemList[position].image)

            holder1.foodPrice.text = "\u20B9" + foodItemList[position].price
            holder1.foodName.text = foodItemList[position].name
            holder1.counterFoodView.text = foodItemList[position].counter.toString()


            holder1.foodImage.setImageResource(R.drawable.food_background)
            try {

                var url:String
                if(foodItemList[position].imageID.equals(""))
                    url="Thali Image"
                else
                    url=foodItemList[position].imageID

                val localFile: File = File.createTempFile(url, "jpeg")
                var storageRef:StorageReference=FirebaseStorage.getInstance().getReference().child(""+
                        url+".jpeg")
                storageRef.getFile(localFile)
                    .addOnSuccessListener ( object: OnSuccessListener<FileDownloadTask.TaskSnapshot>{
                        override fun onSuccess(p0: FileDownloadTask.TaskSnapshot?) {

                            var bitmap:Bitmap=BitmapFactory.decodeFile(localFile.absolutePath)
                            holder1.foodImage.setImageBitmap(bitmap)
                            Log.d("here","true")
                        }

                    }).addOnFailureListener(object: OnFailureListener{
                        override fun onFailure(p0: Exception) {
                            holder1.foodImage.setImageResource(R.drawable.north)
                        }

                    })

            }
            catch (e:IOException)
            {
                e.printStackTrace()
            }

            holder1.addFoodItem.setOnClickListener {

                foodItemList[position].counter++
                price = foodItemList[position].price.toLong()
                _communicator?.onDataChanged(price, 1)
                holder1.counterFoodView.text=foodItemList[position].counter.toString()
            }

            holder1.removeFoodItem.setOnClickListener {

                if (!foodItemList[position].counter.toString().equals("0")) {
                    foodItemList[position].counter--
                    price = foodItemList[position].price.toLong()
                    _communicator?.onDataChanged(-price, -1)
                    holder1.counterFoodView.text=foodItemList[position].counter.toString()
                }
            }

        } else if (category == "Laundry") {

            var holder2: LaundryItemViewHolder = holder as LaundryItemViewHolder
            holder2.itemName.text = laundryItemList[position].name
            holder2.price.text = "\u20B9" + laundryItemList[position].price.toString()
            holder2.counterLaundryView.text = laundryItemList[position].counter.toString()

            holder2.addLaundryItem.setOnClickListener {

                price = laundryItemList[position].price.toLong()
                laundryItemList[position].counter++
                _communicator?.onDataChanged(price, 1)
                notifyDataSetChanged()
            }
            holder2.removeLaundryItem.setOnClickListener {

                if (!laundryItemList[position].counter.toString().equals("0")) {
                    laundryItemList[position].counter--
                    price = laundryItemList[position].price.toLong()
                    _communicator?.onDataChanged(-price, -1)
                    notifyDataSetChanged()
                }
            }

        } else {

            if (subcategory == "Rentals") {
                var holder3: RentalItemViewHolder = holder as RentalItemViewHolder
                holder3.itemName.text = rentalItemList[position].name
                holder3.price.text =
                    "\u20B9" + rentalItemList[position].pricePerDay.toString() + " / day"

                holder3.counterRentalView.text = rentalItemList[position].counter.toString()

                holder3.addRentalItem.setOnClickListener {

                    price = rentalItemList[position].pricePerDay.toLong()
                    rentalItemList[position].counter++
                    _communicator?.onDataChanged(price, 1)
                    notifyDataSetChanged()
                }
                holder3.removeRentalItem.setOnClickListener {

                    if (!rentalItemList[position].counter.toString().equals("0")) {
                        rentalItemList[position].counter--
                        price = rentalItemList[position].pricePerDay.toLong()
                        _communicator?.onDataChanged(-price, -1)
                        notifyDataSetChanged()
                    }
                }

            } else {
                var holder4: TouristItemViewHolder = holder as TouristItemViewHolder
                holder4.personName.text = touristItemList[position].name

            }
        }

    override fun getItemCount(): Int {

        if (category == "Food")
            return foodItemList.size
        else if (category == "Laundry")
            return laundryItemList.size
        else
            if (subcategory == "Rentals")
                return rentalItemList.size
            else
                return touristItemList.size
    }

    override fun getItemViewType(position: Int): Int {

        if (category.lowercase() == "food")
            return 0
        else if (category.lowercase() == "laundry")
            return 1
        else if (subcategory.lowercase() == "rentals")
            return 2
        else
            return 3
    }

    class FoodItemViewHolder(itemView: View, communicator: Communicator) :
        RecyclerView.ViewHolder(itemView) {
        var _communicator: Communicator = communicator
        var foodImage: ShapeableImageView = itemView.findViewById(R.id.food_Image)
        var foodName: TextView = itemView.findViewById(R.id.food_item)
        var foodPrice: TextView = itemView.findViewById(R.id.food_Price)
        var counterFoodView: TextView = itemView.findViewById(R.id.counterFoodItem)
        var addFoodItem: TextView = itemView.findViewById(R.id.increaseFoodItem)
        var removeFoodItem: TextView = itemView.findViewById(R.id.decreaseFoodItem)
    }

    class LaundryItemViewHolder(itemView: View, communicator: Communicator) :
        RecyclerView.ViewHolder(itemView) {
        var _communicator: Communicator = communicator
        var itemName: TextView = itemView.findViewById(R.id.laundry_item)
        var price: TextView = itemView.findViewById(R.id.laundry_price)
        var counterLaundryView: TextView = itemView.findViewById(R.id.counterLaundryItem)
        var addLaundryItem: TextView = itemView.findViewById(R.id.increaseLaundryItem)
        var removeLaundryItem: TextView = itemView.findViewById(R.id.decreaseLaundryItem)

    }

    class RentalItemViewHolder(itemView: View, communicator: Communicator) :
        RecyclerView.ViewHolder(itemView) {

        var _communicator: Communicator = communicator
        var itemName: TextView = itemView.findViewById(R.id.rental_item)
        var price: TextView = itemView.findViewById(R.id.rental_price)
        var counterRentalView: TextView = itemView.findViewById(R.id.counterRentalItem)
        var addRentalItem: TextView = itemView.findViewById(R.id.increaseRentalItem)
        var removeRentalItem: TextView = itemView.findViewById(R.id.decreaseRentalItem)

    }

    class TouristItemViewHolder(itemView: View, communicator: Communicator) :
        RecyclerView.ViewHolder(itemView) {

        var personName: TextView = itemView.findViewById(R.id.person_name)
    }


}