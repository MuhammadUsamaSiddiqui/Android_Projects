package com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Laptop
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Phone
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.User
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class BiddingItemDetailsActivity : AppCompatActivity(),View.OnClickListener {

    private var phoneObject : Phone? = null
    private var laptopObject : Laptop? = null

    private var ramTextView: TextView? = null
    private var memoryTextView: TextView? = null
    private var modelTextView: TextView? = null
    private var manufacturerTextView: TextView? = null
    private var minimumBidTextView: TextView? = null
    private var processorTextView: TextView? = null
    private var versionTextView: TextView? = null
    private var cameraTextView: TextView? = null
    private var auctioneerNameTextView: TextView? = null
    private var auctioneerEmailTextView: TextView? = null
    private var auctioneerPhoneTextView: TextView? = null

    private var itemImageView: ImageView? = null


    private var type : String? = ""

    //Firebase Database
    private  var mFirebaseDatabase : FirebaseDatabase? = null
    private  var mUserDatabaseReference : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bidding_item_details)

        type = intent.extras.getString("Type")

        //Initialize Firebase Components
        mFirebaseDatabase = FirebaseDatabase.getInstance()

        //Text View
        auctioneerNameTextView = findViewById(R.id.auctioneer_name_textView)
        auctioneerEmailTextView = findViewById(R.id.auctioneer_email_textView)
        auctioneerPhoneTextView = findViewById(R.id.auctioneer_phone_textView)
        ramTextView = findViewById(R.id.TextViewRam)
        memoryTextView = findViewById(R.id.TextViewMemory)
        processorTextView = findViewById(R.id.TextViewProcessor)
        modelTextView = findViewById(R.id.TextViewModel)
        manufacturerTextView = findViewById(R.id.TextViewManufacturer)
        minimumBidTextView = findViewById(R.id.TextViewMinimumBid)

        //ImageView
        itemImageView = findViewById(R.id.item_image)
        itemImageView?.setOnClickListener(this)

        if(type.equals("Laptop")) {

            laptopObject = intent.getSerializableExtra("Object") as Laptop
            mUserDatabaseReference = mFirebaseDatabase?.getReference()?.child("Users")?.child(laptopObject!!.userID)
            attachDatabaseReadListener()

            ramTextView?.text = laptopObject?.ram
            memoryTextView?.text = laptopObject?.memory
            processorTextView?.text = laptopObject?.processor
            modelTextView?.text = laptopObject?.model
            manufacturerTextView?.text = laptopObject?.manufacturer
            minimumBidTextView?.text = laptopObject?.minimumBid

            Picasso.get().load(laptopObject?.imageUrl).fit().placeholder(R.drawable.placeholder).into(itemImageView)

        }else{

            var phoneLayout : LinearLayout = findViewById(R.id.phone_layout)
            phoneLayout.visibility = View.VISIBLE

            versionTextView = findViewById(R.id.TextViewVersion)
            cameraTextView = findViewById(R.id.TextViewCamera)

            phoneObject = intent.getSerializableExtra("Object") as Phone
            mUserDatabaseReference = mFirebaseDatabase?.getReference()?.child("Users")?.child(phoneObject!!.userID)
            attachDatabaseReadListener()

            ramTextView?.text = phoneObject?.ram
            memoryTextView?.text = phoneObject?.memory
            processorTextView?.text = phoneObject?.processor
            modelTextView?.text = phoneObject?.model
            manufacturerTextView?.text = phoneObject?.manufacturer
            minimumBidTextView?.text = phoneObject?.minimumBid
            versionTextView?.text = phoneObject?.version
            cameraTextView?.text = phoneObject?.camera

            Picasso.get().load(phoneObject?.imageUrl).fit().placeholder(R.drawable.placeholder).into(itemImageView)

        }
    }

    private fun attachDatabaseReadListener() {
        mUserDatabaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if(!p0.exists()){
                    return
                }
                else{

                    val auctioneer =  p0.getValue(User::class.java)
                    auctioneerNameTextView?.text = auctioneer?.name
                    auctioneerEmailTextView?.text = auctioneer?.email
                    auctioneerPhoneTextView?.text = auctioneer?.phone
                }
            }

        })
    }

    override fun onClick(v: View?) {

         when(v?.id){

              R.id.item_image->{

                 showItemImage()
             }
         }
    }

    private fun showItemImage() {

    }

}
