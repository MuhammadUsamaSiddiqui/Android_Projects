package com.example.muhammadusama.kotlin_friendly_chat

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.muhammadusama.kotlin_friendly_chat.R.id.photoImageView

class MessageAdapter (context : Context,resource :Int , list : List<FriendlyMessage> ) : ArrayAdapter<FriendlyMessage>(context,resource,list){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var listItemView = convertView

        if ( listItemView == null){

            listItemView = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false)
        }

        var  photoImageView:ImageView = listItemView!!.findViewById(R.id.photoImageView)
        var messageTextView : TextView = listItemView!!.findViewById(R.id.messageTextView)
        var authorTextView : TextView = listItemView!!.findViewById(R.id.nameTextView)

        var message = getItem(position)

        var isPhoto : Boolean = message.photoUrl.isNullOrEmpty()

        if (!isPhoto) {

            messageTextView.setVisibility(View.GONE)
            photoImageView.setVisibility(View.VISIBLE)
            Glide.with(photoImageView.getContext())
                    .load(message.photoUrl)
                    .into(photoImageView)
        } else {

            messageTextView.setVisibility(View.VISIBLE)
            photoImageView.setVisibility(View.GONE)
            messageTextView.setText(message.text)
        }

        authorTextView.setText(message.name)

        return listItemView
    }
}