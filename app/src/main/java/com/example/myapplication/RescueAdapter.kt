package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.sos_list_item.view.*
import java.util.ArrayList

class RescueAdapter(val context: Context, val itemCheck: (locationDTO) -> Unit)
    : RecyclerView.Adapter<RescueAdapter.ViewHolder>() {

    private var items = ArrayList<locationDTO>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.sos_list_item, viewGroup, false)
        return ViewHolder(itemView, itemCheck)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: locationDTO = items[position]
        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setItems(items: ArrayList<locationDTO>) {
        this.items = items
    }

    inner class ViewHolder(itemView: View, itemCheck: (locationDTO) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun setItem(item: locationDTO) {

            itemView.uid.text = item.uid
            itemView.latitude.text = item.latitude.toString()
            itemView.longitude.text = item.longitude.toString()
            itemView.address.text = item.address
            itemView.time.text = item.time
            itemView.setOnClickListener() { itemCheck(item) }
        }

    }
}