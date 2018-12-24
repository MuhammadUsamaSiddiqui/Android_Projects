package com.example.muhammadusama.kotlinrecyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.w3c.dom.Text

class CustomAdapter (val userList : ArrayList<User>,private var clickListener : () -> Unit) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val v = LayoutInflater.from(p0?.context).inflate(R.layout.list_layout,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return userList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val user : User = userList[p1]
        p0?.textViewName?.text = user.name
        p0?.textViewAddress?.text = user.address
        p0?.itemView.setOnClickListener{
            clickListener()
        }

    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){

        var textViewName =  itemView.findViewById(R.id.textViewName) as TextView
        var textViewAddress = itemView.findViewById(R.id.textViewAddress) as TextView


    }
}