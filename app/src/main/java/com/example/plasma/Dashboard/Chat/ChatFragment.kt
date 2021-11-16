package com.example.plasma.Dashboard.Chat

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.plasma.Dashboard.Adapter.ChatFragmentAdapter
import com.example.plasma.Dashboard.Model.ChatFragmentModel
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class ChatFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var data2 : DatabaseReference
    lateinit var card : CardView
    lateinit var Rlayout : RelativeLayout
    var online = "0" as String

    lateinit var chatArrayList : ArrayList<ChatFragmentModel>
    lateinit var recyclerview : RecyclerView
    lateinit var pro : LottieAnimationView

    override fun onResume() {
        super.onResume()
        mAuth = FirebaseAuth.getInstance()
        var nodata = 0 as Int
        var User_Id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        data2 = FirebaseDatabase.getInstance().getReference("Details")
        if (User_Id != null) {
            data.child(User_Id).child("Chatting").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatArrayList.clear()
                    var msg = "" as String
                    var time = "" as String
                    var name = "" as String
                    var blod = "" as String
                    var MsG = "" as String
                    var last_seen = "zzz" as String
                    var from = "" as String
                    if(snapshot.exists()){
                        for(snap in snapshot.children){
                            var id = snap.key as String
                            Log.i("Last_Message :", " "+msg)
                            data.child(id).addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(s: DataSnapshot) {
                                    if(s.exists()) {
                                        online = s.child("Online").getValue() as String
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                            data2.child(id).child("Chatting").child(User_Id).addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(sshot: DataSnapshot) {
                                    if(sshot.exists()){
                                        if(sshot.hasChild("Last_Seen"))
                                            last_seen = sshot.child("Last_Seen").getValue() as String
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                            data.child(id).child("Profile").addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(sp: DataSnapshot) {
                                    if(sp.exists()){
                                        name = sp.child("Name").getValue() as String
                                        blod = sp.child("Blood_Grp").getValue() as String
                                        if(snap.hasChild("Last_Message")) {
                                            msg = snap.child("Last_Message").getValue() as String
                                            Log.i("MSG : "," "+msg)
                                            if (msg.substring(0, 2).equals("Sv") || msg.substring(0, 2).equals("Rv")) {
                                                from = msg.substring(0, 2)
                                                time = msg.substring(2, 18)
                                                MsG = msg.substring(40, msg.length)
                                                nodata = 1
                                                if (MsG.length > 25)
                                                    MsG = MsG.substring(0, 25) + "..."
                                                if(MsG.length > 0) {
                                                    chatArrayList.add(ChatFragmentModel(name, time, blod, id, MsG, online, last_seen, from))
                                                }
                                            }
                                            else{
                                                time = msg.substring(1, 17)
                                                from = msg.substring(0, 1)
                                                MsG = msg.substring(20, msg.length)
                                                nodata = 1
                                                Log.i("MSG : "," "+MsG)
                                                if (MsG.length > 25)
                                                    MsG = MsG.substring(0, 25) + "..."
                                                if(MsG.length > 0) {
                                                    chatArrayList.add(ChatFragmentModel(name, time, blod, id, MsG, online, last_seen, from))
                                                }
                                            }
                                        }
                                    }
                                    if(chatArrayList.size > 0)
                                        pro.visibility = View.INVISIBLE
                                    val adapter = ChatFragmentAdapter(chatArrayList) { chat -> }
                                    recyclerview.adapter = adapter
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }
                    }
                    else {
                        if (nodata == 0)
                            pro.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_chat, container, false)


        requireActivity().window.statusBarColor = Color.parseColor("#048AD3")

        Rlayout = view.findViewById(R.id.ChatFragment)
        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        Rlayout.startAnimation(animationx)

        card = view.findViewById(R.id.upper_card)

        val animation = AnimationUtils.loadAnimation(activity, R.anim.top_to_down)
        card.startAnimation(animation)

        mAuth = FirebaseAuth.getInstance()
        var User_Id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")

        pro = view.findViewById(R.id.load_progress)


        //RecyclerView..
        recyclerview = view.findViewById(R.id.recyclerview_chat)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        chatArrayList = arrayListOf<ChatFragmentModel>()




        return view
    }

}