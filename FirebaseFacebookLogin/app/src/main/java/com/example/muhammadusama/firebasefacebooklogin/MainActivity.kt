package com.example.muhammadusama.firebasefacebooklogin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.json.JSONObject
import com.google.firebase.auth.AuthResult
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.internal.FirebaseAppHelper.getToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.AuthCredential



class MainActivity : AppCompatActivity() {

    private lateinit var  callbackManager : CallbackManager
    private lateinit var  mAuthentication : FirebaseAuth
    private lateinit var accessToken : AccessToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuthentication = FirebaseAuth.getInstance()
        initializeFacebookLogin()
    }

    override fun onStart() {
        super.onStart()
//        var currentUser : FirebaseUser? = mAuthentication.currentUser!!
//
//        if (currentUser!=null){
//
//        }
    }

fun initializeFacebookLogin(){

    var loginButton : LoginButton = findViewById(R.id.login_button)
    callbackManager = CallbackManager.Factory.create()

    loginButton.setReadPermissions("email","public_profile")

    loginButton.registerCallback(callbackManager!!, object: FacebookCallback<LoginResult> {

        override fun onSuccess(result: LoginResult?) {

            accessToken = result!!.accessToken

            handleFacebookAccessToken(accessToken)

            var userId : String = result!!.accessToken.userId
            var graphRequest : GraphRequest = GraphRequest.newMeRequest(result!!.accessToken,object : GraphRequest.GraphJSONObjectCallback{

                override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {

                    displayUserInfo(`object`)
                }

            })

            var parameter : Bundle = Bundle()
            parameter.putString("fields","first_name,last_name,email,id")

            graphRequest.parameters = parameter
            graphRequest.executeAsync()

            Toast.makeText(this@MainActivity,"Login Successfull! ", Toast.LENGTH_SHORT).show()

        }

        override fun onCancel() {

            Toast.makeText(this@MainActivity,"Login Cancelled! ", Toast.LENGTH_SHORT).show()

        }

        override fun onError(error: FacebookException?) {

            Toast.makeText(this@MainActivity,"Login Error! "+ error!!.message, Toast.LENGTH_SHORT).show()
            Log.v("Usama",error!!.message)
        }

    })
}

    private fun handleFacebookAccessToken(accessToken: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(accessToken.getToken())
        mAuthentication.signInWithCredential(credential)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuthentication.getCurrentUser()
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                })
    }

    private fun displayUserInfo(jsonObject: JSONObject?) {

        var first_name : String
        var last_name : String
        var email : String
        var id : String

        first_name = jsonObject!!.getString("first_name")
        last_name = jsonObject!!.getString("last_name")
        email = jsonObject!!.getString("email")
        id = jsonObject!!.getString("id")

        Toast.makeText(this@MainActivity,"First Name "+ first_name,Toast.LENGTH_SHORT).show()
        Toast.makeText(this@MainActivity,"Last Name "+ last_name,Toast.LENGTH_SHORT).show()
        Toast.makeText(this@MainActivity,"Email "+ email,Toast.LENGTH_SHORT).show()
        Toast.makeText(this@MainActivity,"ID "+ id,Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }


}
