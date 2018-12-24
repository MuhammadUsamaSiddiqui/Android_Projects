package com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Bidder
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.squareup.picasso.Picasso

class BiddingHistoryAdapter (var bidderList : ArrayList<Bidder>) : RecyclerView.Adapter<BiddingHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.bidder_history_card_view,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

     return bidderList.size

    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val biddingDetails = bidderList [p1]
        p0.itemNameTextView.text = biddingDetails.itemModel
        p0.dateTextView.text = biddingDetails.date
        p0.timeTextView.text = biddingDetails.time
        p0.bidPriceTextView.text = biddingDetails.bid

        Picasso.get().load(biddingDetails.itemImageUrl).fit().placeholder(R.drawable.placeholder).into(p0.itemImageView)

        if(biddingDetails.winner.equals("true")){
            p0.winnerTextView.text = "Win"
        }else{
            p0.winnerTextView.text = "Lose"
        }
    }


    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

        val itemImageView : ImageView = itemView.findViewById(R.id.item_imageView)
        val itemNameTextView : TextView = itemView.findViewById(R.id.item_name_textView)
        val dateTextView : TextView = itemView.findViewById(R.id.bid_date_textView)
        val timeTextView : TextView = itemView.findViewById(R.id.bid_time_textView)
        val bidPriceTextView : TextView = itemView.findViewById(R.id.bid_price_textView)
        val winnerTextView : TextView = itemView.findViewById(R.id.winner_textView)

    }
}