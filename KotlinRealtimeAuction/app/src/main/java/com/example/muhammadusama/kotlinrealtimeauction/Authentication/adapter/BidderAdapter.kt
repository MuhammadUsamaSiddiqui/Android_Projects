package com.example.muhammadusama.kotlinrealtimeauction.Authentication.adapter

import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Bidder
import com.example.muhammadusama.kotlinrealtimeauction.R
import org.w3c.dom.Text

class BidderAdapter (var bidderList : ArrayList<Bidder>) : RecyclerView.Adapter<BidderAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.bidders_card_view,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return bidderList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val bidder = bidderList[p1]
        p0.nameTextView.text = bidder.name
        p0.dateTextView.text = bidder.date
        p0.timeTextView.text = bidder.time
        p0.bidPriceTextView.text = bidder.bid

        if (bidder.winner.equals("true")){

            p0.winnerTextView.visibility = View.VISIBLE
            p0.winnerTextView.text = "(Winner)"

        }else{
            p0.winnerTextView.visibility = View.GONE
        }
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val nameTextView : TextView = itemView.findViewById(R.id.bidder_name_textView)
        val dateTextView : TextView = itemView.findViewById(R.id.bidding_date_textView)
        val timeTextView : TextView = itemView.findViewById(R.id.bidding_time_textView)
        val bidPriceTextView : TextView = itemView.findViewById(R.id.bid_price_textView)
        val winnerTextView : TextView = itemView.findViewById(R.id.winner_textView)
    }
}