package com.example.muhammadusama.kotlinchat.Admin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.muhammadusama.kotlinchat.R
import com.example.muhammadusama.kotlinchat.User

class UsersAdapter (var usersList : ArrayList<User>, var clickListener: (String) -> Unit) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.users_list_item,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return usersList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        var user= usersList[p1]
        p0.textViewName.text = user.name
        p0.itemView.setOnClickListener {

            clickListener(user.userID!!)
        }
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var textViewName = itemView.findViewById(R.id.textViewName) as TextView
    }
}