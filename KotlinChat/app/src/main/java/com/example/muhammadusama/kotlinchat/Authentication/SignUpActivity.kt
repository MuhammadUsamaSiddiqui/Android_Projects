package com.example.muhammadusama.kotlinchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mUserDatabaseReference: DatabaseReference? = null

    private var mLoginTextView: TextView? = null
    private var mSignUpButton: Button? = null
    private var mProgressBar: ProgressBar? = null
    private var mNameEditText: EditText? = null
    private var mEmailEditText:EditText? = null
    private var mPasswordEditText:EditText? = null
    private var mPhoneEditText:EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mUserDatabaseReference = mFirebaseDatabase?.getReference()?.child("Users")

        mLoginTextView = findViewById(R.id.textViewLogin) as TextView
        mEmailEditText = findViewById(R.id.editTextEmail) as EditText
        mPasswordEditText = findViewById(R.id.editTextPassword) as EditText
        mNameEditText = findViewById(R.id.editTextName) as EditText
        mPhoneEditText = findViewById(R.id.editTextPhone) as EditText
        mProgressBar = findViewById(R.id.progressbar) as ProgressBar
        mSignUpButton = findViewById(R.id.buttonSignUp) as Button

        mLoginTextView?.setOnClickListener(this)
        mSignUpButton?.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        when (v?.id){

            R.id.textViewLogin -> {

                val loginIntent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()

            }

            R.id.buttonSignUp ->{
                registerUser()

            }
        }
    }

    private fun registerUser() {
        val mName = mNameEditText?.text.toString().trim ()
        val mEmail = mEmailEditText?.text.toString().trim ()
        val mPassword = mPasswordEditText?.text.toString().trim ()
        val mPhone = mPhoneEditText?.text.toString().trim ()

        if (mName.isNullOrEmpty()) {

            mNameEditText?.error = "Enter your Name!"
            mNameEditText?.requestFocus()
            return
        }

        if (mEmail.isNullOrEmpty()) {

            mEmailEditText?.error = "Enter your Email!"
            mEmailEditText?.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {

            mEmailEditText?.error ="Invalid Email!"
            mEmailEditText?.requestFocus()
            return
        }

        if (mPassword.isNullOrEmpty()) {
            mPasswordEditText?.error = "Enter your Password!"
            mPasswordEditText?.requestFocus()
            return
        }

        if (mPassword.length < 6) {
            mPasswordEditText?.error = "Minimum Password Limit is 6"
            mPasswordEditText?.requestFocus()
            return
        }

        if (mPhone.isNullOrEmpty()) {

            mPhoneEditText?.error = "Enter your Phone!"
            mPhoneEditText?.requestFocus()
            return
        }
        if (mPhone.length != 11) {

            mPhoneEditText?.error = "Invalid Number!"
            mPhoneEditText?.requestFocus()
            return
        }

        mProgressBar?.visibility = View.VISIBLE

        mAuth?.createUserWithEmailAndPassword(mEmail, mPassword)
                ?.addOnCompleteListener { task ->
                    mProgressBar?.visibility = View.GONE

                    if (task.isSuccessful) {

                      val  mUserID = mAuth?.uid

                        val token= FirebaseInstanceId.getInstance().token
                        val currentUser = User(mName, mEmail, mPhone, mUserID,token)

                        Toast.makeText(this@SignUpActivity, "Sign Up Successfull!", Toast.LENGTH_SHORT).show()

                        mUserDatabaseReference = mUserDatabaseReference?.child(mUserID!!)
                        mUserDatabaseReference?.setValue(currentUser)

                        val adminIntent = Intent(this@SignUpActivity, AdminActivity::class.java)
                        adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(adminIntent)
                        finish()

                         /*val userIntent = Intent(this@SignUpActivity, UserActivity::class.java)
                         userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                         userIntent.putExtra("UserID",mUserID)
                         startActivity(userIntent)
                         finish()*/
                    } else {

                        if (task.exception is FirebaseAuthUserCollisionException) {

                            Toast.makeText(applicationContext, "Already Signed Up with this Email!", Toast.LENGTH_SHORT).show()
                        } else {

                            Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }
    }