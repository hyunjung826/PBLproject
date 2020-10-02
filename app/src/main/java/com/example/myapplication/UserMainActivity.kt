package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_user_main.*


class UserMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main)

        button3.setOnClickListener {
            showSettingPopUp()
        }

        button4.setOnClickListener{
            startActivity(Intent(this, MyLocation::class.java))
        }

        button5.setOnClickListener {
            startActivity(Intent(this,FriendActivity::class.java ))
        }

        button6.setOnClickListener {
            showSettingPopUp1()
        }


    }

    private fun showSettingPopUp() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_call, null)

        val txt_pop5: TextView = view.findViewById(R.id.txt_pop5)
        txt_pop5.text = "'연결' 버튼을 누를 시, 가까운 119로 신고가 가니 신중히 선택하기 바랍니다."

        val alertDialog = AlertDialog.Builder(this).setTitle("주의사항").create()

        val butSave2 = view.findViewById<Button>(R.id.butSave2)

        butSave2.setOnClickListener {

        }

        val butCancel2 = view.findViewById<Button>(R.id.butCancel2)
        butCancel2.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun showSettingPopUp1(){

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_car, null)

        val alertDialog1 = AlertDialog.Builder(this).setTitle("출동 차량 위치 확인").create()

        val butSave3 = view.findViewById<Button>(R.id.butSave3)

        butSave3.setOnClickListener {
            startActivity(Intent(this,CheckCarActivity::class.java ))
        }

        val butCancel3 = view.findViewById<Button>(R.id.butCancel3)
        butCancel3.setOnClickListener {
            alertDialog1.dismiss()
        }
        alertDialog1.setView(view)
        alertDialog1.show()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.action_btn1 -> {
                // 기능 소스
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true}
        }
        return super.onOptionsItemSelected(item)
    }
}