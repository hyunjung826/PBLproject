package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_rescue_number.*

class RescueNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescue_number)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        btn_finish_number.setOnClickListener{
            val intent = Intent(this, RescueMainActivity::class.java)
            startActivity(intent)
        }
    }
}