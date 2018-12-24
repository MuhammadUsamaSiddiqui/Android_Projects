package com.example.muhammadusama.kotlinrecyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import java.lang.reflect.Array

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)

        val user = ArrayList<User>()
        user.add(User ("Usama","Korangi"))
        user.add(User ("Bilal","Nazimabad"))
        user.add(User ("Junaid","Shahra-e-Faisal"))
        user.add(User ("Suleman","Lucknow Society"))
        user.add(User ("Moiz","Defence"))

        val adapter = CustomAdapter(user,{

            Toast.makeText(this,"Usama",Toast.LENGTH_SHORT).show()
        })
        recyclerView.adapter = adapter


    }
}
