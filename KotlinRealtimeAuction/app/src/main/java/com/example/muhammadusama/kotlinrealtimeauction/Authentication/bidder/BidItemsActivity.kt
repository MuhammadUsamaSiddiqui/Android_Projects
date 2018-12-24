package com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.muhammadusama.kotlinrealtimeauction.R

class BidItemsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bid_items)

        val phone: Button = findViewById(R.id.phone)
        val laptop: Button = findViewById(R.id.laptop)

        phone.setOnClickListener(this)
        laptop.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.phone -> {

                val phoneIntent : Intent = Intent(this@BidItemsActivity, BidderViewItemsActivity::class.java)
                phoneIntent.putExtra("Type", "Phone")
                startActivity(phoneIntent)

            }

            R.id.laptop-> {

                val laptopIntent : Intent = Intent(this@BidItemsActivity,BidderViewItemsActivity::class.java)
                laptopIntent.putExtra("Type","Laptop")
                startActivity(laptopIntent)
            }
        }
    }
}
