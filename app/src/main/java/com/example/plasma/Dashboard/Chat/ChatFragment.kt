package com.example.plasma.Dashboard.Chat

import android.os.Bundle
import android.util.Log
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
import com.example.plasma.Dashboard.Adapter.ChatFragmentAdapter
import com.example.plasma.Dashboard.Model.ChatFragmentModel
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class ChatFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

    lateinit var chatArrayList : ArrayList<ChatFragmentModel>
    lateinit var recyclerview : RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_chat, container, false)

        mAuth = FirebaseAuth.getInstance()
        var User_Id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")


        //RecyclerView..
        recyclerview = view.findViewById(R.id.recyclerview_chat)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        chatArrayList = arrayListOf<ChatFragmentModel>()


        if (User_Id != null) {
            data.child(User_Id).child("Chatting").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatArrayList.clear()
                    var msg = "" as String
                    var time = "" as String
                    var name = "" as String
                    var blod = "" as String
                    if(snapshot.exists()){
                        for(snap in snapshot.children){
                            var id = snap.key as String
                            Log.i("Last_Message :", " "+msg)
                            data.child(id).child("Profile").addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(sp: DataSnapshot) {
                                    if(sp.exists()){
                                        name = sp.child("Name").getValue() as String
                                        blod = sp.child("Blood_Grp").getValue() as String
                                        msg = snap.child("Last_Message").getValue() as String
                                        time = msg.substring(1,9)
                                        var MsG = msg.substring(20,msg.length)
                                        if(MsG.length > 25)
                                            MsG = MsG.substring(0,25) + "..."
                                        chatArrayList.add(ChatFragmentModel(name,time,blod,id,MsG))
                                    }
                                    val adapter = ChatFragmentAdapter(chatArrayList)
                                    recyclerview.adapter = adapter
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }



        return view
    }

}