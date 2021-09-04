package com.example.plasma.Dashboard.Chat

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Adapter.ChatAdapter
import com.example.plasma.Dashboard.Adapter.PlasmaRequestAdapter
import com.example.plasma.Dashboard.Home.RequestProfileFragment
import com.example.plasma.Dashboard.Model.ChatModel
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.DashboardActivity
import com.example.plasma.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
    lateinit var help : CardView
    lateinit var theme_back : RelativeLayout
    lateinit var image : ImageView
    private var imageUri: Uri? = null
    private val pickImage = 100
    lateinit var storage : StorageReference

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
        help = view.findViewById(R.id.help)
        theme_back = view.findViewById(R.id.theme_back)
        image = view.findViewById(R.id.send_image)

        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var background = pref.getString("Theme","0")

        if(background.equals("0")){
            theme_back.setBackgroundColor(Color.WHITE)
        }
        if(background.equals("1")){
            theme_back.setBackgroundColor(Color.GREEN)
        }
        if(background.equals("2")){
            theme_back.setBackgroundColor(Color.CYAN)
        }
        if(background.equals("3")){
            theme_back.setBackgroundColor(Color.YELLOW)
        }
        if(background.equals("4")){
            theme_back.setBackgroundColor(Color.GRAY)
        }

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
                            image.visibility = View.INVISIBLE
                            I_m_block = 1
                        }
                        else if(value.equals("1")){
                            unblock_card.visibility = View.VISIBLE
                            send.visibility = View.INVISIBLE
                            block_card.visibility = View.INVISIBLE
                            msg_card.visibility = View.INVISIBLE
                            image.visibility = View.INVISIBLE
                            I_m_block = 2
                        }
                        else{
                            unblock_card.visibility = View.INVISIBLE
                            send.visibility = View.VISIBLE
                            block_card.visibility = View.INVISIBLE
                            msg_card.visibility = View.VISIBLE
                            image.visibility = View.VISIBLE
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

        help.setOnClickListener(View.OnClickListener {
            val IDF = HelpChatFragment()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_dashboard_frame,IDF)?.addToBackStack(null)?.commit()

        })

        block.setOnClickListener(View.OnClickListener {
            if(I_m_block == 0){
                data.child(User_Id).child("Chatting").child(id).child("Block").setValue("1")
                data.child(id).child("Chatting").child(User_Id).child("Block").setValue("-1")
            }
            else if(I_m_block == 1){
                Toast.makeText(activity,"You can't blocked this user as that user already blocked you",Toast.LENGTH_SHORT).show()
            }
            menu.visibility = View.INVISIBLE
        })

        clear.setOnClickListener(View.OnClickListener {
            data.child(User_Id).child("Chatting").child(id).child("Message").removeValue()
            data.child(User_Id).child("Chatting").child(id).child("Last_Message").removeValue()
            Toast.makeText(activity,"All chats are cleared",Toast.LENGTH_SHORT).show()
            menu.visibility = View.INVISIBLE
        })

        theme.setOnClickListener(View.OnClickListener {
            var c = background?.toInt()
            if (c != null) {
                c = (c+1)%5
            }
            background = c.toString()
            var pref = PreferenceManager.getDefaultSharedPreferences(activity)
            pref.apply {
                val editor = pref.edit()
                editor.putString("Theme",c.toString())
                editor.apply()
            }
            menu.visibility = View.INVISIBLE
            if(background.equals("0")){
                theme_back.setBackgroundColor(Color.WHITE)
            }
            if(background.equals("1")){
                theme_back.setBackgroundColor(Color.GREEN)
            }
            if(background.equals("2")){
                theme_back.setBackgroundColor(Color.CYAN)
            }
            if(background.equals("3")){
                theme_back.setBackgroundColor(Color.YELLOW)
            }
            if(background.equals("4")){
                theme_back.setBackgroundColor(Color.GRAY)
            }
        })


        image.setOnClickListener(View.OnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        })

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val sdf = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss")
            val d = sdf.format(Date())
            Toast.makeText(activity,"Sending...",Toast.LENGTH_SHORT).show()
            storage =
                    FirebaseStorage.getInstance().reference.child("Message")
                            .child(d)
            storeimage(User_Id,id)
        }
    }

    private fun storeimage(User_id: String, id : String) {
        val sdf = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss")
        val Sdf = SimpleDateFormat("hh:mm a dd/MM/yyyy")
        val d = sdf.format(Date())
        val D = Sdf.format(Date())
        imageUri?.let {
            storage.putFile(it).addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    var r = "" as String
                    r = it.toString()
                    Log.i("Toekn : "," "+r)
                    var da: DatabaseReference
                    var sender = "s" + D + r
                    da = FirebaseDatabase.getInstance().getReference("Details")
                    da.child(User_id).child("Chatting").child(id).child("Message").child(d).setValue(sender)
                    da.child(User_id).child("Chatting").child(id).child("Last_Message").child(d).setValue("Image File")
                    sender = "r" + D + r
                    da.child(id).child("Chatting").child(User_id).child("Message").child(d).setValue(sender)
                    da.child(id).child("Chatting").child(User_id).child("Last_Message").child(d).setValue("Image File")
                }
                Log.i("image upload : ", "Successfull")
            }
                    .addOnFailureListener(){
                        Toast.makeText(activity, "Some thing went wrong!! please try again", Toast.LENGTH_SHORT).show()
                        Log.i("image upload : ", "Fail")
                        }
        }
    }

}