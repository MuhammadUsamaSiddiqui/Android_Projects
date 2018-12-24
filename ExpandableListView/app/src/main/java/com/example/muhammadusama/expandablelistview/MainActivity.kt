package com.example.muhammadusama.expandablelistview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var expandableListView: ExpandableListView

    val header : MutableList<String> = ArrayList()
    val body : ArrayList<MutableList<String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expandableListView = findViewById(R.id.expandableListView)
        header.add("Season 1")
        header.add("Season 2")
        header.add("Season 3")

        val season1 : MutableList<String> = ArrayList()
        season1.add("Season 1 : 1")
        season1.add("Season 1 : 2")
        season1.add("Season 1 : 3")
        season1.add("Season 1 : 4")
        season1.add("Season 1 : 5")

        val season2 : MutableList<String> = ArrayList()
        season2.add("Season 2 : 1")
        season2.add("Season 2 : 2")
        season2.add("Season 2 : 3")
        season2.add("Season 2 : 4")
        season2.add("Season 2 : 5")

        val season3 : MutableList<String> = ArrayList()
        season3.add("Season 3 : 1")
        season3.add("Season 3 : 2")
        season3.add("Season 3 : 3")
        season3.add("Season 3 : 4")
        season3.add("Season 3 : 5")

        body.add(season1)
        body.add(season2)
        body.add(season3)

        expandableListView.setAdapter(ExpandableListAdapter(expandableListView,this,header,body,{

            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }))

    }
}
