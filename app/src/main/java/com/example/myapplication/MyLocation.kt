package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_my_location.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


class MyLocation : AppCompatActivity(), OnMapReadyCallback  {

    private var isRunning=true
    private var x:Double=0.0
    private var y:Double=0.0
    private var addressLine:String?=null
    val textview_address = findViewById<View>(R.id.my_location) as TextView


    private var gpsTracker: GpsTracker? = null
    val LOCATION_PERMISSION_REQUEST_CODE: Int = 1000
    var locationSource: FusedLocationSource? = null
    var naverMap: NaverMap? = null

    private var firestore : FirebaseFirestore? = null

    val user = FirebaseAuth.getInstance().currentUser
    val user_id = user?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_location)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        val textview_address = findViewById<View>(R.id.my_location) as TextView
        val ShowLocationButton = findViewById<View>(R.id.get_location) as FloatingActionButton
        val SOSButton = findViewById<View>(R.id.sos_final) as Button

        gpsTracker = GpsTracker(this)
        val latitude: Double = gpsTracker!!.getLatitude()
        val longitude: Double = gpsTracker!!.getLongitude()
        val address = getCurrentAddress(latitude, longitude)
        val currentTime = System.currentTimeMillis()
        val textTime = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA).format(currentTime)


        ShowLocationButton.setOnClickListener {
            textview_address.text = address
            //Toast.makeText(this, "현재위치 \n위도 $latitude\n경도 $longitude", Toast.LENGTH_LONG).show()
        }

        SOSButton.setOnClickListener{
            fun addDatabase() {
                if (latitude == null || longitude == null) {
                    Toast.makeText(this, "위치 정보를 제대로 받아오지 못했습니다.", Toast.LENGTH_LONG).show()
                    //txtAddResult.text = "입력되지 않은 값이 있습니다."
                    return
                }
                val locationDTO = locationDTO(
                    user_id.toString(),
                    textTime,
                    latitude,
                    longitude,
                    address
                )
                val document = user_id.toString()

                firestore = FirebaseFirestore.getInstance()

                firestore?.collection("locationList")?.document(document)
                    ?.set(locationDTO)?.addOnCompleteListener { task ->
                        //progressBar7.visibility = View.GONE
                        if (task.isSuccessful) {
                            Toast.makeText(this, "신고 접수 완료", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }

            }

            fun showSettingPopUp(){

                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.alert_help, null)
                val txt_pop4: TextView = view.findViewById(R.id.txt_pop4)
                txt_pop4.text = "재난 발생 긴급 신고를 접수하시겠습니까?"

                val alertDialog = AlertDialog.Builder(this).create()

                val butSave1 = view.findViewById<Button>(R.id.butSave1)

                butSave1.setOnClickListener {
                    addDatabase()
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

            showSettingPopUp()
        }

        src_btn.setOnClickListener {
            // 주소입력후 버튼 클릭시 해당 위도경도값의 지도화면으로 이동
            var list:List<Address>?=null
            val str:String = src_edit.text.toString()
            val geocoder = Geocoder(this, Locale.getDefault())

            try {
                    list = geocoder?.getFromLocationName(str, // 지역 이름
                        10) // 읽을 개수
                } catch (e: IOException) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }
            if(list!=null){
                if(list.size==0){
                    Toast.makeText(this, "해당되는 주소 정보가 없습니다", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "주소가 검색되었습니다.", Toast.LENGTH_LONG).show()
                    val addr:Address = list.get(0)
                    x=addr.latitude
                    y=addr.longitude
                    addressLine = addr.getAddressLine(0)
                    println("위도 x : $x")
                    println("y : $y")
                    println("address: $addressLine")
                    val thread=ThreadClass()
                    thread.start()
                }
            }
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        src_edit.visibility = View.VISIBLE

        return super.onCreateOptionsMenu(menu)
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
    inner class ThreadClass:Thread(){
        override fun run(){
            while(isRunning){
                SystemClock.sleep(100)
                runOnUiThread{
                    val marker = Marker()
                    val location = LatLng(x,y)
                    marker.position= location
                    marker.map=naverMap
                    // 카메라 위치와 줌 조절(숫자가 클수록 확대)
                    val cameraPosition = CameraPosition(location, 17.0);
                    naverMap?.setCameraPosition(cameraPosition);
                    textview_address.text = addressLine
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        isRunning=false
    }
}