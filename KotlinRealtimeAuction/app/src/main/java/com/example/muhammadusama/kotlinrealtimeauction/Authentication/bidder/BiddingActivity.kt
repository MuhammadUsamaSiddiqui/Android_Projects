package com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Laptop
import com.example.muhammadusama.kotlinrealtimeauction.R
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Bidder
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Phone
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.User
import com.example.muhammadusama.kotlinrealtimeauction.R.mipmap.calendar
import com.google.android.gms.flags.Flag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_bid_items.*
import kotlinx.android.synthetic.main.bidding_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class BiddingActivity : AppCompatActivity(), View.OnClickListener {

    private var mTextViewCountDown: TextView? = null
    private var mCountDownTimer: CountDownTimer? = null
    private var mTimeLeftInMillis : Long = 0

    private var phoneObject : Phone? = null
    private var laptopObject : Laptop? = null

    private var type : String? = ""
    private var period : String? =""

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mBidsDatabaseReference: DatabaseReference? = null
    private var mUserDatabaseReference : DatabaseReference? = null
    private var mBiddersDatabaseReference: DatabaseReference? = null

    private var mAuth : FirebaseAuth?= null

    private var mUserID : String? = ""

    private var dateFormat: SimpleDateFormat? = null
    private var timeFormat: SimpleDateFormat? = null

    private var currentDate : String? = ""
    private var currentTime : String? = ""

    private var endTimeInMilliseconds : Long = 0
    private var startTimeInMilliseconds : Long = 0
    private var endHourInMilliseconds : Long = 0
    private var endMinutesInMilliseconds: Long = 0
    private var startHourInMilliseconds: Long = 0
    private var startMinutesInMilliseconds: Long = 0

    private var bidderList : ArrayList<Bidder> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bidding)

        val itemDetails : Button = findViewById(R.id.item_details)
        val doBid : Button = findViewById(R.id.do_bid)
        val checkOthersBid : Button = findViewById(R.id.check_others_bid)
        mTextViewCountDown = findViewById(R.id.text_view_countdown)

        // Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mUserID = mAuth?.currentUser?.uid
        mUserDatabaseReference = mFirebaseDatabase?.getReference()?.child("Users")?.child(mUserID!!)


        //Date and Time Formats
        dateFormat = SimpleDateFormat("dd/MM/yyyy")
        timeFormat = SimpleDateFormat("HH:mm")


        currentTime = timeFormat?.format(Calendar.getInstance().time)
        currentDate = dateFormat?.format(Date())

        type = intent.extras.getString("Type")
        period = intent.extras.getString("Period")

        val calendar = Calendar.getInstance()


        val timeFormat = SimpleDateFormat("HH:mm")
        val currentTime = timeFormat?.format(Calendar.getInstance().time)
        var Split : List<String>? = currentTime.split(":")
        val currentHourInMilliseconds: Long  = (Integer.parseInt(Split?.get(0)) * 60 *60 *1000).toLong()
        val currentMinutesInMilliseconds: Long = (Integer.parseInt(Split?.get(1)) * 60 * 1000).toLong()
        val currentTimeInMiiliseconds = currentHourInMilliseconds + currentMinutesInMilliseconds


        if(type.equals("Laptop")){

            laptopObject = intent.getSerializableExtra("Object") as Laptop

            if(period.equals("Live")){

                var Split : List<String>? = laptopObject?.endTime?.split(":")
                endHourInMilliseconds  = (Integer.parseInt(Split?.get(0)) * 60 *60 *1000).toLong()
                endMinutesInMilliseconds = (Integer.parseInt(Split?.get(1)) * 60 * 1000).toLong()
                endTimeInMilliseconds = endHourInMilliseconds + endMinutesInMilliseconds
                mTimeLeftInMillis = endTimeInMilliseconds - currentTimeInMiiliseconds

            }else{

                var Split : List<String>? = laptopObject?.startTime?.split(":")
                startHourInMilliseconds = (Integer.parseInt(Split?.get(0)) * 60 *60 *1000).toLong()
                startMinutesInMilliseconds = (Integer.parseInt(Split?.get(1)) * 60 * 1000).toLong()
                startTimeInMilliseconds =  startHourInMilliseconds +  startMinutesInMilliseconds
                mTimeLeftInMillis = startTimeInMilliseconds - currentTimeInMiiliseconds

            }

/*         calendar?.set(Calendar.HOUR_OF_DAY,Integer.parseInt(Split?.get(0)))
            calendar?.set(Calendar.MINUTE,Integer.parseInt(Split?.get(1)))
*/ //calendar.timeInMillis - Calendar.getInstance().timeInMillis


        }else{

             phoneObject = intent.getSerializableExtra("Object") as Phone

            if(period.equals("Live")){

                var Split : List<String>? = phoneObject?.endTime?.split(":")
                endHourInMilliseconds = (Integer.parseInt(Split?.get(0)) * 60 *60 *1000).toLong()
                endMinutesInMilliseconds = (Integer.parseInt(Split?.get(1)) * 60 * 1000).toLong()
                endTimeInMilliseconds = endHourInMilliseconds + endMinutesInMilliseconds
                mTimeLeftInMillis = endTimeInMilliseconds - currentTimeInMiiliseconds

            }else{

                var Split : List<String>? = laptopObject?.startTime?.split(":")
                startHourInMilliseconds = (Integer.parseInt(Split?.get(0)) * 60 *60 *1000).toLong()
                startMinutesInMilliseconds = (Integer.parseInt(Split?.get(1)) * 60 * 1000).toLong()
                startTimeInMilliseconds =  startHourInMilliseconds +  startMinutesInMilliseconds
                mTimeLeftInMillis = startTimeInMilliseconds - currentTimeInMiiliseconds

            }

        }

         startTimer()

        itemDetails.setOnClickListener(this)
        doBid.setOnClickListener(this)
        checkOthersBid.setOnClickListener(this)

    }

    private fun startTimer() {

        mCountDownTimer =  object : CountDownTimer(mTimeLeftInMillis, 1000) {

            override fun onFinish() {

                Toast.makeText(this@BiddingActivity,"Finish",Toast.LENGTH_SHORT).show()

                if(type!!.equals("Laptop")){

                    mBiddersDatabaseReference = mFirebaseDatabase?.getReference()?.child("Bids")?.child(laptopObject?.pushKey!!)
                    readBiddersList(type!!,laptopObject?.pushKey!!)

                }else{

                    mBiddersDatabaseReference = mFirebaseDatabase?.getReference()?.child("Bids")?.child(phoneObject?.pushKey!!)
                    readBiddersList(type!!, phoneObject?.pushKey!!)
                }
                }

            override fun onTick(millisUntilFinished: Long) {

                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

        }.start()

    }

    private fun readBiddersList(type : String ,productID: String) {

        Toast.makeText(this,"Usama",Toast.LENGTH_SHORT).show()

        mBiddersDatabaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if(!p0.exists()){
                    return
                }
                else{

                    Toast.makeText(this@BiddingActivity,"Siddiqui",Toast.LENGTH_SHORT).show()
                    for (postSnapShot : DataSnapshot  in p0.children) {

                        val bidder  = postSnapShot.getValue(Bidder::class.java)
                        bidderList.add(bidder!!)
                    }

                    var index : Int = 0

                    for (i in 1..(bidderList.size)-1){

                       if(bidderList[index].bid.compareTo(bidderList[i].bid) < 0){
                           index = i
                       }
                   }

                    var mWinnerIdDatabaseReference : DatabaseReference? = mFirebaseDatabase?.getReference()?.child(type!!)
                                                            ?.child(productID!!)?.child("winnerId")

                    mWinnerIdDatabaseReference?.setValue(bidderList[index].userId)

                    var mWinnerDatabaseReference = mBiddersDatabaseReference?.child(bidderList[index].userId)?.child("winner")
                    mWinnerDatabaseReference?.setValue("true")
0
                    var mStatusDatabaseReference = mFirebaseDatabase?.getReference()?.child(type!!)
                                                                    ?.child(productID!!)?.child("status")
                    mStatusDatabaseReference?.setValue("SOLD")


                    val bidderActivityIntent = Intent(this@BiddingActivity,BidderActivity::class.java)
                    bidderActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(bidderActivityIntent)

                }
            }

        })
    }

    private fun updateCountDownText() {

        val hours = (mTimeLeftInMillis / 1000)  / 3600
        val minutes = (mTimeLeftInMillis / 1000 % 3600) / 60
        val seconds = (mTimeLeftInMillis / 1000)  % 60

        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d",hours,minutes,seconds)
        mTextViewCountDown?.setText(timeLeftFormatted)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.item_details->{

                val biddingItemDetailsIntent = Intent (this@BiddingActivity,BiddingItemDetailsActivity::class.java)
               if(type.equals("Laptop")!!){

                   biddingItemDetailsIntent.putExtra("Object",laptopObject)
                   biddingItemDetailsIntent.putExtra("Type","Laptop")

               }else{
                   biddingItemDetailsIntent.putExtra("Object",phoneObject)
                   biddingItemDetailsIntent.putExtra("Type","Phone")
               }
                startActivity(biddingItemDetailsIntent)
            }

            R.id.do_bid->{

                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogueView = inflater.inflate(R.layout.bidding_dialog, null)

                builder.setView(dialogueView)

                builder.setTitle("Bid")

                val biddingDialogue = builder.create()
                biddingDialogue.show()

                val bidEditText = dialogueView.findViewById(R.id.bid) as EditText
                val okButton = dialogueView.findViewById(R.id.ok_button) as Button
                val assurityCheckbox = dialogueView.findViewById(R.id.assurity_checkbox) as CheckBox

                assurityCheckbox.setOnCheckedChangeListener( object : CompoundButton.OnCheckedChangeListener{
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                        okButton.isEnabled = isChecked
                    }
                })


                okButton.setOnClickListener {

                    val bid = bidEditText.text.toString().trim()

                    if (bid.isEmpty()) {

                        bidEditText.error = "Enter Bid"
                        bidEditText.requestFocus()
                    } else {

                        if (type.equals("Laptop")) {

                            if (bid.compareTo(laptopObject?.minimumBid!!) < 0) {

                                Toast.makeText(this@BiddingActivity, "Your Bid is less than the Minimum Bid", Toast.LENGTH_SHORT).show()
                            } else {

                                attachDatabaseReadListener(bid, laptopObject!!.pushKey,laptopObject!!.model,
                                        laptopObject!!.imageUrl)
                                Toast.makeText(this@BiddingActivity, "Bidding Successful!", Toast.LENGTH_SHORT).show()
                                biddingDialogue.dismiss()
                            }

                        } else {

                            if (bid.compareTo(phoneObject?.minimumBid!!) < 0) {

                                Toast.makeText(this@BiddingActivity, "Your Bid is less than the Minimum Bid", Toast.LENGTH_SHORT).show()
                            } else {

                                attachDatabaseReadListener(bid, phoneObject!!.pushKey,phoneObject!!.model,phoneObject!!.imageUrl)
                                Toast.makeText(this@BiddingActivity, "Bidding Successful!", Toast.LENGTH_SHORT).show()
                                biddingDialogue.dismiss()
                            }
                        }
                    }
                }
            }

            R.id.check_others_bid->{

                val checkOthersBid = Intent (this@BiddingActivity,CheckOthersBidActivity::class.java)
                if (type.equals("Laptop")) {

                    checkOthersBid.putExtra("Product ID",laptopObject?.pushKey)
                }else{
                    checkOthersBid.putExtra("Product ID",phoneObject?.pushKey)
                }
                startActivity(checkOthersBid)
            }
        }
    }

    private fun attachDatabaseReadListener(bid : String, productID : String, itemModel : String , imageUrl : String ) {

        mUserDatabaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if(!p0.exists()){
                    return
                }
                else{

                    val bidderDetails = p0.getValue(User::class.java)

                    val bidder = Bidder(bidderDetails?.name!!, bidderDetails.email!!, bidderDetails.phone!!,bid,
                            mUserID!!,productID, currentDate!!,currentTime!!,"false",itemModel,imageUrl)

                    mBidsDatabaseReference = mFirebaseDatabase?.getReference()?.child("Bids")?.child(productID)?.child(mUserID!!)
                    mBidsDatabaseReference?.setValue(bidder)
                    mBidsDatabaseReference = null
                }
            }
        })
    }
}
