package com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Laptop
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.squareup.picasso.Picasso

class LaptopAdapter (var laptopList : ArrayList<Laptop>, var clickListener: (Laptop) -> Unit) : RecyclerView.Adapter<LaptopAdapter.ViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.items_card_view,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return laptopList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val laptop = laptopList[p1]
        p0.itemNameTextView.text = laptop.model
        p0.auctionDateTextView.text = laptop.date
        p0.auctionTimeTextView.text = laptop.startTime
        p0.statusTextView.text = laptop.status

        Picasso.get().load(laptop.imageUrl).fit().placeholder(R.drawable.placeholder).into(p0.itemImageView)

        p0.itemView.setOnClickListener{

            clickListener(laptop)
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