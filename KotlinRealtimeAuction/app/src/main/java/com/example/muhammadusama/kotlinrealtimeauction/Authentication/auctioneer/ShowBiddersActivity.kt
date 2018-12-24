package com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter.BidderAdapter
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Bidder
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.google.firebase.database.*
import java.util.ArrayList

class ShowBiddersActivity : AppCompatActivity() {

    //RecycleView,Adapter and List
    private var mBiddersRecyclerView: RecyclerView? = null
    private var mBidderAdapter: BidderAdapter? = null
    private var bidderList = ArrayList<Bidder>()

    //Firebase Database
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mBidsDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null

    private var productID: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        productID = intent.extras.getString("Product ID")

        //RecyclerView and Adapter
        mBiddersRecyclerView = findViewById(R.id.recyclerView)
        mBiddersRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mBidderAdapter = BidderAdapter(bidderList)
        mBiddersRecyclerView?.adapter = mBidderAdapter

        //Initialize Firebase Components
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mBidsDatabaseReference = mFirebaseDatabase?.getReference()?.child("Bids")?.child(productID!!)

    }

    override fun onResume() {
        super.onResume()
        attachDatabaseReadListener()
    }

    override fun onPause() {
        super.onPause()

        if (mChildEventListener != null) {

            mBidsDatabaseReference?.removeEventListener(mChildEventListener!!)
        }

        bidderList.clear()
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

                if(!p0.exists()){
                    return
                }
                val bidder = p0.getValue(Bidder::class.java)

                    bidderList.add(bidder!!)
                    mBidderAdapter?.notifyDataSetChanged()
                }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }

        mBidsDatabaseReference?.addChildEventListener(mChildEventListener!!)
    }

}

