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
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ChatPageFragment : Fragment() {

    var bun = Bundle()
    lateinit var name : TextView
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var Msg : EditText
    lateinit var send : CardView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_chat_page, container, false)

        name = view.findViewById(R.id.User_Name)
        mAuth = FirebaseAuth.getInstance()
        var User_Id = mAuth.currentUser?.uid

        data = FirebaseDatabase.getInstance().getReference("Details")


        bun = this.requireArguments()
        var s = bun.getString("Name") as String
        name.setText(s)
        var id = bun.getString("Id") as String

        Msg = view.findViewById(R.id.message)
        send = view.findViewById(R.id.send_card)


        //sending msg
        send.setOnClickListener(View.OnClickListener {
            var text = Msg.text.toString()
            if(text.length > 0){
                val sdf = SimpleDateFormat("hh:mm:ss")
                val Sdf = SimpleDateFormat("hh:mm dd/MM/yyyy")
                val d = sdf.format(Date())
                val D = Sdf.format(Date())
                var sender = "S" + D + text
                if(User_Id != null && id != null) {
                    data.child(User_Id).child("Chatting").child(id).child(d).setValue(sender)
                }
                var  receiver = "R" + D + text
                if(User_Id != null && id != null) {
                    data.child(id).child("Chatting").child(User_Id).child(d).setValue(receiver)
                }
                Msg.setText("")
            }
        })

        return view
    }

}