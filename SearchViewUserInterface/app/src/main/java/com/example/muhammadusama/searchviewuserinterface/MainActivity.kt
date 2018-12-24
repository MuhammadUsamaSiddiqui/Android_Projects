package com.example.muhammadusama.searchviewuserinterface

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var arrowImageView : ImageView? = null
    private var isNotExpanded : Boolean = true
    private var  expandedLinearLayout : LinearLayout? =null
    private var  linearLayout : LinearLayout? =null
    private var upToDown : Animation? = null
    private var bottomToUp : Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrowImageView = findViewById(R.id.ic_downward_arrow)
        expandedLinearLayout = findViewById(R.id.Expanded_Linear_Layout)
        linearLayout = findViewById(R.id.Linear_Layout)

        arrowImageView?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.ic_downward_arrow->{

                if(isNotExpanded){

                    expandedLinearLayout?.visibility = View.VISIBLE
                    upToDown = AnimationUtils.loadAnimation(this,R.anim.up_to_down)
                    linearLayout?.startAnimation(upToDown)
                    arrowImageView?.setImageResource(R.drawable.ic_upward_arrow)
                    isNotExpanded = false

                }else{

                    bottomToUp = AnimationUtils.loadAnimation(this,R.anim.bottom_to_up)

                    bottomToUp?.setAnimationListener(object : Animation.AnimationListener{
                        override fun onAnimationRepeat(animation: Animation?) {

                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            expandedLinearLayout?.visibility = View.GONE

                        }

                        override fun onAnimationStart(animation: Animation?) {
                        }


                    })
                    linearLayout?.startAnimation(bottomToUp)
                    arrowImageView?.setImageResource(R.drawable.ic_downward_arrow)
                    isNotExpanded = true

                }
            }
        }
    }
}
