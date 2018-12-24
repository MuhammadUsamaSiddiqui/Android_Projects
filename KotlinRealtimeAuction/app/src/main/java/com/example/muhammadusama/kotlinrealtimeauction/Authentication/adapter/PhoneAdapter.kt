package com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Phone
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.squareup.picasso.Picasso

class PhoneAdapter (var phoneList : ArrayList<Phone>, var clickListener: (Phone) -> Unit) : RecyclerView.Adapter<PhoneAdapter.ViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.items_card_view,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return phoneList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val phone = phoneList[p1]
        p0.itemNameTextView.text = phone.model
        p0.auctionDateTextView.text = phone.date
        p0.auctionTimeTextView.text = phone.startTime
        p0.statusTextView.text = phone.status

        Picasso.get().load(phone.imageUrl).fit().placeholder(R.drawable.placeholder).into(p0.itemImageView)

        p0.itemView.setOnClickListener{

            clickListener(phone)
        }

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val itemNameTextView : TextView = itemView.findViewById(R.id.item_name_textView)
        val itemImageView : ImageView = itemView.findViewById(R.id.item_imageView)
        val auctionDateTextView : TextView = itemView.findViewById(R.id.auction_date_textView)
        val auctionTimeTextView : TextView = itemView.findViewById(R.id.auction_time_textView)
        val statusTextView : TextView = itemView.findViewById(R.id.status_textView)
    }

}