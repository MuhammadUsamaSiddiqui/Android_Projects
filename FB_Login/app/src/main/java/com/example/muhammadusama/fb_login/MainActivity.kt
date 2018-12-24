package com.example.muhammadusama.fb_login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.json.JSONObject
import java.util.*
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var callbackManager :CallbackManager
    private lateinit var loginButton : LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callbackManager = CallbackManager.Factory.create()
        loginButton = findViewById(R.id.login_button)

        loginButton.setReadPermissions("email","public_profile")

        loginButton.registerCallback(callbackManager!!, object:FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {

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

                Toast.makeText(this@MainActivity,"Login Successfull! ",Toast.LENGTH_SHORT).show()

            }

            override fun onCancel() {

                Toast.makeText(this@MainActivity,"Login Cancelled! ",Toast.LENGTH_SHORT).show()

            }

            override fun onError(error: FacebookException?) {

                Toast.makeText(this@MainActivity,"Login Error! "+ error!!.message,Toast.LENGTH_SHORT).show()
                Log.v("Usama",error!!.message)
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

