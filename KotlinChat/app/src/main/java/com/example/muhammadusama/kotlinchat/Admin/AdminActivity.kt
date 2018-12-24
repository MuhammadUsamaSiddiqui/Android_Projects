package com.example.muhammadusama.kotlinchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import com.example.muhammadusama.kotlinchat.Admin.UsersAdapter
import com.example.muhammadusama.kotlinchat.ModelClasses.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity() {

    //RecycleView,Adapter and List
    private var usersList = ArrayList<User>()
    private var mUsersRecyclerView : RecyclerView? = null
    private  var   mUsersAdapter : UsersAdapter? = null

    //Firebase Database
    private  var mFirebaseDatabase : FirebaseDatabase? = null
    private  var mUserDatabaseReference : DatabaseReference? = null
    private var mChildEventListener : ChildEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //Initialize Firebase Components
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mUserDatabaseReference = mFirebaseDatabase?.getReference()?.child("Users")


        //RecyclerView and Adapter
        mUsersRecyclerView = findViewById(R.id.recyclerView)
        mUsersRecyclerView?.layoutManager = LinearLayoutManager(this@AdminActivity, LinearLayout.VERTICAL,false)
        mUsersAdapter = UsersAdapter (usersList) {

            var intent = Intent(this,UserActivity::class.java)
            intent.putExtra("UserID",it)
            startActivity(intent)

        }
        mUsersRecyclerView?.adapter = mUsersAdapter

    }

    override fun onResume() {
        super.onResume()

        attachDatabaseReadListener()
    }

    override fun onPause() {
        super.onPause()

        if (mChildEventListener != null){

            mUserDatabaseReference?.removeEventListener(mChildEventListener!!)
        }

        usersList.clear()
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

                var  user: User? = p0.getValue(User :: class.java)

                usersList.add(user!!)
                mUsersAdapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        }

        mUserDatabaseReference?.addChildEventListener(mChildEventListener!!)
    }


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
}