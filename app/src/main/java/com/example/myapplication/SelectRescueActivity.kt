package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_select_rescue.*

class SelectRescueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_rescue)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        txt_firestation2.setOnClickListener{
            val intent = Intent(this, RescueLoginActivity::class.java)
            startActivity(intent)
        }
    }
}