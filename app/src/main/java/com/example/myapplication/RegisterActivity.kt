package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        reg_button_join_us.setOnClickListener{
            createAndLoginEmail()
        }
    }

    private fun showSettingPopUp2() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_complete_register, null)
        val textView6: TextView = view.findViewById(R.id.txt_pop6)

        textView6.text = "회원가입이 완료되었습니다."

        val alertDialog2 = AlertDialog.Builder(this).create()

        val butSave4 = view.findViewById<Button>(R.id.butSave4)

        butSave4.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java ))
        }
        alertDialog2.setView(view)
        alertDialog2.show()
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
                    showSettingPopUp2()
                } else {
                    //회원가입 에러가 발생했을 경우
                    Toast.makeText(this,
                        task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}
