package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class RescueLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescue_login)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        /*butfire.setOnClickListener{
            startActivity(Intent(this, MainActivityRescue::class.java))
        }*/
    }
}