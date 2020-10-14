package com.example.myapplication


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rescue_login.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL


class RescueLoginActivity : Activity() {

    private var firestore: FirebaseFirestore? = null

    val user = FirebaseAuth.getInstance().currentUser



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescue_login)

        val db = FirebaseFirestore.getInstance()

        StrictMode.enableDefaults()
        val status1 = findViewById<View>(R.id.result) as TextView //파싱된 결과확인!
        var initem = false
        var inEntid = false
        var inSiGunGu = false
        var inTitle = false
        var inDelegaTee = false
        var inRoadAddress = false
        var inHomePage = false
        var inTel = false
        var inFax = false
        var entid: String? = null
        var sigungu: String? = null
        var title: String? = null
        var delegatee: String? = null
        var roadaddress: String? = null
        var homepage: String? = null
        var tel: String? = null
        var fax: String? = null
        try {
            val url = URL(
                "http://apis.data.go.kr/6480000/gyeongnamfirestation/gyeongnamfirestationList?"
                        + "&pageNo=1&numOfRows=10&ServiceKey="
                        + "2k6I7GQsBtOms%2BiHgBGPMUfdEhprKWAXD0QURVgIP%2BPbNRZnTf5wzBnpTO6NeTFhSs%2FueWtsTZA59b3R68AjDg%3D%3D"
            ) //검색 URL부분
            val parserCreator = XmlPullParserFactory.newInstance()
            val parser = parserCreator.newPullParser()
            parser.setInput(url.openStream(), null)
            var parserEvent = parser.eventType
            println("파싱시작합니다.")
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                when (parserEvent) {
                    XmlPullParser.START_TAG -> {
                        if ((parser.name == "entid")) { //title 만나면 내용을 받을수 있게 하자
                            inEntid = true
                        }
                        if ((parser.name == "sigungu")) { //address 만나면 내용을 받을수 있게 하자
                            inSiGunGu = true
                        }
                        if ((parser.name == "title")) { //mapx 만나면 내용을 받을수 있게 하자
                            inTitle = true
                        }
                        if ((parser.name == "delegatee")) { //mapy 만나면 내용을 받을수 있게 하자
                            inDelegaTee = true
                        }
                        if ((parser.name == "roadaddress")) { //mapy 만나면 내용을 받을수 있게 하자
                            inRoadAddress = true
                        }
                        if ((parser.name == "homepage")) { //mapy 만나면 내용을 받을수 있게 하자
                            inHomePage = true
                        }
                        if ((parser.name == "tel")) { //mapy 만나면 내용을 받을수 있게 하자
                            inTel = true
                        }
                        if ((parser.name == "fax")) { //mapy 만나면 내용을 받을수 있게 하자
                            inFax = true
                        }
                        if ((parser.name == "message")) { //message 태그를 만나면 에러 출력
                            status1.text = status1.text.toString() + "에러"
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                    }
                    XmlPullParser.TEXT -> {
                        if (inEntid) { //isTitle이 true일 때 태그의 내용을 저장.
                            entid = parser.text
                            inEntid = false
                        }
                        if (inSiGunGu) { //isAddress이 true일 때 태그의 내용을 저장.
                            sigungu = parser.text
                            inSiGunGu = false
                        }
                        if (inTitle) { //isMapx이 true일 때 태그의 내용을 저장.
                            title = parser.text
                            inTitle = false
                        }
                        if (inDelegaTee) { //isMapy이 true일 때 태그의 내용을 저장.
                            delegatee = parser.text
                            inDelegaTee = false
                        }
                        if (inRoadAddress) { //isMapy이 true일 때 태그의 내용을 저장.
                            roadaddress = parser.text
                            inRoadAddress = false
                        }
                        if (inHomePage) { //isMapy이 true일 때 태그의 내용을 저장.
                            homepage = parser.text
                            inHomePage = false
                        }
                        if (inTel) { //isMapy이 true일 때 태그의 내용을 저장.
                            tel = parser.text
                            inTel = false
                        }
                        if (inFax) { //isMapy이 true일 때 태그의 내용을 저장.
                            fax = parser.text
                            inFax = false
                        }
                    }
                    XmlPullParser.END_TAG -> if ((parser.name == "item")) {
                        status1.text =
                            (status1.text.toString()
                                    //       + "고유번호 : " + entid + "\n "
                                    //       + "시군구명 : " + sigungu + "\n "
                                    + "기관명 : " + title + "\n "
                                    //        + "관리기관명 : " + delegatee + "\n "
                                    //       + "소재지 : " + roadaddress + "\n "
                                    //        + "홈페이지 : " + homepage + "\n "
                                    //         + "연락처 : " + tel + "\n "
                                    //        + "팩스번호 : " + fax
                                    + "\n")
                        initem = false
                    }


                }
                parserEvent = parser.next()

            /*    fun addDatabase() {
                    if (entid == null) {
                        Toast.makeText(this, "소방서명을 제대로 받아오지 못했습니다.", Toast.LENGTH_LONG).show()
                        //txtAddResult.text = "입력되지 않은 값이 있습니다."
                        return
                    }

                    val document = entid.toString()

                    firestore = FirebaseFirestore.getInstance()

                    firestore?.collection("FirestaionList")?.document(document)
                        ?.set(status1)?.addOnCompleteListener { task ->
                            //progressBar7.visibility = View.GONE
                            if (task.isSuccessful) {
                                Toast.makeText(this, "로그인 완료", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
            }

                rescue_login.setOnClickListener {
                    //startActivity(Intent(this, RescueMainActivity::class.java)

                    /* val FirestationDTO = FirestationDTO(
                         entid.toString(),
                         sigungu,
                         delegatee,
                         roadaddress,
                         homepage,
                         tel,
                         fax
                     ) */
                    //addDatabase()
                    startActivity(Intent(this, RescueMainActivity::class.java ))
                }   */
            }
        } catch (e: Exception) {
            status1.text = "에러가..났습니다..."

        }
    }
}

