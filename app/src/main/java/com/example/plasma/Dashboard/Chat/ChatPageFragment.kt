package com.example.plasma.Dashboard.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Adapter.ChatAdapter
import com.example.plasma.Dashboard.Adapter.PlasmaRequestAdapter
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ChatPageFragment : Fragment() {

    var bun = Bundle()
    lateinit var name : TextView
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var Msg : EditText
    lateinit var send : CardView

    lateinit var chatArrayList : ArrayList<ChatModel>
    lateinit var recyclerview : RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_chat_page, container, false)


        //Bundle code...
        bun = this.requireArguments()
        var s = bun.getString("Name") as String
        var id = bun.getString("Id") as String

        //Find View BY ID...
        name = view.findViewById(R.id.User_Name)
        Msg = view.findViewById(R.id.message)
        send = view.findViewById(R.id.send_card)

        //FireBase Intitializer...
        name.setText(s)
        mAuth = FirebaseAuth.getInstance()
        var User_Id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")


        //RecyclerView..
        recyclerview = view.findViewById(R.id.message_recyclerview)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        chatArrayList = arrayListOf<ChatModel>()


        if (User_Id != null) {
            data.child(User_Id).child("Chatting").child(id).child("Message").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        chatArrayList.clear()
                        var time = ""
                        var msg = ""
                        var from = ""
                        var day = ""
                        if(snapshot.exists()){
                            for(snap in snapshot.children){
                                var m = snap.getValue() as String
                                from = m.substring(0,1)
                                time = m.substring(1,9)
                                day = m.substring(10,15)
                                msg = m.substring(20,m.length)
                                chatArrayList.add(ChatModel(msg,time,from,day))
                            }
                        }
                    val adapter = ChatAdapter(chatArrayList)
                    recyclerview.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }


        //sending msg
        send.setOnClickListener(View.OnClickListener {
            var text = Msg.text.toString()
            if(text.length > 100){
                Toast.makeText(activity,"Message length must be less then 120 characters",Toast.LENGTH_SHORT).show()
            }
            else if(text.length > 0){
                val sdf = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss")
                val Sdf = SimpleDateFormat("hh:mm a dd/MM/yyyy")
                val d = sdf.format(Date())
                val D = Sdf.format(Date())
                var sender = "S" + D + text
                if(User_Id != null && id != null) {
                    data.child(User_Id).child("Chatting").child(id).child("Message").child(d).setValue(sender)
                    data.child(User_Id).child("Chatting").child(id).child("Last_Message").setValue(sender)
                }
                var  receiver = "R" + D + text
                if(User_Id != null && id != null) {
                    data.child(id).child("Chatting").child(User_Id).child("Message").child(d).setValue(receiver)
                    data.child(id).child("Chatting").child(User_Id).child("Last_Message").setValue(receiver)
                }
                Msg.setText("")
            }
        })

        return view
    }

}