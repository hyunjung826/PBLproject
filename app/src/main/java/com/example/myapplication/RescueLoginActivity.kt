package com.example.myapplication


import android.R.id.edit
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_my_location.*
import kotlinx.android.synthetic.main.activity_rescue_login.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL


class RescueLoginActivity : Activity(){

    //List Dialog Adapter
    lateinit var adapter: ArrayAdapter<String>
    var list = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescue_login)

        fire_listview.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

//        editTextFilter.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val filterText = edit.toString()
//                if (filterText.isNotEmpty()) {
//                    fire_listview.setFilterText(filterText)
//                } else {
//                    fire_listview.clearTextFilter()
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//        })

//        fire_listview.setOnClickListener {
//            val intent = Intent(this, RescueNumberActivity::class.java)
//            startActivity(intent)
//        }

        StrictMode.enableDefaults()

        var inTitle = false
        var facilityName: String? = null

        try {
            val url = URL(
                "http://openapi.safekorea.go.kr/openapi/service/firestation/item?"
                        + "&ServiceKey="
                        + "3dItxsHHzQJiyIitvKVQUr8%2ByHe5Qoov%2F5P2PbQ5IHpGhY5DBOO5J05edGISs%2BpNk1vMZzGX4K5XD1VvLBIIjw%3D%3D"
            ) //검색 URL부분
            val parserCreator = XmlPullParserFactory.newInstance()
            val parser = parserCreator.newPullParser()
            parser.setInput(url.openStream(), null)
            var parserEvent = parser.eventType
            println("파싱시작합니다.")

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                when (parserEvent) {
                    XmlPullParser.START_TAG -> {
                        if ((parser.name == "facilityName")) { //기관명 만나면 내용을 받을수 있게 하자
                            inTitle = true
                        }
                    }
                    XmlPullParser.TEXT -> {
                        if (inTitle) { //isMapx이 true일 때 태그의 내용을 저장.
                            facilityName = parser.text.toString()
                            inTitle = false
                        }
                    }
                    XmlPullParser.END_TAG -> if ((parser.name == "item")) {
                        if (facilityName != null) {
                            list.add(facilityName)
                        }
                    }
                }
                parserEvent = parser.next()

            }
        } catch (e: Exception) {
            result_firestation.text = "에러가..났습니다..."

        }


        //Adapter
        adapter = ArrayAdapter(this, android.R.layout.select_dialog_singlechoice)
        adapter.addAll(list)
        adapter.notifyDataSetChanged()

        select_firestation.setOnClickListener {
            CreateListDialog()
        }

    }



    //리스트 다이얼로그 생성
    fun CreateListDialog() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("소방서목록") //타이틀

        val checkedItem = -1 //맨 처음 아무것도 체크 안된 상태

        //어답터 , 클릭이벤트 설정
        alert.setSingleChoiceItems(adapter, checkedItem){ dialog, which -> }
        alert.setPositiveButton("OK") { dialog, which -> }

        alert.setAdapter(adapter, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                val firestation = adapter.getItem(which)
                result_firestation.setText(firestation)
                //Toast.makeText(this@RescueLoginActivity, "선택한 소방서 : " + firestation, Toast.LENGTH_SHORT).show()
            }
        })
        alert.show()
    }


}

