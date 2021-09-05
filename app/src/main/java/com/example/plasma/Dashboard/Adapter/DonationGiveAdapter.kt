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
import com.example.plasma.Dashboard.Setting.Donation_G_DetailsFragment
import com.example.plasma.R

class DonationGiveAdapter (var plasmarequest : ArrayList<PlasmaRequestModel>) : RecyclerView.Adapter<DonationGiveAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.donationgivelayout,parent,false)
        return DonationGiveAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var PR = plasmarequest[position]

        holder.name.text = PR.Name
        holder.blood.text = PR.Blood
        holder.city.text = PR.City
        holder.state.text = PR.State

        var id = PR.Id

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                val IDF = Donation_G_DetailsFragment()
                var bun : Bundle
                bun = Bundle()
                bun.putString("Id",id)
                IDF.arguments = bun
                activity.supportFragmentManager.beginTransaction().replace(R.id.main_dashboard_frame,IDF).commit()
            }
        })

        holder.msg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                    var activity = v!!.context as AppCompatActivity
                    val IDF = ChatPageFragment()
                    var bun: Bundle
                    bun = Bundle()
                    bun.putString("Name", PR.Name.toString())
                    bun.putString("Id", id)
                    IDF.arguments = bun
                    activity.supportFragmentManager.beginTransaction().replace(R.id.main_dashboard_frame, IDF).commit()
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
        var card = itemView.findViewById(R.id.card) as CardView
        var msg = itemView.findViewById(R.id.Message_card) as CardView


    }

}