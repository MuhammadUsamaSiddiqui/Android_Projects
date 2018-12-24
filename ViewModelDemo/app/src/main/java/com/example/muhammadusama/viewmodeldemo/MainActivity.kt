package com.example.muhammadusama.viewmodeldemo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val TAG : String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mTextView : TextView = findViewById(R.id.tvNumber)
        val mButton : Button = findViewById(R.id.bRandom)

        //val myData = MainActivityViewModel()

        // Creating Instance of View Model
        val model : MainActivityViewModel =
                ViewModelProviders.of(this@MainActivity).get(MainActivityViewModel::class.java)

        val myRandomNumber = model.getNumber()

        //Attach Observer object to live data, using observe method
        myRandomNumber?.observe(this@MainActivity, object : Observer<String>{

            // Controls What happerns when our live data object is Changed
            override fun onChanged(t: String?) {

                mTextView.text = t
                Log.i(TAG,"Data Updated in UI")
            }
        })

        mButton.setOnClickListener {

            model.createNumber()
        }

        Log.i(TAG,"Random Number Set")

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab : FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener{
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }
}