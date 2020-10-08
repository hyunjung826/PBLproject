package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder

public class GetFireStationAPI : AppCompatActivity() {
    private var isRunning=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_fire_station_a_p_i)
        val thread=ThreadClass()
        thread.start()
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
}
private fun getAPI() {
    val urlBuilder =
        StringBuilder("http://openapi.safekorea.go.kr/openapi/service/firestation/item") /*URL*/
    urlBuilder.append(
        "?" + URLEncoder.encode("ServiceKey", "UTF-8")
            .toString() + URLEncoder.encode("=3dItxsHHzQJiyIitvKVQUr8%2ByHe5Qoov%2F5P2PbQ5IHpGhY5DBOO5J05edGISs%2BpNk1vMZzGX4K5XD1VvLBIIjw%3D%3D", "UTF-8")
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
}




