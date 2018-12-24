package com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.muhammadusama.kotlinrealtimeauction.R

class AuctionItemsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction_items)

        val phone : Button =  findViewById(R.id.phone)
        val laptop : Button = findViewById(R.id.laptop)

        phone.setOnClickListener(this)
        laptop.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.phone->{

                val phoneIntent : Intent = Intent(this@AuctionItemsActivity,AuctionPhoneActivity::class.java)
                startActivity(phoneIntent)
            }

            R.id.laptop->{

                val laptopIntent : Intent = Intent(this@AuctionItemsActivity,AuctionLaptopActivity::class.java)
                startActivity(laptopIntent)

            }
        }
    }

}
