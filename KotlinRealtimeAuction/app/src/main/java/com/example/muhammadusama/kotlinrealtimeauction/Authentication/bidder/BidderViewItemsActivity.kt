package com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer.AuctioneerLiveItemsActivity
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer.AuctioneerUpcomingItemsActivity
import com.example.muhammadusama.kotlinrealtimeauction.R

class BidderViewItemsActivity : AppCompatActivity(), View.OnClickListener {

    private var type: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bidder_view_items)

        type  = intent.extras.getString("Type")

        val liveItemsButton : Button = findViewById(R.id.liveItems)
        val upcomingItemsButton : Button = findViewById(R.id.upcomingItems)

        liveItemsButton.setOnClickListener(this)
        upcomingItemsButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.liveItems->{
                val liveItemsIntent = Intent(this@BidderViewItemsActivity, BidderLiveItemsActivity::class.java)
                liveItemsIntent.putExtra("Type",type)
                liveItemsIntent.putExtra("Period","Live")
                startActivity(liveItemsIntent)

            }

            R.id.upcomingItems-> {
                val upcomingItemsIntent = Intent (this@BidderViewItemsActivity,BidderUpcomingItemsActivity::class.java)
                upcomingItemsIntent.putExtra("Type",type)
                upcomingItemsIntent.putExtra("Period","Upcoming")
                startActivity(upcomingItemsIntent)

            }
        }
    }

}
