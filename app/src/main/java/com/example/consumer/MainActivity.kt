package com.example.consumer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      supportFragmentManager.beginTransaction().apply {
          replace(R.id.RoomService,RoomServiceFragment())
          commit()
      }

//        categoryRecyclerView = findViewById(R.id.categoryRecyclerView)
//        categoryRecyclerView.layoutManager = LinearLayoutManager(this)
//        categoryRecyclerView.setHasFixedSize(true)
//        categoryList = arrayListOf<Category>()
//
//        FirebaseDatabase.getInstance().getReference("categories")
//            .addValueEventListener(object : ValueEventListener {
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    categoryList= arrayListOf()
//                    subcategoryList= arrayListOf()
//                    foodItemList= arrayListOf()
//                    laundryItemList= arrayListOf()
//                    rentalItemList= arrayListOf()
//                    touristItemList= arrayListOf()
//
//                    if (snapshot.exists()) {
//
//                        for (category in snapshot.children) {
//
//                            var categoryName = category.key.toString()
//                            if (categoryName == "Laundary")
//                                categoryName = "Laundry"
//                                subcategoryList = arrayListOf()
//
//                            for (subcategory in category.children) {
//
//                                var subcategoryName:String=subcategory.key.toString()
//                                    foodItemList= arrayListOf()
//                                    laundryItemList= arrayListOf()
//                                    rentalItemList= arrayListOf()
//                                    touristItemList= arrayListOf()
//
//                                for(item in subcategory.children){
//
//                                    if(categoryName=="Food")
//                                        foodItemList.add(FoodItem(item.child("name").value.toString(),
//                                            item.child("price").value as Long
//                                        ))
//                                    else if(categoryName=="Laundry")
//                                        laundryItemList.add(LaundryItem(item.child("name").value.toString(),
//                                            item.child("price").value as Long
//                                        ))
//                                    else
//                                    {
//                                        if(subcategoryName=="Rentals")
//                                            rentalItemList.add(RentalItem(item.child("name").value.toString(),
//                                                item.child("price").value as Long
//                                            ))
//                                        else
//                                            touristItemList.add(TouristItem(item.child("name").value.toString(),item.child("phoneNo").value.toString()))
//                                    }
//                                }
//
//                                subcategoryList.add(Subcategory(subcategoryName,foodItemList,laundryItemList,rentalItemList,touristItemList))
//
//                            }
//
//                            categoryList.add(Category(categoryName,subcategoryList, false))
//
//                        }
//
//                        MainAdapter = CategoryAdapter(categoryRecyclerView, applicationContext, categoryList)
//                        categoryRecyclerView.adapter = MainAdapter
//
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//
    }

}