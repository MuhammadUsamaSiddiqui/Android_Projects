package com.example.muhammadusama.kotlin_google_maps

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.muhammadusama.kotlin_google_maps.models.PlaceInfo
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.lang.NullPointerException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private val TAG : String = "MapsActivity"

    // Error if not correct version of Google Play Services in Phone
    private val ERROR_DIALOG_REQUEST : Int = 9001

    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private val DEFAULT_ZOOM = 15f
    private val LAT_LNG_BOUNDS : LatLngBounds = LatLngBounds( LatLng(-40.0, -168.0) , LatLng(71.0,136.0))
    private val PLACE_PICKER_REQUEST = 1

    // Variables
    private var mLocationPermissionsGranted = false
    private lateinit var  mFusedLocationProviderClient :FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private var mPlaceAutocompleteAdapter : PlaceAutocompleteAdapter? = null
    private lateinit var mGoogleApiClient : GoogleApiClient
    private var mPlace : PlaceInfo? = null
    private var mMarker : Marker? = null


    //Widgets
    private var mSearchText : AutoCompleteTextView? = null
    private var mGps : ImageView? = null
    private var mInfo : ImageView? = null
    private var mPlacePicker : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Get a reference to the ConnectivityManager to check state of network connectivity
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get details on the currently active default data network
        val networkInfo = connMgr.activeNetworkInfo

        // If there is a network connection, Ready Map
        if (networkInfo != null && networkInfo.isConnected) {

            if (isServicesOK()) {

                mSearchText = findViewById(R.id.input_search)
                mGps = findViewById(R.id.ic_gps)
                mInfo = findViewById(R.id.place_info)
                mPlacePicker = findViewById(R.id.place_picker)

                getLocationPermission()
            }
        }else{

            Toast.makeText(this@MapsActivity,"No Internet Connection!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init(){

        Log.d(TAG, "init: initializing")

         mGoogleApiClient = GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()


        // Attach Item Click Listener with Auto Complete Text View
        mSearchText?.setOnItemClickListener(mAutocompleteClickListener)

        // Attach Place Auto Complete Adapter with Auto Complete Text View
        mPlaceAutocompleteAdapter = PlaceAutocompleteAdapter(this,mGoogleApiClient,LAT_LNG_BOUNDS, null)
        mSearchText?.setAdapter(mPlaceAutocompleteAdapter)


        // Call when some action is performed
        mSearchText?.setOnEditorActionListener(object :TextView.OnEditorActionListener{

            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event?.action == KeyEvent.ACTION_DOWN
                        || event?.action == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate()
                    hideSoftKeyboard()
                }
                return false
            }
        })

        // When GPS ImageView Clicked
        mGps?.setOnClickListener{

            Log.d(TAG, "onClick: clicked gps icon")
            mMap.clear()
            getDeviceLocation()
        }

        // When Someone tap on the map
        mMap.setOnMapClickListener {

            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title("Latitiude : ${it.latitude} , Longitude : ${it.longitude}"))

        }

        // When Info ImageView Clicked
        mInfo?.setOnClickListener{

            Log.d(TAG, "onClick: clicked place info")

            try{

                if(mMarker?.isInfoWindowShown!!){

                    mMarker?.hideInfoWindow()

                }else{

                    Log.d(TAG, "onClick: place info: " + mPlace.toString())
                    mMarker?.showInfoWindow()
                }

            }catch (e : NullPointerException){

                Log.e(TAG, "onClick: NullPointerException: " + e.message)
            }
        }

        // When PlacePicker ImageView Clicked
        mPlacePicker?.setOnClickListener {

            val builder = PlacePicker.IntentBuilder()

            try {
                startActivityForResult(builder.build(this@MapsActivity), PLACE_PICKER_REQUEST)

            }catch (e : GooglePlayServicesRepairableException){

                Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.message)

            }catch (e : GooglePlayServicesNotAvailableException){

                Log.e(TAG, "onClick: GooglePlayServicesNotAvailableException: " + e.message)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == PLACE_PICKER_REQUEST){

            if(resultCode == RESULT_OK){

               val place = PlacePicker.getPlace(this,data)

                //Submit a request for Place Object
                var placeResult : PendingResult<PlaceBuffer> = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient,place.id)

                placeResult.setResultCallback(mUpdatePlaceDetailsCallback)
            }
        }
    }

    // Geo Locating the Search String that you enter in the Search Field
    private fun geoLocate() {

        Log.d(TAG, "geoLocate: geolocating")

        val searchString = mSearchText?.text.toString()

        val geocoder = Geocoder(this)
        var list : List<Address> = ArrayList()
        try{

            list = geocoder.getFromLocationName(searchString,1) // here 1 is maximum Result
        }catch (e : IOException){
            Log.e(TAG, "geoLocate: IOException: " + e.message)
        }

        if(list.size > 0){

            val address : Address = list.get(0)
            Log.d(TAG, "geoLocate: found a location: " + address.toString())

        moveCamera(LatLng(address.latitude,address.longitude),DEFAULT_ZOOM,address.getAddressLine(0))
        }

    }

    private fun initMap() {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
        /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {

            Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onMapReady: map is ready")

            mMap = googleMap

            if(mLocationPermissionsGranted){

                getDeviceLocation()
                init()
            }
    }

    // To get the current Location of the device
    private fun getDeviceLocation (){

        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try{

            if(mLocationPermissionsGranted){

                val location = mFusedLocationProviderClient.lastLocation

                location.addOnCompleteListener {

                    if(it.isSuccessful){

                        Log.d(TAG, "onComplete: found location!")

                        val currentLocation = it.result

                        moveCamera(LatLng(currentLocation?.latitude!!, currentLocation?.longitude),DEFAULT_ZOOM,"My Location")

                        mMap.isMyLocationEnabled = true
                        mMap.uiSettings.isMyLocationButtonEnabled = false

                    }else{
                        Log.d(TAG, "onComplete: current location is null")
                        Toast.makeText(this, "unable to get current location", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }catch (e : SecurityException){

            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.message )

        }
    }

    private fun moveCamera(latLng : LatLng, zoom : Float, placeInfo : PlaceInfo){

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0F))

        mMap.clear()

        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this@MapsActivity))

        if(placeInfo != null){

            try {

                val snippet = "Address: ${placeInfo.address}" + "\n" +
                        "Phone Number: ${placeInfo.phoneNumber}" + "\n" +
                        "Website: ${placeInfo.websiteUri}" + "\n" +
                        "Price Rating: ${placeInfo.rating}" + "\n"

               val markerOptions : MarkerOptions = MarkerOptions()
                       .position(latLng)
                       .title(placeInfo.name)
                       .snippet(snippet)

               mMarker = mMap.addMarker(markerOptions)

            }catch (e : NullPointerException){

                Log.e(TAG, "moveCamera: NullPointerException: " + e.message)

            }
        }else{
            mMap.addMarker(MarkerOptions().position(latLng))
        }
    }

    private fun moveCamera(latLng : LatLng, zoom : Float, title : String){

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0F))

        if(!title.equals("My Location")){

            var markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(title)

            mMap.addMarker(markerOptions)
        }

    }

    // Verifying Permissions Explicitly
    private fun getLocationPermission(){
        val permissions =
                arrayOf<String>(FINE_LOCATION,COARSE_LOCATION)

        if(ContextCompat.checkSelfPermission(this.applicationContext,FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.applicationContext,COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                mLocationPermissionsGranted = true
                initMap()

            }else{

                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE)
            }

        }else{

            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        mLocationPermissionsGranted = false

        when(requestCode){

            LOCATION_PERMISSION_REQUEST_CODE ->{

                if(grantResults.size > 0) {

                    for(i in 0..grantResults.size-1){

                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false
                            Log.d(TAG, "onRequestPermissionsResult: permission failed")
                            return
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult: permission granted")
                    mLocationPermissionsGranted = true

                    //initialize our map
                    initMap()
                }
            }
        }
    }

    // To check that correct version of Google Services is available in phone
    private fun isServicesOK() : Boolean {

        Log.d(TAG,"isServicesOK : checking google services version")

        val available  = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        if(available == ConnectionResult.SUCCESS){
            //Everything is fine and user can make map request
            Log.d(TAG,"isServicesOK : Google Play Servies is Working")
            return true

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){

            // An error occured but we can resolve it
            Log.d(TAG,"isServicesOK : An error occured but we can resolve it")
            var dialog : Dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST)
            dialog.show()

        }else{
            Toast.makeText(this,"You can't make map requests",Toast.LENGTH_SHORT).show()
        }
        return false
    }

    // Hide Keyboard
    private fun hideSoftKeyboard(){

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }
    /*
       --------------------------- google places API autocomplete suggestions -----------------
    */

    private var mAutocompleteClickListener : AdapterView.OnItemClickListener = object : AdapterView.OnItemClickListener{

        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            hideSoftKeyboard()
            mSearchText?.text = null

            val item : AutocompletePrediction = mPlaceAutocompleteAdapter?.getItem(position)!!
            val placeID : String = item.placeId!!

            //Submit a request for Place Object
            var placeResult : PendingResult<PlaceBuffer> = Places.GeoDataApi
                   .getPlaceById(mGoogleApiClient,placeID)

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback)
        }
    }

    // Give us a Place Object
    private var mUpdatePlaceDetailsCallback : ResultCallback<PlaceBuffer> = object :ResultCallback<PlaceBuffer>{

        override fun onResult(p0: PlaceBuffer) {

            if(!p0.status.isSuccess){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + p0.getStatus().toString())

                //Release Place Buffer Object to prevent memory leaks
                p0.release()
                return
            }

            val place : Place = p0.get(0)

            try{

                mPlace = PlaceInfo(place.name.toString(),place.address.toString(),place.phoneNumber.toString()
                        ,place.id,place.websiteUri,place.latLng,place.rating,place.attributions.toString())

                Log.d(TAG,"onResult: place: " + mPlace.toString())

            }catch (e : NullPointerException){

                Log.e(TAG, "onResult: NullPointerException: " + e.message)
            }

            moveCamera(LatLng(place.viewport?.center?.latitude!!, place.viewport?.center?.longitude!!),DEFAULT_ZOOM,
                    mPlace!!)
            p0.release()

        }

    }
}
