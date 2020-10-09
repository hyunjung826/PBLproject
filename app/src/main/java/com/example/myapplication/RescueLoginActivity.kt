package com.example.myapplication


import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.kakao.kakaonavi.KakaoNaviParams
import com.kakao.kakaonavi.KakaoNaviService
import com.kakao.kakaonavi.Location
import com.kakao.kakaonavi.NaviOptions
import com.kakao.kakaonavi.options.CoordType
import com.kakao.kakaonavi.options.RpOption
import com.kakao.kakaonavi.options.VehicleType


class RescueLoginActivity : AppCompatActivity() {

    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescue_login)
    }

    /*override fun onClick(v: View?) {
        if (v!!.id == R.id.button) {
            val destination: Location =
                Location.newBuilder("카카오 판교 오피스", 127.10821222694533, 37.40205604363057).build()

            val options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84)
                .setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build()

            val builder = KakaoNaviParams.newBuilder(destination).setNaviOptions(options)

            val params = builder.build()
            KakaoNaviService.getInstance().navigate(this, builder.build())
        }
    }*/
}