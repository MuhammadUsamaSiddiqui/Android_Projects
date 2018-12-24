package com.example.muhammadusama.kotlinchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.muhammadusama.kotlinchat.ModelClasses.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserActivity : AppCompatActivity() {

    //RecycleView,Adapter and List
    private var messagesList = ArrayList<ChatMessage>()
    private var mMessageRecyclerView: RecyclerView? = null
    private var mMessageAdapter: CustomAdapter? = null

    //Default Message Length
    private val DEFAULT_MESSAGE_LENGTH_LIMIT = 1000

    //For Username
    private var mUsername = ArrayList<String>()
    private var mUserID: String? = ""
    private var myUserID: String? = ""

    //For Firebase Features

    //Firebase Database
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mMessagesDatabaseReference: DatabaseReference? = null
    private var mUserDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null

    //Firebase Authentication
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        //Getting Infromation from previous Activity
        var bundle = intent.extras
        mUserID = bundle.getString("UserID")

        //Initialize Firebase Components
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        myUserID = mAuth.uid


        /* if(mUserID.isNullOrEmpty()){
            mUserID = myUserID
         }
 */
        var key: String

        if (myUserID?.compareTo(mUserID!!)!! < 0) {

            key = "$myUserID $mUserID"
        } else {

            key = "$mUserID $myUserID"
        }

        mMessagesDatabaseReference = mFirebaseDatabase?.getReference()?.child("Chats")?.child(key!!)
        mUserDatabaseReference = mFirebaseDatabase?.getReference()?.child("Users")?.child(myUserID!!)?.child("name")

        //RecyclerView and Adapter
        mMessageRecyclerView = findViewById(R.id.recyclerView)
        mMessageRecyclerView?.layoutManager = LinearLayoutManager(this@UserActivity, LinearLayout.VERTICAL, false)
        mMessageAdapter = CustomAdapter(messagesList)
        mMessageRecyclerView?.adapter = mMessageAdapter


        // Initialize references to views
        val mMessageEditText = findViewById(R.id.messageEditText) as EditText
        val mSendButton = findViewById(R.id.sendButton) as Button


        mMessageEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mSendButton.isEnabled = s.toString().trim().isNotEmpty()
            }

        })

        mMessageEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MESSAGE_LENGTH_LIMIT))

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener{

            var message: ChatMessage = ChatMessage(mUsername[0], mMessageEditText.text.toString(),mUserID,myUserID)
            mMessagesDatabaseReference?.push()?.setValue(message)

            // Clear input box
            mMessageEditText.setText("")

        }

        getUsername()
    }

    override fun onPause() {
        super.onPause()

        if (mChildEventListener != null) {

            mMessagesDatabaseReference?.removeEventListener(mChildEventListener!!)
        }

        messagesList.clear()
    }

    override fun onResume() {
        super.onResume()

        attachDatabaseReadListener()

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

                var message: ChatMessage? = p0.getValue(ChatMessage::class.java)

                messagesList.add(message!!)
                mMessageAdapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

                messagesList.remove(p0?.getValue(ChatMessage::class.java))
                mMessageAdapter?.notifyDataSetChanged()
            }

        }

        mMessagesDatabaseReference?.addChildEventListener(mChildEventListener!!)


    }

    private fun getUsername() {

        mUserDatabaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists()) {
                    return
                } else {

                    mUsername.add(p0?.getValue(String::class.java)!!)
                }
            }

        })

    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var menuinflater : MenuInflater = menuInflater
        menuinflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId){

            R.id.menuLogout ->{

                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()

            }
        }
        return true
    }
*/

}
