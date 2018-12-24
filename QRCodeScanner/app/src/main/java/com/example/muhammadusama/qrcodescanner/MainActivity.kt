package com.example.muhammadusama.qrcodescanner

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.util.SparseArray
import android.view.ContextMenu
import android.view.GestureDetector
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var surfaceView :SurfaceView
    private lateinit var textView : TextView
    private lateinit var cameraSource : CameraSource
    private lateinit var barcodeDetector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),1234)
        }

        surfaceView = findViewById(R.id.cameraPreview)
        textView = findViewById(R.id.textView)

        barcodeDetector = BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE).build()

        cameraSource = CameraSource.Builder(this,barcodeDetector)
            .setRequestedPreviewSize(640,480)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setAutoFocusEnabled(true)
            .setRequestedFps(24.toFloat())
            .build()

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback{

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {

                cameraSource.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {

                if(ActivityCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){

                    return
                }
                cameraSource.start(holder)
            }
        })


        barcodeDetector.setProcessor(object :  Detector.Processor<Barcode>{
            override fun release() {

            }

            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {

                val qrCodes : SparseArray<Barcode>? = p0?.detectedItems

                if(qrCodes?.size() != 0){

                    textView.post(object : Runnable {

                        override fun run() {

  /*                          val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrator.vibrate(1000)
  */                          textView.text = qrCodes?.valueAt(0)?.displayValue.toString()
                        }
                    })
                }
            }
        })
    }
}
