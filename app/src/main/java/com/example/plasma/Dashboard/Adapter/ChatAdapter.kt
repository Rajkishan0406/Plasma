package com.example.plasma.Dashboard.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.R

class ChatAdapter(var chatModel: ArrayList<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var from = "" as String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sender,parent,false)
        val V = LayoutInflater.from(parent.context).inflate(R.layout.receiver,parent,false)
        if(from.equals("S"))
            return ChatAdapter.ViewHolder(v)
        return ChatAdapter.ViewHolder(V)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var PR = chatModel[position]

        holder.message.text = PR.Message
        holder.time.text = PR.Time

        from = PR.From.toString()
    }

    override fun getItemCount(): Int {
        return chatModel.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var message = itemView.findViewById(R.id.text) as TextView
        var time  = itemView.findViewById(R.id.time) as TextView


    }

}