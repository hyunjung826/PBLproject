package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        txt_edit.setOnClickListener{
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        txt_protector.setOnClickListener{
            val intent = Intent(this, PlusProtectorActivity::class.java )
            startActivity(intent)
        }

        txt_protected.setOnClickListener{
            val intent = Intent(this, PlusProtectedActivity::class.java )
            startActivity(intent)
        }

        txt_logout.setOnClickListener{
            val intent = Intent(this, UserLoginActivity::class.java )
            FirebaseAuth.getInstance().signOut()
            startActivity(intent)
        }
    }
}
