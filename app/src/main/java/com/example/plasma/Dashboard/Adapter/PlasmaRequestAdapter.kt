package com.example.plasma.Dashboard.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Chat.ChatActivity
import com.example.plasma.Dashboard.Chat.ChatPageFragment
import com.example.plasma.Dashboard.Home.RequestProfileFragment
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.ProfileActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PlasmaRequestAdapter (var plasmarequest : ArrayList<PlasmaRequestModel>) : RecyclerView.Adapter<PlasmaRequestAdapter.ViewHolder>() {

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

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
                var intent = Intent(activity, ProfileActivity::class.java)
                intent.putExtra("Id",id)
                activity.startActivity(intent)
            }
        })

        mAuth = FirebaseAuth.getInstance()
        var User_id = mAuth.currentUser?.uid as String
        data = FirebaseDatabase.getInstance().getReference("Details")

        var yes = 0 as Int

        data.child(User_id).child("Profile").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                    yes = 1
                else
                    yes = -1
            }
            override fun onCancelled(error: DatabaseError) { }
        })

        holder.msg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                if(!id.equals(User_id) && yes == 1) {
                    val intent = Intent(activity, ChatActivity::class.java)
                    intent.putExtra("Id",PR.Id)
                    intent.putExtra("Name",PR.Name)
                    activity?.startActivity(intent)
                }
                else if(yes == 0){
                    Toast.makeText(activity,"Please add your profile for chat",Toast.LENGTH_SHORT).show()
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