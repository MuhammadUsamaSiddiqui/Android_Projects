package com.example.muhammadusama.kotlin_google_maps

import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import android.view.LayoutInflater
import android.widget.TextView


class CustomInfoWindowAdapter(): GoogleMap.InfoWindowAdapter {

    private var mWindow: View? = null
    private var mContext: Context? = null

    constructor(context: Context) : this (){

        mContext = context
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)
    }

    private fun rendowWindowText(marker : Marker? , view : View?){

        val title= marker?.title
        val titleTextView : TextView = view?.findViewById(R.id.title)!!

        if(!title .equals("")){

            titleTextView.text = title
        }

        val snippet= marker?.snippet
        val snippetTextView : TextView = view.findViewById(R.id.snippet)

        if(!snippet.equals("")){

            snippetTextView.text = snippet
        }
    }
    override fun getInfoContents(p0: Marker?): View? {

      rendowWindowText(p0,mWindow)
        return mWindow
    }

    override fun getInfoWindow(p0: Marker?): View? {

        rendowWindowText(p0,mWindow)
        return mWindow
    }
}