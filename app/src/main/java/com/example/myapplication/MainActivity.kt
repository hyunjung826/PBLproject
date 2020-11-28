package com.example.myapplication

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.kakaonavi.KakaoNaviParams
import com.kakao.kakaonavi.KakaoNaviService
import com.kakao.kakaonavi.Location
import com.kakao.kakaonavi.NaviOptions
import com.kakao.kakaonavi.options.CoordType
import com.kakao.kakaonavi.options.RpOption
import com.kakao.kakaonavi.options.VehicleType
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL
import java.security.MessageDigest

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var firestore: FirebaseFirestore? = null
    val user = FirebaseAuth.getInstance().currentUser


    private var auth : FirebaseAuth? = null

    var button: Button? = null

    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<View>(R.id.button) as Button
        button!!.setOnClickListener(this)

        val db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        StrictMode.enableDefaults()
        var status1: String? = null //파싱된 결과확인!
        //var inFirestationAddrCode = false
        //var inResultCode = false
        var inFacilityName = false
        var inLatitude = false
        var inLongitude = false
        var inAddrNm = false
        var inTel = false
        var inFax = false

        //var firestation_addr_code: String? = null
        //var resultCode: String? = null
        var facilityName: String? = null
        var latitude: String? = null
        var longitude: String? = null
        var addrNm: String? = null
        var tel: String? = null
        var fax: String? = null

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
                        if (inFacilityName) { //isAddress이 true일 때 태그의 내용을 저장.
                            facilityName = parser.text
                            inFacilityName = false
                        }
                        if (inLatitude) { //isMapy이 true일 때 태그의 내용을 저장.
                            latitude = parser.text
                            inLatitude = false
                        }
                        if (inLongitude) { //isMapy이 true일 때 태그의 내용을 저장.
                            longitude = parser.text
                            inLongitude = false
                        }
                        if (inAddrNm) { //isMapy이 true일 때 태그의 내용을 저장.
                            addrNm = parser.text
                            inAddrNm = false
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

            fun addDatabase() {
                if (facilityName == null) {
                    Toast.makeText(this, "소방서명을 제대로 받아오지 못했습니다.", Toast.LENGTH_LONG).show()
                    //txtAddResult.text = "입력되지 않은 값이 있습니다."
                    return
                }

                val FirestationDTO = FirestationDTO(
                    //resultCode,
                    facilityName,
                    latitude,
                    longitude,
                    addrNm,
                    tel,
                    fax
                )

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

            btn_login.setOnClickListener {
                emailLogin()
                addDatabase()
//            val intent = Intent(this, LoginResultActivity::class.java)
//            startActivity(intent)
            }

        } catch (e: Exception) {
            if (status1 != null) {
                //status1.text = "에러가..났습니다..."
            }
        }




        txt_register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
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
            startActivity(Intent(this, UserLoginActivity::class.java))
            finish()
        }

    }

    //카카오내비 테스트
    override fun onClick(v: View?) {
        if (v!!.id == R.id.button) {
            val destination: Location =
                Location.newBuilder("카카오 판교 오피스", 127.10821222694533, 37.40205604363057).build()

            val options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84)
                .setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build()

            val builder = KakaoNaviParams.newBuilder(destination).setNaviOptions(options)

            val params = builder.build()
            KakaoNaviService.getInstance().navigate(this, builder.build())
        }
    }



    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray

    ) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

                //위치 값을 가져올 수 있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

//        if (locationSource!!.onRequestPermissionsResult(
//                permsRequestCode, permissions, grantResults
//            )
//        ) {
//            if (!locationSource.isActivated()) { // 권한 거부됨
//                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
//           }
//            return
//        }
        super.onRequestPermissionsResult(
            permsRequestCode, permissions, grantResults
        )
    }

    fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            """
    앱을 사용하기 위해서는 위치 서비스가 필요합니다.
    위치 설정을 수정하실래요?
    """.trimIndent()
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("취소") { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    companion object {
        private const val GPS_ENABLE_REQUEST_CODE = 2001
        const val PERMISSIONS_REQUEST_CODE = 100
    }



}