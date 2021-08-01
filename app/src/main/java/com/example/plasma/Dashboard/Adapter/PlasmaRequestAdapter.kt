package com.example.plasma.Dashboard.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PlasmaRequestAdapter (var plasmarequest : ArrayList<PlasmaRequestModel>) : RecyclerView.Adapter<PlasmaRequestAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.plasmarequestlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var PR : PlasmaRequestModel = plasmarequest[position]



    }

    override fun getItemCount(): Int {
        return plasmarequest.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById(R.id.User_Name) as TextView
        var city  = itemView.findViewById(R.id.User_City) as TextView
        var state = itemView.findViewById(R.id.User_State) as TextView
        var blood = itemView.findViewById(R.id.User_Blood) as TextView

    }

}