package com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.example.muhammadusama.kotlinrealtimeauction.LoginActivity
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.google.firebase.auth.FirebaseAuth

class AuctioneerActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auctioneer)

        val auctionItem : Button = findViewById(R.id.auctionItem)
        val viewItems : Button = findViewById(R.id.viewItems)

        auctionItem.setOnClickListener(this)
        viewItems.setOnClickListener(this)
    }


    override fun onClick(v: View?) {

        when(v?.id){

            R.id.auctionItem ->{

                val auctionItemIntent = Intent(this@AuctioneerActivity,AuctionItemsActivity::class.java)
                startActivity(auctionItemIntent)
            }

            R.id.viewItems ->{

                val viewItemsIntent = Intent (this@AuctioneerActivity,AuctioneerItemTypeActivity::class.java)
                startActivity(viewItemsIntent)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){

            R.id.menuLogout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@AuctioneerActivity,LoginActivity::class.java))
                finish()
            }
        }
        return true
    }
}
