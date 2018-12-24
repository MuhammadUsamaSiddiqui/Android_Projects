package com.example.muhammadusama.textdetectorapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var imageView : ImageView? = null
    private var textView : TextView? = null
    private var pickImageButton : Button? = null
    private var detectTextButton : Button? = null
    private var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        pickImageButton = findViewById(R.id.pick_image)
        detectTextButton = findViewById(R.id.detect_text)

        pickImageButton?.setOnClickListener(this)
        detectTextButton?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id){

            R.id.pick_image ->{

                pickImage()

            }
            R.id.detect_text->{

                detectText()
            }
        }
    }

    private fun detectText(){

        if(bitmap ==  null){

            Toast.makeText(this,"Image is null",Toast.LENGTH_SHORT).show()
        }else{

            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap!!)
            val firebaseVisionTextDetector  = FirebaseVision.getInstance().onDeviceTextRecognizer

            firebaseVisionTextDetector.processImage(firebaseVisionImage).addOnSuccessListener{

                processText(it)

            }
        }
    }

    private fun processText(it: FirebaseVisionText?) {

        val blocks : MutableList<FirebaseVisionText.TextBlock>? = it!!.textBlocks

        if(blocks?.size == 0){

            Toast.makeText(applicationContext,"No Text Detected",Toast.LENGTH_SHORT).show()

        }else{

            var text : String = ""

            for(block in it.textBlocks ){

                text += block.text
            }
            textView?.text = text

        }
    }

    private fun pickImage (){

        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){

            val uri = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
                imageView?.setImageBitmap(bitmap)

            }catch (e : IOException){

                e.printStackTrace()
            }
        }
    }
}
