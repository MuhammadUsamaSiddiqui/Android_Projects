package com.example.muhammadusama.kotlinrealtimeauction.Authentication.auctioneer

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.text.format.DateFormat
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.example.muhammadusama.kotlinrealtimeauction.Authentication.data.classes.Phone
import com.example.muhammadusama.kotlinrealtimeauction.R
import com.example.muhammadusama.kotlinrealtimeauction.R.mipmap.calendar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AuctionPhoneActivity : AppCompatActivity(), View.OnClickListener , DatePickerDialog.OnDateSetListener
        , TimePickerDialog.OnTimeSetListener{

    private val PICK_IMAGE_REQUEST = 1
    private var mImageUri: Uri? = null

    private var ramEditText: EditText? = null
    private var memoryEditText: EditText? = null
    private var modelEditText: EditText? = null
    private var versionEditText: EditText? = null
    private var manufacturerEditText: EditText? = null
    private var minimumBidEditText: EditText? = null
    private var processorEditText: EditText? = null
    private var cameraEditText: EditText? = null

    private var dateTextView : TextView? = null
    private var timeTextView : TextView? = null
    private var durationTextView : TextView? = null

    private var dateImageView : ImageView? = null
    private var timeImageView : ImageView? = null
    private var durationImageView : ImageView? = null
    private var itemImageView : ImageView? = null

    private var postButon : Button? = null

    private var auctionDate : String = ""
    private var auctionStartTime : String = ""
    private var auctionEndTime : String = ""
    private var auctionDuration : String = ""
    private var imageUrl : String = ""

    private var auctionYear : Int? = null
    private var auctionMonth : Int? = null
    private var auctionDay : Int? = null
    private var auctionStartHour : Int? = null
    private var auctionStartMinute : Int? = null
    private var auctionEndHour : Int? = null

    private var dateFormat: SimpleDateFormat? = null
    private var timeFormat:SimpleDateFormat? = null

    private var calendar: Calendar? = null

    private var mFirebaseDatabase: FirebaseDatabase? = null
    private var mItemsDatabaseReference: DatabaseReference? = null
    private var mStorage: FirebaseStorage? = null
    private var mStorageRef: StorageReference? = null
    private var mAuth : FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auction_item)

        var phoneLayout : LinearLayout = findViewById(R.id.phone_layout)
        phoneLayout.visibility = View.VISIBLE

        //Date and Time Formats
        dateFormat = SimpleDateFormat("dd/MM/yyyy")
        timeFormat = SimpleDateFormat("HH:mm")

        // Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mItemsDatabaseReference = mFirebaseDatabase?.getReference()?.child("Phone")
        mStorage = FirebaseStorage.getInstance()
        mStorageRef = mStorage?.getReference("items_images")
        mAuth = FirebaseAuth.getInstance()

        //Edit Text
        ramEditText = findViewById(R.id.editTextRam)
        memoryEditText = findViewById(R.id.editTextMemory)
        modelEditText = findViewById(R.id.editTextModel)
        versionEditText = findViewById(R.id.editTextVersion)
        manufacturerEditText = findViewById(R.id.editTextManufacturer)
        minimumBidEditText = findViewById(R.id.editTextMinimumBid)
        processorEditText = findViewById(R.id.editTextProcessor)
        cameraEditText = findViewById(R.id.editTextCamera)

        //Text View
        dateTextView = findViewById(R.id.date_textView)
        timeTextView = findViewById(R.id.time_textView)
        durationTextView = findViewById(R.id.duration_textView)

        //Image View
        dateImageView = findViewById(R.id.calender_image)
        timeImageView = findViewById(R.id.clock_image)
        durationImageView = findViewById(R.id.durationClock_image)
        itemImageView = findViewById(R.id.item_image)

        //Button
        postButon = findViewById(R.id.post_Button)

        dateImageView?.setOnClickListener(this)
        timeImageView?.setOnClickListener(this)
        durationImageView?.setOnClickListener(this)
        postButon?.setOnClickListener(this)
        itemImageView?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.post_Button ->{

                post()
            }

            R.id.calender_image ->{

                displayDatePickerDialog()
            }

            R.id.clock_image ->{

                if (auctionDate.equals("")){

                    Toast.makeText(this, "Select Date First!", Toast.LENGTH_SHORT).show()
                }else{

                    displayTimePickerDialog()
                }
            }
            R.id.durationClock_image ->{

                if (auctionDate.equals("") && auctionStartTime.equals("")){

                    Toast.makeText(this, "Select Date and Time First!", Toast.LENGTH_SHORT).show()


                }else if(auctionStartTime.equals("")){

                    Toast.makeText(this, "Select Time First!", Toast.LENGTH_SHORT).show()

                }else{
                    durationDialog()
                }
            }

            R.id.item_image->{

                openImagePicker()

            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {

            mImageUri = data.data
            Picasso.get().load(mImageUri).fit().centerCrop().into(itemImageView)
            uploadImage()
        }
    }

    private fun uploadImage() {

        if (mImageUri != null) {

            val fileReference = mStorageRef?.child(System.currentTimeMillis().toString()
                    + "." + getFileExtension(mImageUri!!))

            fileReference?.putFile(mImageUri!!)
                    ?.addOnSuccessListener {

                        fileReference.getDownloadUrl().addOnCompleteListener { task ->

                            imageUrl = task.result.toString()

                        }
                    }
                    ?.addOnFailureListener { e ->
                        Toast.makeText(this@AuctionPhoneActivity, e.message, Toast.LENGTH_SHORT).show()

                    }
        }
    }
    private fun getFileExtension(mImageUri: Uri): String {

        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(mImageUri))
    }

    private fun post() {

        val ram = ramEditText?.text.toString().trim()
        val memory = memoryEditText?.text.toString().trim()
        val model = modelEditText?.text.toString().trim()
        val processor = processorEditText?.text.toString().trim()
        val manufacturer = manufacturerEditText?.text.toString().trim()
        val version = versionEditText?.text.toString().trim()
        val camera = cameraEditText?.text.toString().trim()
        val minimumBid = minimumBidEditText?.text.toString().trim()
        var userId = mAuth?.currentUser?.uid

        if(imageUrl.equals("")){

            Toast.makeText(this,"Kindly Upload the Item Image!",Toast.LENGTH_SHORT).show()
            return
        }
        if (ram.isEmpty()) {

            ramEditText?.error = "Enter RAM"
            ramEditText?.requestFocus()
            return
        }
        if (memory.isEmpty()) {

            memoryEditText?.error = "Enter Memory!"
            memoryEditText?.requestFocus()
            return
        }
        if (model.isEmpty()) {

            modelEditText?.error = "Enter Model"
            modelEditText?.requestFocus()
            return
        }
        if (processor.isEmpty()) {

            processorEditText?.error = "Enter Processor!"
            processorEditText?.requestFocus()
            return
        }
        if (manufacturer.isEmpty()) {

            manufacturerEditText?.error = "Enter Manufacturer!"
            manufacturerEditText?.requestFocus()
            return
        }
        if (version.isEmpty()) {

            versionEditText?.error = "Enter Version!"
            versionEditText?.requestFocus()
            return
        }
        if (camera.isEmpty()) {

            cameraEditText?.error = "Enter Camera!"
            cameraEditText?.requestFocus()
            return
        }
        if (minimumBid.isEmpty()) {

            minimumBidEditText?.error = "Enter Minimum Bid!"
            minimumBidEditText?.requestFocus()
            return
        }

        if (auctionDate.equals("") || auctionStartTime.equals("") || auctionDuration.equals("")) {

            Toast.makeText(this, "Kindly Select Date, Time and Duration!", Toast.LENGTH_SHORT).show()
            return
        }

        try {

            calculateEndTime()

            val date = dateFormat?.parse(auctionDate)

            if (calendar?.timeInMillis!! < Calendar.getInstance().timeInMillis && !date?.after(Date())!!) {

                Toast.makeText(this, "Invalid Time Selection", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this@AuctionPhoneActivity, "End: $auctionEndTime", Toast.LENGTH_SHORT).show()

                val key = mItemsDatabaseReference?.push()?.key.toString()
                var phone : Phone = Phone(userId!!,key,ram, memory, model, processor, version, manufacturer, camera, imageUrl,
                        auctionStartTime,auctionEndTime,auctionDate,minimumBid,"UNSOLD")

                    mItemsDatabaseReference?.child(key)?.setValue(phone)

                resetUserInterface()
            }
        } catch (e : ParseException) {
            e.printStackTrace();
        }

    }

    private fun resetUserInterface() {

        ramEditText?.text = null
        memoryEditText?.text = null
        modelEditText?.text = null
        processorEditText?.text = null
        manufacturerEditText?.text = null
        versionEditText?.text = null
        cameraEditText?.text = null
        minimumBidEditText?.text = null

        dateTextView?.text = null
        timeTextView?.text = null
        durationTextView?.text = null

        dateTextView?.hint = "AUCTION DATE"
        timeTextView?.hint = "TIME"
        durationTextView?.hint = "DURATION"

        Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(itemImageView)

    }

    private fun calculateEndTime() {

        try {
            auctionEndHour = auctionStartHour!! + Integer.parseInt(auctionDuration)
            auctionEndTime = "$auctionEndHour:$auctionStartMinute"

            val endTime = timeFormat?.parse(auctionEndTime)

            auctionEndTime = timeFormat?.format(endTime)!!

            val maxTime = "24:00"

            if (maxTime.compareTo(auctionEndTime) < 0) {

                auctionEndHour = auctionStartHour!! + Integer.parseInt(auctionDuration) - 24
                auctionEndTime = "$auctionEndHour:$auctionStartMinute"

            }
        }catch (e : ParseException) {
                e.printStackTrace();
        }
    }

    private fun displayDatePickerDialog() {

        val day: Int
        val month: Int
        val year: Int

        val calendar = Calendar.getInstance()

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, this, year, month, day)
        datePickerDialog.setTitle("Auction Date")
        // Disable Past Dates
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        auctionYear = year
        auctionMonth = month + 1
        auctionDay= dayOfMonth

        auctionDate = "$auctionDay/$auctionMonth/$auctionYear"
        val date = dateFormat?.parse(auctionDate)
        auctionDate = dateFormat?.format(date)!!

        dateTextView?.setText(auctionDate)

        Toast.makeText(this, "Date: $auctionDate", Toast.LENGTH_SHORT).show()

    }

    private fun displayTimePickerDialog() {

        val hour: Int
        val minute: Int

        val calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, this, hour, minute, DateFormat.is24HourFormat(this))
        timePickerDialog.setTitle("AuctionTime")
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        try {

            calendar = Calendar.getInstance()
            calendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar?.set(Calendar.MINUTE, minute)

            val date = dateFormat!!.parse(auctionDate)

            if (calendar?.timeInMillis!! < Calendar.getInstance().timeInMillis && !date?.after(Date())!!) {

                Toast.makeText(this, "Invalid Time Selection", Toast.LENGTH_SHORT).show()

                val timePickerDialog = TimePickerDialog(this, this, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), DateFormat.is24HourFormat(this))
                timePickerDialog.setTitle("Auction Time")
                timePickerDialog.show()
            } else {

                auctionStartHour = hourOfDay
                auctionStartMinute = minute

                auctionStartTime = "$auctionStartHour:$auctionStartMinute"

                val startTime = timeFormat?.parse(auctionStartTime)

                auctionStartTime = timeFormat?.format(startTime)!!

                timeTextView?.setText(auctionStartTime)

                Toast.makeText(this, "start $auctionStartTime", Toast.LENGTH_SHORT).show()
            }
        }catch (e : ParseException) {
            e.printStackTrace()
        }

    }

    private fun durationDialog() {

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogueView = inflater.inflate(R.layout.duration_dialog, null)

        builder.setView(dialogueView)

        builder.setTitle(R.string.duration)

        val durationDialogue = builder.create()
        durationDialogue.show()

        val okButton = dialogueView.findViewById(R.id.ok_button) as Button
        val durationSpinner = dialogueView.findViewById(R.id.duration_spinner) as Spinner

        // Spinner Drop down elements
        val duration = ArrayList<String>()
        duration.add("1")
        duration.add("2")
        duration.add("3")
        duration.add("4")
        duration.add("5")
        duration.add("6")
        duration.add("7")
        duration.add("8")
        duration.add("9")
        duration.add("10")
        duration.add("11")
        duration.add("12")
        duration.add("13")
        duration.add("14")
        duration.add("15")
        duration.add("16")
        duration.add("17")
        duration.add("18")
        duration.add("19")
        duration.add("20")
        duration.add("21")
        duration.add("22")
        duration.add("23")
        duration.add("24")

        // Creating adapter for spinner
        val durationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, duration)

        // Drop down layout style - list view with radio button
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        durationSpinner.adapter = durationAdapter

        durationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                auctionDuration = parent.getItemAtPosition(position).toString()

                durationTextView?.setText("$auctionDuration Hour(s)")

                Toast.makeText(this@AuctionPhoneActivity, "Booking Duration : $auctionDuration Hour(s)", Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

                Toast.makeText(this@AuctionPhoneActivity, "Select the Duration!", Toast.LENGTH_SHORT).show()
            }
        }

        okButton.setOnClickListener{
            Toast.makeText(this@AuctionPhoneActivity, "Booking Duration : $auctionDuration Hour(s)", Toast.LENGTH_SHORT).show()
            durationDialogue.dismiss()
        }
    }
}
