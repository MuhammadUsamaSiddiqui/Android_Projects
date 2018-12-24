package com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter.LaptopAdapter
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter.PhoneAdapter
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Laptop
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Phone
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AuctioneerPastItemsActivity : AppCompatActivity() {

    //RecycleView,Adapter and List
    private var phoneList = ArrayList<Phone>()
    private var laptopList = ArrayList<Laptop>()
    private var mItemsRecyclerView : RecyclerView? = null
    private var mPhoneAdapter : PhoneAdapter? = null
    private var mLaptopAdapter : LaptopAdapter? = null

    //Firebase Database
    private  var mFirebaseDatabase : FirebaseDatabase? = null
    private  var mItemDatabaseReference : DatabaseReference? = null
    private var mChildEventListener : ChildEventListener? = null

    private var type : String? = ""

    private var dateFormat: SimpleDateFormat? = null
    private var timeFormat: SimpleDateFormat? = null

    private var currentDate : String? = ""
    private var currentTime : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        //Date and Time Formats
        dateFormat = SimpleDateFormat("dd/MM/yyyy")
        timeFormat = SimpleDateFormat("HH:mm")

        currentTime = timeFormat?.format(Calendar.getInstance().time)
        currentDate = dateFormat?.format(Date())


        type  = intent.extras.getString("Type")

        //RecyclerView and Adapter
        mItemsRecyclerView = findViewById(R.id.recyclerView)
        mItemsRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        //Initialize Firebase Components
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        if(type.equals("Phone")){

            mItemDatabaseReference = mFirebaseDatabase?.getReference()?.child("Phone")
            mPhoneAdapter = PhoneAdapter(phoneList){

                val showBiddersIntent = Intent(this@AuctioneerPastItemsActivity,ShowBiddersActivity::class.java)
                showBiddersIntent.putExtra("Product ID",it.pushKey)
                startActivity(showBiddersIntent)
            }
            mItemsRecyclerView?.adapter = mPhoneAdapter

        }else{

            mItemDatabaseReference = mFirebaseDatabase?.getReference()?.child("Laptop")
            mLaptopAdapter = LaptopAdapter(laptopList){

                val showBiddersIntent = Intent(this@AuctioneerPastItemsActivity,ShowBiddersActivity::class.java)
                showBiddersIntent.putExtra("Product ID",it.pushKey)
                startActivity(showBiddersIntent)
            }
            mItemsRecyclerView?.adapter = mLaptopAdapter

        }
    }

    override fun onResume() {
        super.onResume()

        attachDatabaseReadListener()
    }

    override fun onPause() {
        super.onPause()

        if (mChildEventListener != null){

            mItemDatabaseReference?.removeEventListener(mChildEventListener!!)
        }

        if(type.equals("Phone")){

            phoneList.clear()
        }else{

            laptopList.clear()
        }

    }
    private fun attachDatabaseReadListener() {

        mChildEventListener = object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if  (type.equals("Phone")) {

                    val currentPhone = p0.getValue(Phone::class.java)

                    if (currentDate?.compareTo(currentPhone?.date!!)!! > 0 ||
                            (currentDate?.equals(currentPhone?.date)!!&&
                                    currentTime?.compareTo(currentPhone?.endTime!!)!! > 0 )) {

                        phoneList.add(currentPhone!!)
                        mPhoneAdapter?.notifyDataSetChanged()

                    }

                } else {

                    val currentLaptop = p0.getValue(Laptop::class.java)

                    if (currentDate?.compareTo(currentLaptop?.date!!)!! > 0 ||
                            (currentDate?.equals(currentLaptop?.date)!! &&
                                    currentTime?.compareTo(currentLaptop?.endTime!!)!! > 0 )) {

                        laptopList.add(currentLaptop!!)
                        mLaptopAdapter?.notifyDataSetChanged()
                    }
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }

        mItemDatabaseReference?.addChildEventListener(mChildEventListener!!)
    }
}