/* private var isRunning = true

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_get_fire_station)


       //val thread=ThreadClass()
       //thread.start()
   }

     inner class ThreadClass:Thread(){
        override fun run(){
            try{
                getAPI()
            } catch(e: Exception){
                e.printStackTrace()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        isRunning=false
    }
    private fun getAPI() {
        val urlBuilder =
            StringBuilder("http://openapi.safekorea.go.kr/openapi/service/firestation/item") /*URL*/
        urlBuilder.append(
            "?" + URLEncoder.encode("ServiceKey", "UTF-8")
                .toString() + "=" + "3dItxsHHzQJiyIitvKVQUr8%2ByHe5Qoov%2F5P2PbQ5IHpGhY5DBOO5J05edGISs%2BpNk1vMZzGX4K5XD1VvLBIIjw%3D%3D"
        ) /*Service Key*/
        urlBuilder.append(
                "&" + URLEncoder.encode("firestation_addr_cd", "UTF-8")
                    .toString() + "=" + URLEncoder.encode("3011000000", "UTF-8")
        ) /*법정동코드*/

        val url = URL(urlBuilder.toString())
        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.setRequestMethod("GET")
        conn.setRequestProperty("Content-type", "application/json")
        System.out.println("Response code: " + conn.getResponseCode())
        val rd: BufferedReader
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = BufferedReader(InputStreamReader(conn.getInputStream()))
        } else {
            rd = BufferedReader(InputStreamReader(conn.getErrorStream()))
        }
        val sb = StringBuilder()
        var line: String? = null
        while (rd.readLine().also({ line = it }) != null) {
            sb.append(line)
        }
        rd.close()
        conn.disconnect()
        println("성공"+sb.toString())
        println(url)
    }

    private fun getAPI() {
        val urlBuilder =
            StringBuilder("http://apis.data.go.kr/6480000/gyeongnamfirestation/gyeongnamfirestationList") /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=2k6I7GQsBtOms%2BiHgBGPMUfdEhprKWAXD0QURVgIP%2BPbNRZnTf5wzBnpTO6NeTFhSs%2FueWtsTZA59b3R68AjDg%3D%3D") /*Service Key*/
        urlBuilder.append(
            "&" + URLEncoder.encode(
                "ServiceKey",
                "UTF-8"
            ) + "=" + URLEncoder.encode("-", "UTF-8")
        ) /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append(
            "&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(
                "1",
                "UTF-8"
            )
        ) /*페이지번호*/
        urlBuilder.append(
            "&" + URLEncoder.encode(
                "numOfRows",
                "UTF-8"
            ) + "=" + URLEncoder.encode("10", "UTF-8")
        ) /*한 페이지 결과 수*/
        urlBuilder.append(
            "&" + URLEncoder.encode(
                "resultType",
                "UTF-8"
            ) + "=" + URLEncoder.encode("json", "UTF-8")
        ) /*JSON방식으로 호출 시 파라미터 resultType=json 입력*/
        val url = URL(urlBuilder.toString())
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-type", "application/json")
        println("Response code: " + conn.responseCode)
        val rd: BufferedReader
        rd = if (conn.responseCode >= 200 && conn.responseCode <= 300) {
            BufferedReader(InputStreamReader(conn.inputStream))
        } else {
            BufferedReader(InputStreamReader(conn.errorStream))
        }
        val sb = StringBuilder()
        var line: String?
        while (rd.readLine().also { line = it } != null) {
            sb.append(line)
        }
        rd.close()
        conn.disconnect()
        println(sb.toString())
    }  */

