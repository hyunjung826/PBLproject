package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_rescue_main.*


class RescueMainActivity : AppCompatActivity() {private var adapter: RescueAdapter? = null

    private var firestore : FirebaseFirestore? = null
    //private var firebaseAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rescue_main)

        val ab: ActionBar? = (supportActionBar)?.apply {
            hide()
        }

        /*butfire.setOnClickListener{
            startActivity(Intent(this, MainActivityRescue::class.java))
        }*/

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RescueAdapter(this){
            var myIntent = Intent(this, MyLocation::class.java)

            myIntent.putExtra("userID", it.uid)
            myIntent.putExtra("currentTime", it.time)
            myIntent.putExtra("latitude", it.latitude)
            myIntent.putExtra("longitude", it.longitude)
            myIntent.putExtra("address", it.address)

            startActivity(myIntent)

        }

        recyclerView.adapter = adapter

        viewDatabase()
    }

    private fun viewDatabase() {

        firestore = FirebaseFirestore.getInstance()


        firestore?.collection("locationList")?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var locationList = ArrayList<locationDTO>()
                    for (dc in task.result!!.documents) {
                        var mateDTO = dc.toObject(locationDTO::class.java)
                        locationList.add(mateDTO!!)
                    }
                    adapter?.setItems(locationList)
                    adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }

    }

}