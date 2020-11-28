package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class firestation_test : AppCompatActivity() {

    private var firestore: FirebaseFirestore? = null
    var facilityName : String? = null
    var latitude: String? = null
    var longitude: String? = null
    var addrNm: String? = null
    var tel: String? = null
    var fax: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firestation_test)

        StrictMode.enableDefaults()
        var status1: String? = null //파싱된 결과확인!
        var inFacilityName = false
        var inLatitude = false
        var inLongitude = false
        var inAddrNm = false
        var inTel = false
        var inFax = false

        try {
            val url = URL(
                "http://openapi.safekorea.go.kr/openapi/service/firestation/item?"
                        + "&ServiceKey="
                        + "XNuagU2meF4n8h4A3ZORYikPkK%2F7nDmE30cEYtzdnUlStW5ViNKwDYTHb1gRptlPciZIMCcvfznKdD7lYGxCRw%3D%3D"
            ) //검색 URL부분
            val parserCreator = XmlPullParserFactory.newInstance()
            val parser = parserCreator.newPullParser()
            parser.setInput(url.openStream(), null)
            var parserEvent = parser.eventType
            println("파싱시작합니다.")
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                when (parserEvent) {
                    XmlPullParser.START_TAG -> {
//                        if ((parser.name == "firestation_addr_code")) { //title 만나면 내용을 받을수 있게 하자
//                            inFirestationAddrCode = true
//                        }
//                        if ((parser.name == "resultCode")) { //title 만나면 내용을 받을수 있게 하자
//                            inResultCode = true
//                        }
                        if ((parser.name == "facilityName")) { //mapx 만나면 내용을 받을수 있게 하자
                            inFacilityName = true
                        }
                        if ((parser.name == "latitude")) { //mapy 만나면 내용을 받을수 있게 하자
                            inLatitude = true
                        }
                        if ((parser.name == "longitude")) { //mapy 만나면 내용을 받을수 있게 하자
                            inLongitude = true
                        }
                        if ((parser.name == "addrNm")) { //mapy 만나면 내용을 받을수 있게 하자
                            inAddrNm = true
                        }
                        if ((parser.name == "tel")) { //mapy 만나면 내용을 받을수 있게 하자
                            inTel = true
                        }
                        if ((parser.name == "fax")) { //mapy 만나면 내용을 받을수 있게 하자
                            inFax = true
                        }
                        if ((parser.name == "message")) { //message 태그를 만나면 에러 출력
                            status1 = status1 + "에러"
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                    }

                    XmlPullParser.TEXT -> {
//                        if (inFirestationAddrCode) { //isTitle이 true일 때 태그의 내용을 저장.
//                            firestation_addr_code = parser.text
//                            inFirestationAddrCode = false
//                        }
//                        if (inResultCode) { //isTitle이 true일 때 태그의 내용을 저장.
//                            resultCode = parser.text
//                            inResultCode = false
//                        }
                        val FirestationDTO = FirestationDTO(
                            //resultCode,
                            facilityName,
                            latitude,
                            longitude,
                            addrNm,
                            tel,
                            fax
                        )
                        if (inFacilityName) { //isAddress이 true일 때 태그의 내용을 저장.
                            FirestationDTO.facilityName = parser.text
                            inFacilityName = false
                        }
                        if (inLatitude) { //isMapy이 true일 때 태그의 내용을 저장.
                            FirestationDTO.latitude = parser.text
                            inLatitude = false
                        }
                        if (inLongitude) { //isMapy이 true일 때 태그의 내용을 저장.
                            FirestationDTO.longitude = parser.text
                            inLongitude = false
                        }
                        if (inAddrNm) { //isMapy이 true일 때 태그의 내용을 저장.
                            FirestationDTO.addrNm = parser.text
                            inAddrNm = false
                        }
                        if (inTel) { //isMapy이 true일 때 태그의 내용을 저장.
                            FirestationDTO.tel = parser.text
                            inTel = false
                        }
                        if (inFax) { //isMapy이 true일 때 태그의 내용을 저장.
                            FirestationDTO.fax = parser.text
                            inFax = false
                        }
                        val document = facilityName.toString()

                        firestore = FirebaseFirestore.getInstance()

                        firestore?.collection("FirestationList")?.document(document)
                            ?.set(FirestationDTO)?.addOnCompleteListener { task ->
                                //progressBar7.visibility = View.GONE
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "완료", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                    }
//                    XmlPullParser.END_TAG -> if ((parser.name == "item")) {
//                            status1 =
//                                (status1
//                                        //+ "법정동코드 : " + firestation_addr_code + "\n "
//                                        //+ "결과코드 : " + resultCode+ "\n "
//                                        + "시설물명 : " + facilityName + "\n "
//                                        + "위도 : " + latitude + "\n "
//                                        + "경도 : " + longitude + "\n "
//                                        + "주소 : " + addrNm + "\n "
//                                        + "연락처 : " + tel + "\n "
//                                        + "팩스번호 : " + fax
//                                        + "\n")
//                    }
                }

                parserEvent = parser.next()
            }
        } catch (e: Exception) {
            if (status1 != null) {
                //status1.text = "에러가..났습니다..."
            }
        }
    }

}