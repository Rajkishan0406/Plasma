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
import com.example.plasma.Dashboard.Model.ChatFragmentModel
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.R

class ChatFragmentAdapter(var chatfragModel: ArrayList<ChatFragmentModel>) : RecyclerView.Adapter<ChatFragmentAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return ChatFragmentAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var PR = chatfragModel[position]

        holder.name.text = PR.Name
        holder.blood.text = PR.Blood
        holder.msg.text = PR.Last_Message
        holder.time.text = PR.Time

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                val IDF = ChatPageFragment()
                var bun : Bundle
                bun = Bundle()
                bun.putString("Name",PR.Name)
                bun.putString("Id",PR.Id)
                IDF.arguments = bun
                activity.supportFragmentManager.beginTransaction().replace(R.id.main_dashboard_frame,IDF).addToBackStack(null).commit()
            }
        })

        holder.card.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                val IDF = ChatPageFragment()
                var bun : Bundle
                bun = Bundle()
                bun.putString("Name",PR.Name)
                bun.putString("Id",PR.Id)
                IDF.arguments = bun
                activity.supportFragmentManager.beginTransaction().replace(R.id.main_dashboard_frame,IDF).addToBackStack(null).commit()
            }
        })

    }

    override fun getItemCount(): Int {
        return chatfragModel.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name = itemView.findViewById(R.id.name) as TextView
        var time  = itemView.findViewById(R.id.time) as TextView
        var blood  = itemView.findViewById(R.id.blood) as TextView
        var msg  = itemView.findViewById(R.id.last_msg) as TextView
        var card  = itemView.findViewById(R.id.click_to_move) as CardView

    }

}