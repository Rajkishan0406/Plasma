package com.example.plasma.Dashboard.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.R
import com.squareup.picasso.Picasso

class ChatAdapter(var chatModel: ArrayList<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var from = "" as String
    var type1 = 0 as Int
    var type2 = 1 as Int
    var imagetype1 = 2 as Int
    var imagetype2 = 3 as Int


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sender, parent, false)
        val V = LayoutInflater.from(parent.context).inflate(R.layout.receiver,parent,false)
        val imgV = LayoutInflater.from(parent.context).inflate(R.layout.image_chat_layout_left,parent,false)
        val imgv = LayoutInflater.from(parent.context).inflate(R.layout.image_chat_layout_right,parent,false)
        if(viewType == type1)
            return ChatAdapter.ViewHolder(v)
        if(viewType == imagetype1)
            return ChatAdapter.ViewHolder(imgV)
        if(viewType == imagetype2)
            return ChatAdapter.ViewHolder(imgv)
            return ChatAdapter.ViewHolder(V)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var PR = chatModel[position]

        from = PR.From.toString()

            holder.sender_time.text = PR.Time

        if(from.equals("s"))
            Picasso.get().load(PR.Message).into(holder.image)
        else if(from.equals("r"))
            Picasso.get().load(PR.Message).into(holder.image)
        else
            holder.sender_message.text = PR.Message
        

    }

    override fun getItemCount(): Int {
        return chatModel.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var sender_message = itemView.findViewById(R.id.text) as TextView
        var sender_time  = itemView.findViewById(R.id.time) as TextView
        var image = itemView.findViewById(R.id.image) as ImageView


    }

    override fun getItemViewType(position: Int): Int {
        if(chatModel.get(position).From.equals("S"))
            return type1
        if(chatModel.get(position).From.equals("s"))
            return imagetype2
        if(chatModel.get(position).From.equals("r"))
            return imagetype1
        else
            return type2
    }

}