package com.example.plasma.Dashboard.Chat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
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
    lateinit var setting : ImageView
    lateinit var block_card : CardView
    lateinit var unblock_card : CardView
    lateinit var msg_card : TextInputLayout

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
        setting = view.findViewById(R.id.setting)
        msg_card = view.findViewById(R.id.msg_card)
        block_card = view.findViewById(R.id.block_card)
        unblock_card = view.findViewById(R.id.unblock_card)

        //FireBase Intitializer...
        name.setText(s)
        mAuth = FirebaseAuth.getInstance()
        var User_Id = mAuth.currentUser?.uid
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
                        }
                        else if(value.equals("1")){
                            unblock_card.visibility = View.VISIBLE
                            send.visibility = View.INVISIBLE
                            block_card.visibility = View.INVISIBLE
                            msg_card.visibility = View.INVISIBLE
                        }
                        else{
                            unblock_card.visibility = View.INVISIBLE
                            send.visibility = View.VISIBLE
                            block_card.visibility = View.INVISIBLE
                            msg_card.visibility = View.VISIBLE
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

        //setting menu
        setting.setOnClickListener(View.OnClickListener {

        })

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_menu,menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 1){
            Toast.makeText(activity,"Block",Toast.LENGTH_SHORT).show()
        }
        else if(item.itemId == 2){
            Toast.makeText(activity,"Clear Chat",Toast.LENGTH_SHORT).show()
        }
        else if(item.itemId == 3){
            Toast.makeText(activity,"Theme",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}