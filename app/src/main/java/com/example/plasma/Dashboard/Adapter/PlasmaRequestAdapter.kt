package com.example.plasma.Dashboard.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Chat.ChatPageFragment
import com.example.plasma.Dashboard.Home.RequestProfileFragment
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PlasmaRequestAdapter (var plasmarequest : ArrayList<PlasmaRequestModel>) : RecyclerView.Adapter<PlasmaRequestAdapter.ViewHolder>() {

    lateinit var mAuth : FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.plasmarequestlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var PR = plasmarequest[position]

        holder.name.text = PR.Name
        holder.blood.text = PR.Blood
        holder.city.text = PR.City
        holder.state.text = PR.State

        var id = PR.Id
        var name = PR.Name

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                val IDF = RequestProfileFragment()
                var bun : Bundle
                bun = Bundle()
                bun.putString("Id",id)
                IDF.arguments = bun
                activity.supportFragmentManager.beginTransaction().replace(R.id.main_dashboard_frame,IDF).addToBackStack(null).commit()
            }
        })

        mAuth = FirebaseAuth.getInstance()
        var User_id = mAuth.currentUser?.uid as String

        holder.msg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(!id.equals(User_id)) {
                    var activity = v!!.context as AppCompatActivity
                    val IDF = ChatPageFragment()
                    var bun: Bundle
                    bun = Bundle()
                    bun.putString("Name", name)
                    bun.putString("Id", id)
                    IDF.arguments = bun
                    activity.supportFragmentManager.beginTransaction().replace(R.id.main_dashboard_frame, IDF).addToBackStack(null).commit()
                }
            }
        })

    }

    override fun getItemCount(): Int {
        return plasmarequest.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById(R.id.User_Name) as TextView
        var city  = itemView.findViewById(R.id.User_City) as TextView
        var state = itemView.findViewById(R.id.User_State) as TextView
        var blood = itemView.findViewById(R.id.User_Blood) as TextView
        var msg = itemView.findViewById(R.id.Message_card) as CardView

    }

}