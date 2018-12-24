package com.example.muhammadusama.kotlinchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null

    private var mSignUpTextview: TextView? = null
    private var mProgressBar: ProgressBar? = null
    private var mLoginButton: Button? = null
    private var mEmailEditText: EditText? = null
    private var mPasswordEditText: EditText? = null
    private var mUserID: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        mSignUpTextview = findViewById(R.id.textViewSignup) as TextView
        mEmailEditText = findViewById(R.id.editTextEmail) as EditText
        mPasswordEditText = findViewById(R.id.editTextPassword) as EditText
        mProgressBar = findViewById(R.id.progressbar) as ProgressBar
        mLoginButton = findViewById(R.id.buttonLogin) as Button

        mSignUpTextview?.setOnClickListener(this)
        mLoginButton?.setOnClickListener(this)


    }

    override fun onResume() {
        super.onResume()

        if (mAuth?.currentUser != null) {

            if (mAuth?.currentUser?.email == "admin@gmail.com") {

                val adminIntent = Intent(this@LoginActivity, AdminActivity::class.java)
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(adminIntent)
                finish()

            } else {

/*
                var userIntent = Intent(this@LoginActivity, UserActivity::class.java)
                userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                userIntent.putExtra("UserID",mUserID)
                startActivity(userIntent)
                finish()
*/

                val adminIntent = Intent(this@LoginActivity, AdminActivity::class.java)
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(adminIntent)
                finish()

            }
        }

    }

    private fun Login() {

        val email = mEmailEditText?.text.toString().trim()
        val password = mPasswordEditText?.text.toString().trim()

        if (email.isNullOrEmpty()) {

            mEmailEditText?.error = "Enter your Email!"
            mEmailEditText?.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            mEmailEditText?.error = "Invalid Email!"
            mEmailEditText?.requestFocus()
            return
        }
        if (password.isNullOrEmpty()) {
            mPasswordEditText?.error = "Enter your Password!"
            mPasswordEditText?.requestFocus()
            return
        }

        if (password.length < 6) {
            mPasswordEditText?.error = "Minimum Password Limit is 6"
            mPasswordEditText?.requestFocus()
            return
        }

        mProgressBar?.visibility = View.VISIBLE

        mAuth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener { task ->

                    mProgressBar?.setVisibility(View.GONE)

                    if (task.isSuccessful) {

                        if (mAuth?.getCurrentUser()?.email == "admin@gmail.com" && password == "123456") {

                            Toast.makeText(this@LoginActivity, "Login Successfull!", Toast.LENGTH_SHORT).show()

                            val adminIntent = Intent(this@LoginActivity, AdminActivity::class.java)
                            adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(adminIntent)
                            finish()

                        } else {

                            Toast.makeText(this@LoginActivity, "Login Successfull!", Toast.LENGTH_SHORT).show()

                            mUserID = mAuth?.uid

                            val adminIntent = Intent(this@LoginActivity, AdminActivity::class.java)
                            adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(adminIntent)
                            finish()

/*
                            var userIntent = Intent(this@LoginActivity, UserActivity::class.java)
                            userIntent.putExtra("UserID", mUserID)
                            startActivity(userIntent)
                            finish()
*/
                        }
                    } else {

                        Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.textViewSignup -> {

                val signuUpIntent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(signuUpIntent)
                finish()

            }

            R.id.buttonLogin -> {
                Login()
            }
        }
    }
}
