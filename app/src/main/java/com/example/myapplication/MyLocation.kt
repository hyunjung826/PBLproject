package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_my_location.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.*


class MyLocation : AppCompatActivity(), OnMapReadyCallback  {

    private val clientId:String = "kcn1kedf9a";//애플리케이션 클라이언트 아이디값";
    private val clientSecret:String = "wwcby6dtxjXmNu4FVVBqDle2ZZn2xWtkGRMQi5sv";//애플리케이션 클라이언트 시크릿값";
    private var isRunning=true
    private var x:Double=0.0
    private var y:Double=0.0

    private var gpsTracker: GpsTracker? = null
    val LOCATION_PERMISSION_REQUEST_CODE: Int = 1000
    var locationSource: FusedLocationSource? = null
    var naverMap: NaverMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_location)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        val textview_address = findViewById<View>(R.id.my_location) as TextView
        val ShowLocationButton = findViewById<View>(R.id.get_location) as FloatingActionButton
        val SOSButton = findViewById<View>(R.id.sos_final) as Button

        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference()

        gpsTracker = GpsTracker(this)
        val latitude: Double = gpsTracker!!.getLatitude()
        val longitude: Double = gpsTracker!!.getLongitude()
        val address = getCurrentAddress(latitude, longitude)

        ShowLocationButton.setOnClickListener {
            textview_address.text = address
            //Toast.makeText(this, "현재위치 \n위도 $latitude\n경도 $longitude", Toast.LENGTH_LONG).show()
        }

        SOSButton.setOnClickListener{
            val result = HashMap<Any, Any>()
            result["latitude"] = latitude
            result["longitude"] = longitude
            result["address"] = address

            myRef.child("LocationList").push().setValue(result)

            showSettingPopUp()
        }

        src_btn.setOnClickListener {
            fetchJson(src_edit.text.toString())
        }

        val fm: FragmentManager = getSupportFragmentManager()
        var mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance()
            fm.beginTransaction().add(R.id.map, mapFragment).commit()
        }
        mapFragment!!.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

    }

    private fun showSettingPopUp(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_help, null)
        val txt_pop4: TextView = view.findViewById(R.id.txt_pop4)
        txt_pop4.text = "재난 발생 긴급 신고를 접수하시겠습니까?"

        val alertDialog = AlertDialog.Builder(this).create()

        val butSave1 = view.findViewById<Button>(R.id.butSave1)

        butSave1.setOnClickListener {
            alertDialog.dismiss()
            /* val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
             val view = inflater.inflate(R.layout.alert_help1, null)
             val txt_pop5: TextView = view.findViewById(R.id.txt_pop5)
             txt_pop5.text = "재난 발생 긴급 신고가 접수되었습니다."

             val alertDialog = AlertDialog.Builder(this).create()

             val intent = Intent(this, LoginResultActivity::class.java)
             startActivity(intent)*/
        }

        val butCancel = view.findViewById<Button>(R.id.butCancel)
        butCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }


    fun getCurrentAddress(latitude: Double, longitude: Double): String {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>
        addresses = try {
            geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        }
        val address = addresses[0]
        return """
            ${address.getAddressLine(0)}
    
            """.trimIndent()
    }


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        // ...
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

    }

    fun fetchJson(vararg p0:String){
        //OkHttp로 요청하기
        val text= URLEncoder.encode("${p0[0]}", "utf-8")
        println(text)
        val url: String =
            "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=${text}&display="
        val formBody = FormBody.Builder()
            .add("query", "${text}")
            .add("display", "1")
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("X-NCP-APIGW-API-KEY-ID", clientId)
            .addHeader("X-NCP-APIGW-API-KEY", clientSecret)
            .method("GET",null)
            .build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println("Success to execute request : $body")

                val jsonObject = JSONObject(body)
                val jsonArray = jsonObject.getJSONArray("addresses")

                for (i in 0..jsonArray.length() - 1) {
                    val iObject = jsonArray.getJSONObject(i)
                    val roadAddress = iObject.getString("roadAddress")
                    val jibunAddress = iObject.getString("jibunAddress")
                    x = iObject.getDouble("x")
                    y = iObject.getDouble("y")

                    println("roadAddress : $roadAddress")
                    println("jibunAddress : $jibunAddress")
                    println("x : $x")
                    println("y : $y")
                    val thread=ThreadClass()
                    thread.start()
                }
            }
            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
            }
        })
    }
    inner class ThreadClass:Thread(){
        override fun run(){
            while(isRunning){
                SystemClock.sleep(100)
                runOnUiThread{
                    val marker = Marker()
                    marker.position= LatLng(y, x)
                    marker.map=naverMap
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        isRunning=false
    }
}