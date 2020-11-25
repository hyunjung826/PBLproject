package com.example.myapplication


import android.R.id.edit
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
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

    // 검색어를 입력할 Input 창
    private var editSearch: EditText? = null

    private var arraylist: java.util.ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescue_login)

        list = java.util.ArrayList()

        arraylist = java.util.ArrayList()

        arraylist!!.addAll(list as java.util.ArrayList<String>)

        //검색하는 창
        editSearch = findViewById<View>(R.id.editSearch) as EditText

        //리스트 형식으로 소방서목록 받아온것
        fire_listview.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                val text = editSearch!!.text.toString()
                search(text)
            }
        })

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

    fun search(charText: String) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list!!.clear()

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length == 0) {
            list!!.addAll(arraylist!!)
        } else {
            // 리스트의 모든 데이터를 검색한다.
            for (i in arraylist!!.indices) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist!![i].toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list!!.add(arraylist!![i])
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter?.notifyDataSetChanged()
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

