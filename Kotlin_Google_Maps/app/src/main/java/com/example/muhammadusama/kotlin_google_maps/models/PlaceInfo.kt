package com.example.muhammadusama.kotlin_google_maps.models

import android.net.Uri
import com.google.android.gms.maps.model.LatLng

data class PlaceInfo (var name : String? = "", var address : String? = "", var phoneNumber : String? = "", var id : String? = "",
                      var websiteUri : Uri? = null, var latlng : LatLng? = null, var rating : Float? = null,
                      var attributions : String? = "") {
}