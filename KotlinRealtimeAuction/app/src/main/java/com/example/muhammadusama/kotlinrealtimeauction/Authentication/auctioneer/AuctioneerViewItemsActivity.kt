package com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.muhammadusama.kotlinrealtimeauction.R

class AuctioneerViewItemsActivity : AppCompatActivity(), View.OnClickListener {

    private var type: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auctioneer_view_items)

        type  = intent.extras.getString("Type")

        val liveItemsButton : Button = findViewById(R.id.liveItems)
        val pastItemsButton : Button = findViewById(R.id.pastItems)
        val upcomingItemsButton : Button = findViewById(R.id.upcomingItems)

        liveItemsButton.setOnClickListener(this)
        pastItemsButton.setOnClickListener(this)
        upcomingItemsButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.liveItems ->{

                val liveItemsIntent = Intent(this@AuctioneerViewItemsActivity,AuctioneerLiveItemsActivity::class.java)
                liveItemsIntent.putExtra("Type",type)
                startActivity(liveItemsIntent)
            }

            R.id.pastItems->{
                val pastItemsIntent = Intent (this@AuctioneerViewItemsActivity,AuctioneerPastItemsActivity::class.java)
                pastItemsIntent.putExtra("Type",type)
                startActivity(pastItemsIntent)
            }

            R.id.upcomingItems->{
                val upcomingItemsIntent = Intent (this@AuctioneerViewItemsActivity,AuctioneerUpcomingItemsActivity::class.java)
                upcomingItemsIntent.putExtra("Type",type)
                startActivity(upcomingItemsIntent)
            }
        }
    }

}
