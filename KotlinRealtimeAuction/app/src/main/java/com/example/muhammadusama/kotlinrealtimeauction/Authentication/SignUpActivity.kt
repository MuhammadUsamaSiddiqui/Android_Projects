package com.example.muhammadusama.kotlinrealtimeauction.Authentication

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer.AuctioneerActivity
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.bidder.BidderActivity
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.User
import com.example.muhammadusama.kotlinrealtimeauction.LoginActivity
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private var mType: String? = ""

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mUserDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var nameEditText: EditText? = null
    private var phoneEditText: EditText? = null
    private var progressBar: ProgressBar? = null
    private var auctioneerRadioButton: RadioButton? = null
    private var bidderRadioButton: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mUserDatabaseReference = mFirebaseDatabase?.reference?.child("Users")
        mAuth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        nameEditText = findViewById(R.id.editTextName)
        phoneEditText = findViewById(R.id.editTextPhone)
        progressBar = findViewById(R.id.progressbar)

        auctioneerRadioButton = findViewById(R.id.auctioneer_radio_button)
        bidderRadioButton = findViewById(R.id.bidder_radio_button)

        val loginTextView: TextView = findViewById(R.id.textViewLogin)
        val signUpButton: Button = findViewById(R.id.buttonSignUp)


        loginTextView.setOnClickListener(this)
        signUpButton.setOnClickListener(this)
        auctioneerRadioButton?.setOnClickListener(this)
        bidderRadioButton?.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.textViewLogin -> {

                val loginIntent: Intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }

            R.id.buttonSignUp -> registerUser()


            R.id.auctioneer_radio_button -> mType = "Auctioneer"

            R.id.bidder_radio_button -> mType = "Bidder"
        }
    }

    private fun registerUser() {

        val name: String = nameEditText?.text.toString().trim()
        val email: String = emailEditText?.text.toString().trim()
        val password: String = passwordEditText?.text.toString().trim()
        val phone: String = phoneEditText?.text.toString().trim()

        if (name.isEmpty()) {

            nameEditText?.error = "Enter your Name!"
            nameEditText?.requestFocus()
            return
        }

        if (email.isEmpty()) {

            emailEditText?.error = "Enter your Email!"
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

        if (phone.isEmpty()) {

            phoneEditText?.error = "Enter your Phone!"
            phoneEditText?.requestFocus()
            return
        }
        if (phone.length != 11) {

            phoneEditText?.error = "Invalid Number!"
            phoneEditText?.requestFocus()
            return
        }
        if (!auctioneerRadioButton?.isChecked!! && !bidderRadioButton?.isChecked!!) {

            Toast.makeText(this@SignUpActivity, "Choose the Category!", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar?.visibility = View.VISIBLE

        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener { p0 ->

            progressBar?.visibility = View.GONE

            if (p0.isSuccessful) {

                val userID: String = mAuth?.currentUser!!.uid
                val currentUser = User(name= name,email =  email, phone = phone, type = mType, userID = userID)

                mUserDatabaseReference?.child(userID)?.setValue(currentUser)

                Toast.makeText(this@SignUpActivity, "Sign Up Successfull!", Toast.LENGTH_SHORT).show()

                if (mType.equals(resources.getString(R.string.auctioneer))) {

                    val auctioneerIntent = Intent(this@SignUpActivity, AuctioneerActivity::class.java)
                    auctioneerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(auctioneerIntent)
                    finish()

                } else {

                    val bidderIntent = Intent(this@SignUpActivity, BidderActivity::class.java)
                    bidderIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(bidderIntent)
                    finish()

                }
            } else {

                if (p0?.exception is FirebaseAuthUserCollisionException) {

                    Toast.makeText(applicationContext, "Already Signed Up with this Email!", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(applicationContext, p0?.exception!!.message, Toast.LENGTH_SHORT).show()

                }
            }

        }
    }
}
