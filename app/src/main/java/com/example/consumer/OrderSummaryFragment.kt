package com.example.consumer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


class OrderSummaryFragment : Fragment() {

    private lateinit var orderSummaryRecyclerView: RecyclerView
    private lateinit var orderList: ArrayList<Orders>
    private lateinit var total: TextView
    private lateinit var confirmButton: Button
    private lateinit var items: ArrayList<OrderItem>
    private lateinit var addMoreButton: Button
    private lateinit var bundleItems: ArrayList<BundleItems>
    private lateinit var ID:String
    private lateinit var currentTime:String
    private lateinit var db:DatabaseReference
    private lateinit var listener:ValueEventListener
    private var confirmation:Boolean=false

    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = arguments
        items = arrayListOf()
        bundleItems= arrayListOf()

        if (args != null) {
            bundleItems = args.getParcelableArrayList<BundleItems>("items")!!
            Log.d("order",bundleItems[0].categorylist.size.toString())
            items=bundleItems[0].itemList
        }

        val view: View? = inflater.inflate(R.layout.fragment_order_summary, container, false)
        super.onCreate(savedInstanceState)

        if (view != null) {

            orderSummaryRecyclerView = view.findViewById(R.id.orderSummaryRecyclerView)
            total = view.findViewById(R.id.total)
            total.ellipsize=TextUtils.TruncateAt.MARQUEE
            total.isSelected=true
            confirmButton = view.findViewById(R.id.confirmOrder)
            addMoreButton=view.findViewById(R.id.add_more)
        }

       var billingPrice:Long= 0

        for (i in items.indices)
            billingPrice += items[i].totalItemPrice

        total.text = "\u20B9" + billingPrice.toString()

        orderList = arrayListOf()


        val nowUtc: Instant = Instant.now()
        val asiaSingapore: ZoneId = ZoneId.of("Asia/Calcutta")
        val nowAsiaSingapore: ZonedDateTime = ZonedDateTime.ofInstant(nowUtc, asiaSingapore)
        Log.d("time",nowAsiaSingapore.toString())
        var currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        currentTime = LocalTime.now().atOffset(ZoneOffset.of("+02:00")).format(DateTimeFormatter.ofPattern("HH:mm:ss"))

         ID = Settings.Secure.getString(
            requireActivity().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        orderList.add(
            Orders(
                "100",
                currentDate,
                currentTime.toString(),
                items,
                billingPrice,
                false
            )
        )

        orderSummaryRecyclerView.layoutManager = LinearLayoutManager(context)
        orderSummaryRecyclerView.setHasFixedSize(true)
        orderSummaryRecyclerView.adapter = OrderAdapter(items)

        db = FirebaseDatabase.getInstance().getReference("Orders")

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.child(ID).child("confirmation").value == false) {

                    confirmation=false
                } else {

                    confirmation=true
                    }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.addValueEventListener(listener)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton.setOnClickListener {

            if(confirmation==false){
                Toast.makeText(context,
                        "Another Order Already in Progress", Toast.LENGTH_LONG
                    ).show()

            }
            else {

                Toast.makeText(context, "Order Placed", Toast.LENGTH_LONG).show()

                    for (i in orderList.indices) {
                        FirebaseDatabase.getInstance().getReference("Orders").child(ID)
                            .setValue(orderList[i])
                    }

                    db.removeEventListener(listener)
                    Commit("confirm")


            }
            Log.d("confirmation",confirmation.toString())
        }

        addMoreButton.setOnClickListener {
            Commit("add")
        }
    }

    fun Commit(msg:String){

        activity?.supportFragmentManager?.beginTransaction()?.apply {

            if(msg=="confirm")
            bundleItems[0].categorylist = arrayListOf()

            var bundle = Bundle()
            var bundleList: ArrayList<BundleByOrderFragment>
            bundleList = arrayListOf()
            var time: String

            if (orderList.size > 0)
                time = orderList[0].time
            else
                time = "abc"

            bundleList.add(BundleByOrderFragment(msg, bundleItems[0].categorylist, time))
            bundle.putParcelableArrayList("items", bundleList)
            var roomServiceFragment = RoomServiceFragment()
            roomServiceFragment.arguments = bundle
            replace(R.id.RoomService, roomServiceFragment)
            commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.removeEventListener(listener)
    }

}