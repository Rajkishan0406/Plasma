package com.example.plasma.Dashboard.Chat

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Adapter.ChatAdapter
import com.example.plasma.Dashboard.Adapter.PlasmaRequestAdapter
import com.example.plasma.Dashboard.Home.RequestProfileFragment
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.R
import com.google.android.material.textfield.TextInputLayout
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
    lateinit var block_card : CardView
    lateinit var unblock_card : CardView
    lateinit var setting : ImageView
    lateinit var menu : CardView
    lateinit var msg_card : TextInputLayout
    lateinit var block : CardView
    lateinit var clear : CardView
    lateinit var theme : CardView

    lateinit var chatArrayList : ArrayList<ChatModel>
    lateinit var recyclerview : RecyclerView
    var I_m_block = 0 as Int
    var User_Id = "" as String
    var id = "" as String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_chat_page, container, false)


        //Bundle code...
        bun = this.requireArguments()
        var s = bun.getString("Name") as String
        id = bun.getString("Id") as String

        //Find View BY ID...
        name = view.findViewById(R.id.User_Name)
        Msg = view.findViewById(R.id.message)
        send = view.findViewById(R.id.send_card)
        msg_card = view.findViewById(R.id.msg_card)
        block_card = view.findViewById(R.id.block_card)
        unblock_card = view.findViewById(R.id.unblock_card)
        setting = view.findViewById(R.id.setting)
        menu = view.findViewById(R.id.menu_card)
        block = view.findViewById(R.id.block)
        clear = view.findViewById(R.id.clear_chat)
        theme = view.findViewById(R.id.theme)

        //FireBase Intitializer...
        name.setText(s)
        mAuth = FirebaseAuth.getInstance()
        User_Id = mAuth.currentUser?.uid.toString()
        data = FirebaseDatabase.getInstance().getReference("Details")

        if (User_Id != null) {
            data.child(User_Id).child("Chatting").child(id).child("Block").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var value = snapshot.getValue() as String
                        if(value.equals("-1")){
                            send.visibility = View.INVISIBLE
                            block_card.visibility = View.VISIBLE
                            msg_card.visibility = View.INVISIBLE
                            I_m_block = 1
                        }
                        else if(value.equals("1")){
                            unblock_card.visibility = View.VISIBLE
                            send.visibility = View.INVISIBLE
                            block_card.visibility = View.INVISIBLE
                            msg_card.visibility = View.INVISIBLE
                            I_m_block = 2
                        }
                        else{
                            unblock_card.visibility = View.INVISIBLE
                            send.visibility = View.VISIBLE
                            block_card.visibility = View.INVISIBLE
                            msg_card.visibility = View.VISIBLE
                            I_m_block = 0
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        unblock_card.setOnClickListener(View.OnClickListener {
            if (User_Id != null) {
                data.child(User_Id).child("Chatting").child(id).child("Block").setValue("0")
            }
            if (id != null && User_Id != null) {
                data.child(id).child("Chatting").child(User_Id).child("Block").setValue("0")
            }
        })

        name.setOnClickListener(View.OnClickListener {
            val IDF = RequestProfileFragment()
            var bun : Bundle
            bun = Bundle()
            bun.putString("Id",id)
            IDF.arguments = bun
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_dashboard_frame,IDF)?.addToBackStack(null)?.commit()
        })

        //RecyclerView..
        recyclerview = view.findViewById(R.id.message_recyclerview)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity).apply {
            stackFromEnd = true
            reverseLayout = false
        }
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
            if(id.equals(User_Id)){
                Toast.makeText(activity,"You can't message to yourself!!",Toast.LENGTH_SHORT).show()
                Msg.setText("")
            }
            else if(text.length > 100){
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

        setting.setOnClickListener(View.OnClickListener {
            if(menu.isVisible) {
                menu.visibility = View.INVISIBLE
            }
            else {
                menu.visibility = View.VISIBLE
            }
        })

        recyclerview.setOnClickListener(View.OnClickListener {
            if(menu.isVisible){
                menu.visibility = View.INVISIBLE
            }
        })

        block.setOnClickListener(View.OnClickListener {
            if(I_m_block == 0){
                data.child(User_Id).child("Chatting").child(id).child("Block").setValue("1")
                data.child(id).child("Chatting").child(User_Id).child("Block").setValue("-1")
            }
            else if(I_m_block == 1){
                Toast.makeText(activity,"You can't blocked this user as that user already blocked you",Toast.LENGTH_SHORT).show()
            }
        })

        clear.setOnClickListener(View.OnClickListener {
            data.child(User_Id).child("Chatting").child(id).child("Message").removeValue()
            data.child(User_Id).child("Chatting").child(id).child("Last_Message").removeValue()
            Toast.makeText(activity,"All chats are cleared",Toast.LENGTH_SHORT).show()
        })

        theme.setOnClickListener(View.OnClickListener {

        })

        return view
    }

}