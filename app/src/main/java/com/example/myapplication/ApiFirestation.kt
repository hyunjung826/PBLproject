package com.example.myapplication

import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

class ApiFirestation(url : URL) {
    var url : URL
    init{
        this.url = url
    }

    var adapter : FirestationAdapter = FirestationAdapter()

    fun main(): FirestationAdapter{
        val responseBody = get(url)
        parseData(responseBody)
        return adapter
    }
    private operator fun get(apiUrl: URL): String {
        var responseBody: String = ""
        try {
            val `in` = apiUrl.openStream()
            responseBody = readBody(`in`)

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println(responseBody)
        return responseBody
    }

    private fun readBody(body: InputStream): String {
        val streamReader = InputStreamReader(body)

        try {
            BufferedReader(streamReader).use({ lineReader ->
                val responseBody = StringBuilder()

                var line: String? = lineReader.readLine()
                while (line != null) {
                    responseBody.append(line)
                    line = lineReader.readLine()
                }
                return responseBody.toString()
            })
        } catch (e: IOException) {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }
    private fun parseData(responseBody: String) {
        var title: String
        var roadaddress : String
        var jsonObject = JSONObject()
        try {
            jsonObject = JSONObject(responseBody)
            val root = jsonObject.getJSONObject("gyeongnamfirestationList")
            val jsonArray = root.getJSONArray("item")

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                title = item.getString("title")
                roadaddress = item.getString("roadaddress")
                println("title : $title")
                println("roadaddress : $roadaddress")

                adapter.addItem(title, roadaddress)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}