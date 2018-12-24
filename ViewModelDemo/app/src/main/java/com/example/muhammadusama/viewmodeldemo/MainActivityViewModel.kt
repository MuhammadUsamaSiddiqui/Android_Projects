package com.example.muhammadusama.viewmodeldemo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import java.util.*

class MainActivityViewModel : ViewModel(){

    private val TAG : String = this.javaClass.simpleName

    //Create an instance of live data
    // Since Live data does not provide any public method to update the store data, that's why we use MutableLiveData
    private var myRandomNumber : MutableLiveData<String>? = null


    fun getNumber () : MutableLiveData<String>? {

        Log.i(TAG,"Get Number")

        if(myRandomNumber == null){

            myRandomNumber = MutableLiveData()
            createNumber()
        }

        return myRandomNumber
    }


    fun createNumber() {

        Log.i(TAG,"Create new Number")

        val random = Random()
        // Generate Random Number from 1-10
        myRandomNumber?.value = "Number: " + (random.nextInt(10 - 1) + 1)
    }

    // When View Model is Destroyed
    override fun onCleared() {
        super.onCleared()

        Log.i(TAG,"ViewModel Destroyed")
    }
}