package com.example.muhammadusama.lifecycleawaredemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

//LifecycleOwner
class MainActivity : AppCompatActivity() {

    private val TAG : String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG,"Owner ON_CREATE")

        lifecycle.addObserver(MainActivityObserver())
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"Owner ON_START")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"Owner ON_RESUME")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"Owner ON_PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"Owner ON_STOP")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"Owner ON_DESTROY")
    }

}
