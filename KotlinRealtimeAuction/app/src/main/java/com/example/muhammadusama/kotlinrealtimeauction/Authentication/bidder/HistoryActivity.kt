package com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter.BiddingHistoryAdapter
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Bidder
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class HistoryActivity : AppCompatActivity() {

    //RecycleView,Adapter and List
    private var mBiddingsRecyclerView: RecyclerView? = null
    private var mBiddingAdapter: BiddingHistoryAdapter? = null
    private var biddingList = ArrayList<Bidder>()

    //Firebase Database
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mBidsDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null
    private var mAuth : FirebaseAuth?= null

    private var mUserID : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        //RecyclerView and Adapter
        mBiddingsRecyclerView = findViewById(R.id.recyclerView)
        mBiddingsRecyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mBiddingAdapter = BiddingHistoryAdapter(biddingList)
        mBiddingsRecyclerView?.adapter = mBiddingAdapter

        //Initialize Firebase Components
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mBidsDatabaseReference = mFirebaseDatabase?.getReference()?.child("Bids")
        mAuth = FirebaseAuth.getInstance()
        mUserID = mAuth?.currentUser?.uid

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

       biddingList.clear()
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

                for (postSnapShot : DataSnapshot  in p0.children){

                    var bidding =  postSnapShot.getValue(Bidder::class.java)
                    if(bidding?.userId.equals(mUserID)){

                        biddingList.add(bidding!!)
                        mBiddingAdapter?.notifyDataSetChanged()
                    }
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
        }
        mBidsDatabaseReference?.addChildEventListener(mChildEventListener!!)
    }
}

