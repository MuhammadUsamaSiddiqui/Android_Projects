package com.example.muhammadusama.kotlinrealtimeauction

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.SignUpActivity
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer.AuctioneerActivity
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder.BidderActivity
import com.example.muhammadusama.kotlinrealtimeauction.R.string.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var mFirebaseDatabase : FirebaseDatabase? = null
    private var mUserDatabaseReference : DatabaseReference?= null
    private var mAuth : FirebaseAuth?= null

    private var passwordEditText : EditText?= null
    private var emailEditText : EditText?= null
    private var progressBar : ProgressBar?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mUserDatabaseReference = mFirebaseDatabase?.reference?.child("Users")
        mAuth = FirebaseAuth.getInstance()

        emailEditText  = findViewById(R.id.editTextEmail)
        passwordEditText  = findViewById(R.id.editTextPassword)
        progressBar  = findViewById(R.id.progressbar)

        val signUpTextView : TextView = findViewById(R.id.textViewSignup)
        val loginButton : Button = findViewById(R.id.buttonLogin)

        signUpTextView.setOnClickListener(this)
        loginButton.setOnClickListener(this)

        if(mAuth?.currentUser?.uid!= null){

            attachDatabaseReadListener()

        }

    }

    override fun onResume() {

        super.onResume()

    }
    override fun onClick(v: View?) {

        when(v?.id){

            R.id.textViewSignup ->{

                val signUpIntent : Intent = Intent (this@LoginActivity,SignUpActivity::class.java)
                startActivity(signUpIntent)
                finish()
            }

            R.id.buttonLogin ->  login()


        }
    }

    private fun login() {

        val password : String = passwordEditText?.text.toString().trim()
        val email : String = emailEditText?.text.toString().trim()

        if (email.isEmpty()) {

            emailEditText?.error= "Enter your Email!"
            emailEditText?.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            emailEditText?.error = "Invalid Email!"
            emailEditText?.requestFocus()
            return
        }

        if (password.isEmpty()) {
            passwordEditText?.error = "Enter your Password!"
            passwordEditText?.requestFocus()
            return
        }

        if (password.length < 6) {
            passwordEditText?.error = "Minimum Password Limit is 6"
            passwordEditText?.requestFocus()
            return
        }

        progressBar?.setVisibility(View.VISIBLE)

        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { p0->

            progressBar?.visibility = View.GONE

            if(p0.isSuccessful){

                attachDatabaseReadListener()

            }else{

                Toast.makeText(applicationContext, p0?.exception!!.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun attachDatabaseReadListener() {

        var userID : String = mAuth?.currentUser!!.uid
        mUserDatabaseReference = mUserDatabaseReference?.child(userID)?.child("type")

        mUserDatabaseReference?.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                if(!p0.exists()){
                    Toast.makeText(this@LoginActivity, "Account does not exist!", Toast.LENGTH_SHORT).show()
                    return
                }else{

                    val type : String = p0.getValue(String::class.java)!!

                    if ( type.equals(resources.getString(R.string.auctioneer))){

                        val auctioneerIntent = Intent(this@LoginActivity, AuctioneerActivity::class.java)
                        startActivity(auctioneerIntent)
                        finish()
                    }else{

                        val bidderIntent = Intent(this@LoginActivity, BidderActivity::class.java)
                        startActivity(bidderIntent)
                        finish()
                    }
                }
            }

        })
    }
}
