package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        reg_button_join_us.setOnClickListener{
            createAndLoginEmail()
        }
    }

    private fun createAndLoginEmail() {
        if (reg_email_et.text.toString().isNullOrEmpty() || reg_password_et.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            return
        }

        var email = reg_email_et.text.toString()
        var password = reg_password_et.text.toString()

        //progressBar2.visibility = View.VISIBLE

        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                //progressBar2.visibility = View.GONE
                if (task.isSuccessful) {
                    //아이디 생성이 성공했을 경우
                    Toast.makeText(this,
                        "회원가입 성공",
                        Toast.LENGTH_LONG).show()
                } else {
                    //회원가입 에러가 발생했을 경우
                    Toast.makeText(this,
                        task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}
