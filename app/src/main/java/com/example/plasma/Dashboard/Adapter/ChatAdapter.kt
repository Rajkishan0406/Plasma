package com.example.plasma.Dashboard.Adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Chat.ChatActivity
import com.example.plasma.Dashboard.Chat.DeleteMsgFragment
import com.example.plasma.Dashboard.Chat.FullImageFragment
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.DashboardActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ChatAdapter(var chatModel: ArrayList<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    var from = "" as String
    var type1 = 0 as Int
    var type2 = 1 as Int
    var imagetype1 = 2 as Int
    var imagetype2 = 3 as Int
    var replytype1 = 4 as Int
    var replytype2 = 5 as Int
    lateinit var data : DatabaseReference
    lateinit var mAuth  :FirebaseAuth


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sender, parent, false)
        val V = LayoutInflater.from(parent.context).inflate(R.layout.receiver, parent, false)
        val Rv = LayoutInflater.from(parent.context).inflate(R.layout.receiver_reply, parent, false)
        val Sv = LayoutInflater.from(parent.context).inflate(R.layout.sender_reply, parent, false)
        val imgV = LayoutInflater.from(parent.context).inflate(R.layout.image_chat_layout_left, parent, false)
        val imgv = LayoutInflater.from(parent.context).inflate(R.layout.image_chat_layout_right, parent, false)

        if(viewType == type1)
            return ChatAdapter.ViewHolder(v)
        if(viewType == imagetype1)
            return ChatAdapter.ViewHolder(imgV)
        if(viewType == imagetype2)
            return ChatAdapter.ViewHolder(imgv)
        if(viewType == type2)
            return ChatAdapter.ViewHolder(V)
        if(viewType == replytype1)
            return  ChatAdapter.ViewHolder(Rv)
        return ChatAdapter.ViewHolder(Sv)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var PR = chatModel[position]

        from = PR.From.toString()


        //Time conversion
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

        holder.sender_time.text = Time

        if(from.equals("S") || from.equals("s") || from.equals("Sv")){
            var t = PR.Last_msg_id
            if (t != null) {
                if(t >= PR.day.toString()) {
                    holder.tick1.visibility = View.VISIBLE
                    holder.tick2.visibility = View.VISIBLE
                }
            }

        }



        if(from.equals("s"))
            Picasso.get().load(PR.Message).into(holder.image)
        else if(from.equals("r"))
            Picasso.get().load(PR.Message).into(holder.image)
        else if(from.equals("S") || from.equals("R"))
            holder.sender_message.text = PR.Message
        else if(from.equals("Sv") || from.equals("Rv")){
            var upper = PR.old_msg as String
            holder.sender_message.text = PR.Message
            mAuth = FirebaseAuth.getInstance()
            var user = mAuth.currentUser?.uid
            data = FirebaseDatabase.getInstance().getReference("Details")
            var l = PR.user_Id as String
            Log.i("user id : "+l,"    "+upper)
            if (user != null) {
                data.child(user).child("Chatting").child(l).child("Message").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists() && snapshot.hasChild(upper)){
                            var uppe = snapshot.child(upper).getValue() as String
                            Log.i("Here it is "," "+uppe)
                            if(!uppe.substring(0,2).equals("Sv") && !uppe.substring(0,2).equals("Rv"))
                                uppe = uppe.substring(20,uppe.length)
                            else
                                uppe = uppe.substring(40,uppe.length)
                            holder.replied_msg.text = uppe
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        }

        //Full Imgae show..
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.i("Clicked", ": ho rha hai " + PR.From)
                if (PR.From.toString().equals("s")) {
                    var activity = v!!.context as AppCompatActivity
                    val IDF = FullImageFragment()
                    Log.i("Clicked", ":  " + PR.From)
                    var bun: Bundle
                    bun = Bundle()
                    bun.putString("image", PR.Message)
                    IDF.arguments = bun
                    activity.supportFragmentManager.beginTransaction().replace(
                        R.id.chat_frame,
                        IDF
                    ).addToBackStack(null).commit()
                }
                if (PR.From.toString().equals("r")) {
                    var activity = v!!.context as AppCompatActivity
                    val IDF = FullImageFragment()
                    Log.i("Clicked", ":  " + PR.From)
                    var bun: Bundle
                    bun = Bundle()
                    bun.putString("image", PR.Message)
                    IDF.arguments = bun
                    activity.supportFragmentManager.beginTransaction().replace(
                        R.id.chat_frame,
                        IDF
                    ).addToBackStack(null).commit()
                }
            }
        })

        var id = PR.id.toString()

        holder.reply.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                mAuth = FirebaseAuth.getInstance()
                var user = mAuth.currentUser?.uid
                data = FirebaseDatabase.getInstance().getReference("Details")
                if (user != null) {
                    data.child(user).child("Reply_Id").setValue(PR.id)
                }
                Log.i("Reply Click : ",""+PR.Time)
            }

        })


        holder.sender_message.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                var activity = v!!.context as AppCompatActivity
                val IDF = DeleteMsgFragment()
                var bun: Bundle
                bun = Bundle()
                bun.putString("Msgid", id)
                IDF.arguments = bun
                IDF.show(
                    (activity as ChatActivity).getSupportFragmentManager(),
                    IDF.getTag()
                )
                Log.i("id : ", "" + id)

                return true
            }

        })


    }

    override fun getItemCount(): Int {
        return chatModel.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var sender_message = itemView.findViewById(R.id.text) as TextView
        var sender_time  = itemView.findViewById(R.id.time) as TextView
        var image = itemView.findViewById(R.id.image) as ImageView
        var tick1 = itemView.findViewById(R.id.tick) as TextView
        var reply = itemView.findViewById(R.id.reply_icon) as ImageView
        var tick2 = itemView.findViewById(R.id.tick2) as TextView
        var replied_msg = itemView.findViewById(R.id.replied_message) as TextView

    }

    override fun getItemViewType(position: Int): Int {
        if(chatModel.get(position).From.equals("S"))
            return type1
        if(chatModel.get(position).From.equals("s"))
            return imagetype2
        if(chatModel.get(position).From.equals("r"))
            return imagetype1
        if(chatModel.get(position).From.equals("Sv"))
            return replytype2
        if(chatModel.get(position).From.equals("Rv"))
            return replytype1
        else
            return type2
    }

}