package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class FirestationAdapter : BaseAdapter(){

    var list = ArrayList<FirestationDTO>()

    override fun getCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int) : Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    fun getSize(): Int {
        return list.size
    }

    fun addItem(title: String, roadaddress: String) {
        var item = FirestationDTO(title, roadaddress)
        list.add(item)
        println(list)
        println("in ListViewAdapter --> listSize : " + list.size)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var context = parent?.getContext()
        var convertV = convertView

        if (convertView == null) {
            val systemService =
                context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertV = systemService.inflate(R.layout.firestation_listview, parent, false)
        }

        var title = convertV?.findViewById(R.id.title) as TextView
        var roadaddress = convertV?.findViewById(R.id.roadaddress) as TextView

        val listViewItem = list[position]

        title.text = listViewItem.getTitle()
        roadaddress.text = listViewItem.getRoadaddress()

        return convertV
    }







}