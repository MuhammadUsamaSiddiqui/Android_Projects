package com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes

import java.io.Serializable

class Phone (var userID : String = "",var pushKey : String = "", var ram : String = "" , var memory : String = ""
             , var model : String = "", var processor : String = "" , var version : String = "",
             var manufacturer : String = "", var camera : String = "",var imageUrl : String = "", var startTime : String = "",
             var endTime : String ="" , var date : String = "", var minimumBid : String = "", var status : String = "",
             var winnerId : String = "") : Serializable {
}