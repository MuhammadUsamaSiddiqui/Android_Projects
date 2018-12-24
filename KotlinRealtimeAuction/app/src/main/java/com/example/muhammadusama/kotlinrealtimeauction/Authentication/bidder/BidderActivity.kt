package com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder

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

class BidderActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bidder)

        val bidItem : Button = findViewById(R.id.bidItem)
        val history : Button = findViewById(R.id.history)
        bidItem.setOnClickListener(this)
        history.setOnClickListener(this)
    }


    override fun onClick(v: View?) {

        when(v?.id) {

            R.id.bidItem->{

                val bidItemIntent = Intent (this@BidderActivity,BidItemsActivity::class.java)
                startActivity(bidItemIntent)
            }

            R.id.history->{

                val historyIntent = Intent (this@BidderActivity,HistoryActivity::class.java)
                startActivity(historyIntent)
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
                startActivity(Intent(this@BidderActivity, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }
}
