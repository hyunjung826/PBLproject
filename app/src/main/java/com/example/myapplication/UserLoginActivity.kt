package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_user_login.*

class UserLoginActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            emailLogin()
//            val intent = Intent(this, LoginResultActivity::class.java)
//            startActivity(intent)
        }

        txt_register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun emailLogin() {
        if (reg_name_et.text.toString().isNullOrEmpty() || reg_password_et.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            return
        }

        var email = reg_name_et.text.toString()
        var password = reg_password_et.text.toString()
        //progressBar.visibility = View.VISIBLE
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                //progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email 로그인 성공", Toast.LENGTH_LONG).show()
                    moveLoginResult(auth?.currentUser)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun moveLoginResult(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, UserMainActivity::class.java))
            finish()
        }

    }
}