package com.example.plasma.Dashboard.Adapter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Chat.ChatActivity
import com.example.plasma.Dashboard.Chat.ChatPageFragment
import com.example.plasma.Dashboard.Home.RequestProfileFragment
import com.example.plasma.Dashboard.Model.ChatFragmentModel
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.MapActivity
import com.example.plasma.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ContextUtils.getActivity

class ChatFragmentAdapter(var chatfragModel: ArrayList<ChatFragmentModel>, param: (Any) -> Unit) : RecyclerView.Adapter<ChatFragmentAdapter.ViewHolder>()   {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout, parent, false)
        return ChatFragmentAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var PR = chatfragModel[position]

        holder.name.text = PR.Name
        holder.blood.text = PR.Blood
        holder.msg.text = PR.Last_Message

        if(PR.online.toString().equals("1")){
            holder.Online_Card.setCardBackgroundColor(Color.GREEN)
        }

        var Time = PR.Time as String
        var checker = Time.substring(0,2).toInt()
        if(checker > 12){
            Time = (checker - 12).toString() + Time.substring(2,Time.length) + " pm"
        }
        else if(checker == 12){
            Time = Time + " pm"
        }
        else if(checker == 0){
            Time = "12" + Time.substring(2,Time.length) + " am"
        }
        else{
            Time = Time + " am"
        }

        holder.time.text = Time

        var name = PR.Name.toString()
        name = name.substring(0,1)
        holder.character.text = name

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("Id",PR.Id)
                intent.putExtra("Name",PR.Name)
                activity?.startActivity(intent)
            }
        })

        holder.card.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("Id",PR.Id)
                intent.putExtra("Name",PR.Name)
                activity?.startActivity(intent)
            }
        })

        holder.Name_Card.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var activity = v!!.context as AppCompatActivity
                val IDF = RequestProfileFragment()
                var bun : Bundle
                bun = Bundle()
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
        var character  = itemView.findViewById(R.id.charr) as TextView
        var card  = itemView.findViewById(R.id.click_to_move) as CardView
        var Name_Card  = itemView.findViewById(R.id.name_card) as CardView
        var Online_Card  = itemView.findViewById(R.id.online_card) as CardView

    }

}