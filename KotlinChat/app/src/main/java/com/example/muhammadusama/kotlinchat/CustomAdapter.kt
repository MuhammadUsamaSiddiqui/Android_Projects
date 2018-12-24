package com.example.muhammadusama.kotlinchat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.muhammadusama.kotlinchat.ModelClasses.ChatMessage

class CustomAdapter (val messagesList : ArrayList<ChatMessage>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val v = LayoutInflater.from(p0?.context).inflate(R.layout.chat_list_item,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return  messagesList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val message : ChatMessage = messagesList[p1]
        p0?.textViewMessage.text =message.message
        p0?.textViewUsername.text = message.name
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var textViewMessage =  itemView.findViewById(R.id.textViewMessage) as TextView
        var textViewUsername = itemView.findViewById(R.id.textViewUsername) as TextView
    }
}