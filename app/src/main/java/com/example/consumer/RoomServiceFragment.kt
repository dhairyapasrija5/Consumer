package com.example.consumer

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

var Price:Long=0
var Count:Long=0
var vw: View? =null

class RoomServiceFragment : Fragment(), Communicator {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var subcategoryList: ArrayList<Subcategory>
    lateinit var MainAdapter: CategoryAdapter
    private lateinit var foodItemList: ArrayList<FoodItem>
    private lateinit var laundryItemList: ArrayList<LaundryItem>
    private lateinit var rentalItemList: ArrayList<RentalItem>
    private lateinit var touristItemList: ArrayList<TouristItem>
    private lateinit var confirmButton: Button
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabaseReference1: DatabaseReference
    private lateinit var listener: ValueEventListener
    private lateinit var itemList: ArrayList<OrderItem>
    var buttonclicked: String = "default"
    lateinit var priceTextView: TextView
    private lateinit var itemTextView: TextView
    private lateinit var cardView: CardView
    private lateinit var messageText:TextView
    private lateinit var orderConfirmation:TextView
    private lateinit var ID:String
    private lateinit var listener2: ValueEventListener
   private lateinit var time:String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        vw= inflater.inflate(R.layout.fragment_room_service, container, false)
        super.onCreate(savedInstanceState)

        val args = arguments
        var bundleList: ArrayList<BundleByOrderFragment>
        bundleList = arrayListOf()
        categoryList = arrayListOf()

        if (args != null) {
            bundleList = args.getParcelableArrayList("items")!!
            categoryList = bundleList[0].categoryList
            buttonclicked = bundleList[0].name.toString()
            time=bundleList[0].time.toString()
            ID=bundleList[0].ID.toString()
        }
        else
            time= LocalTime.now().atOffset(ZoneOffset.of("+02:00")).format(DateTimeFormatter.ofPattern("HH:mm:ss"))


        if(buttonclicked=="default" ||  buttonclicked=="add")
         {
             orderConfirmation= vw?.findViewById(R.id.order_message1)!!
             orderConfirmation.visibility =View.GONE
         }

         if(buttonclicked=="default"){

            val dialog=LoadingDialog(activity)
            dialog.startLoadingDialog()
            var c= Runnable {
                dialog.dismissDialog()
            }
            Handler().postDelayed(c,3000)
        }

        if(buttonclicked=="confirm") {
            Price=0
            Count=0
        }


        if (vw != null) {

            categoryRecyclerView = vw!!.findViewById(R.id.categoryRecyclerView)
            confirmButton = vw!!.findViewById(R.id.confirmRoomService)
            if(Count>0) {
                cardView= vw?.findViewById(R.id.cardView)!!
                cardView.visibility=View.VISIBLE
                priceTextView = vw!!.findViewById(R.id.totalprice)
                priceTextView.text = Price.toString()
                itemTextView = vw!!.findViewById(R.id.noofitems)
                itemTextView.text = Count.toString() + " items"
            }
        }

        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        categoryRecyclerView.setHasFixedSize(true)
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("categories")


