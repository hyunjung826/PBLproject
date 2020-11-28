package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_plus_protected.*
import kotlinx.android.synthetic.main.activity_register.*

class PlusProtectedActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_plus_protected)

        auth = FirebaseAuth.getInstance()

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        btn_finish_protected.setOnClickListener {
            createPlusProtected()
        }
    }

    private fun createPlusProtected() {
        if (txt_protected_number.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "보호대상이 생성한 인증번호 6자리를 입력해주세요.", Toast.LENGTH_LONG).show()
            return
        }
        else{
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}