        if (buttonclicked == "confirm" ||  buttonclicked=="default") {


            if (vw != null) {
                messageText= vw!!.findViewById(R.id.order_message1)
                messageText.isSelected=true
                messageText.setSingleLine(true)
            }

            listener =
                object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        categoryList = arrayListOf()
                        subcategoryList = arrayListOf()
                        foodItemList = arrayListOf()
                        laundryItemList = arrayListOf()
                        rentalItemList = arrayListOf()
                        touristItemList = arrayListOf()

                        if (snapshot.exists()) {

                            for (category in snapshot.children) {

                                var categoryName = category.key.toString()
                                if (categoryName == "Laundary")
                                    categoryName = "Laundry"
                                subcategoryList = arrayListOf()

                                for (subcategory in category.children) {

                                    val subcategoryName: String = subcategory.key.toString()
                                    foodItemList = arrayListOf()
                                    laundryItemList = arrayListOf()
                                    rentalItemList = arrayListOf()
                                    touristItemList = arrayListOf()

                                    for (item in subcategory.children) {

                                        if (item.child("available").value == true) {


                                            if (categoryName == "Food") {


                                                foodItemList.add(
                                                    FoodItem(
                                                        item.child("name").value.toString(),
                                                        item.child("price").value.toString(),
                                                        item.child("imageURL").value.toString()
                                                    )
                                                )
                                            } else if (categoryName == "Laundry")
                                                laundryItemList.add(
                                                    LaundryItem(
                                                        item.child("name").value.toString(),
                                                        item.child("price").value.toString()
                                                    )
                                                )
                                            else {
                                                if (subcategoryName == "Rentals")
                                                    rentalItemList.add(
                                                        RentalItem(
                                                            item.child("name").value.toString(),
                                                            item.child("price").value.toString()
                                                        )
                                                    )
                                                else
                                                    touristItemList.add(
                                                        TouristItem(
                                                            item.child("name").value.toString(),
                                                            item.child("phoneNo").value.toString()
                                                        )
                                                    )
                                            }
                                        }
                                    }

                                    subcategoryList.add(
                                        Subcategory(
                                            subcategoryName,
                                            foodItemList,
                                            laundryItemList,
                                            rentalItemList,
                                            touristItemList
                                        )
                                    )

                                }

                                categoryList.add(Category(categoryName, subcategoryList, false))
                            }

                            MainAdapter = CategoryAdapter(
                                categoryRecyclerView,
                                requireActivity().application,
                                categoryList
                            )
                            categoryRecyclerView.adapter = MainAdapter



                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }

            mDatabaseReference.addValueEventListener(listener)


            MainAdapter = CategoryAdapter(categoryRecyclerView, requireActivity().application, categoryList)
            categoryRecyclerView.adapter = MainAdapter

        } else {
            MainAdapter = CategoryAdapter(categoryRecyclerView, requireActivity().application, categoryList)
            categoryRecyclerView.adapter = MainAdapter

        }
        return vw
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener2=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists() && buttonclicked=="confirm")
                {

                    if(snapshot.child(ID).child("confirmation").value==true) {
                        orderConfirmation= view?.findViewById(R.id.order_message1) !!
                        orderConfirmation.text  = "Order is confirmed"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        mDatabaseReference1=FirebaseDatabase.getInstance().getReference("Orders")
        mDatabaseReference1.addValueEventListener(listener2)

        confirmButton = view.findViewById(R.id.confirmRoomService)

        confirmButton.setOnClickListener {

            activity?.supportFragmentManager?.beginTransaction()?.apply {
                itemList = arrayListOf()
                var totalPrice: Long = 0
                var count: Long = 0

                for (i in categoryList.indices) {
                    for (j in categoryList[i].subcategoryList.indices) {

                        for (k in categoryList[i].subcategoryList[j].foodItemList.indices) {


                            if (categoryList[i].subcategoryList[j].foodItemList[k].counter > 0) {

                                val itemName: String =
                                    categoryList[i].subcategoryList[j].foodItemList[k].name
                                val itemTotalPrice: Int = categoryList[i].subcategoryList[j].foodItemList[k].price.toInt() *
                                        categoryList[i].subcategoryList[j].foodItemList[k].counter
                                val quantity: Int =
                                    categoryList[i].subcategoryList[j].foodItemList[k].counter
                                itemList.add(OrderItem(itemName, quantity, itemTotalPrice))
                                totalPrice += itemTotalPrice
                                count++
                            }
                        }

                        for (k in categoryList[i].subcategoryList[j].rentalItemList.indices) {

                            if (categoryList[i].subcategoryList[j].rentalItemList[k].counter > 0) {
                                val itemName: String =
                                    categoryList[i].subcategoryList[j].rentalItemList[k].name
                                val itemTotalPrice: Int =
                                    categoryList[i].subcategoryList[j].rentalItemList[k].pricePerDay.toInt() * categoryList[i].subcategoryList[j].rentalItemList[k].counter
                                val quantity: Int =
                                    categoryList[i].subcategoryList[j].rentalItemList[k].counter
                                itemList.add(OrderItem(itemName, quantity, itemTotalPrice))
                                totalPrice += itemTotalPrice
                                count++
                            }

                        }

                        for (k in categoryList[i].subcategoryList[j].laundryItemList.indices) {

                            if (categoryList[i].subcategoryList[j].laundryItemList[k].counter > 0) {
                                val itemName: String =
                                    categoryList[i].subcategoryList[j].laundryItemList[k].name +" ("+categoryList[i].subcategoryList[j].Subcategory+")"
                                val itemTotalPrice: Int =
                                    categoryList[i].subcategoryList[j].laundryItemList[k].price.toInt() * categoryList[i].subcategoryList[j].laundryItemList[k].counter
                                val quantity: Int =
                                    categoryList[i].subcategoryList[j].laundryItemList[k].counter
                                itemList.add(OrderItem(itemName, quantity, itemTotalPrice))
                                totalPrice += itemTotalPrice
                                count++
                            }
                        }
                    }
                }

                var bundleItems: ArrayList<BundleItems>
                bundleItems = arrayListOf()
                bundleItems.add(BundleItems(categoryList, itemList))
                val bundle = Bundle()
                bundle.putParcelableArrayList("items", bundleItems)
                val orderSummaryFragment = OrderSummaryFragment()
                orderSummaryFragment.arguments = bundle
                replace(R.id.RoomService, orderSummaryFragment)
                commit()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (buttonclicked == "confirm" || buttonclicked=="default") {
            mDatabaseReference.removeEventListener(listener)
            mDatabaseReference1.removeEventListener(listener2)
        }
    }

    override fun onDataChanged(price: Long, count: Long)  {

        Price+=price
        Count+=count

        if(Count>0) {

            if (vw != null) {
                cardView= vw!!.findViewById(R.id.cardView)
                cardView.visibility=View.VISIBLE
                priceTextView = vw!!.findViewById(R.id.totalprice)
                priceTextView.text = Price.toString()
                itemTextView = vw!!.findViewById(R.id.noofitems)
                itemTextView.text = Count.toString() + " items"
            }
        }
        else
        {
            cardView= vw?.findViewById(R.id.cardView)!!
            cardView.visibility =View.GONE
        }
    }


}