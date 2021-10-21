package com.example.plasma.Dashboard.Chat

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.LoginFragment
import com.example.plasma.Dashboard.NoInternetFragment
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatActivity : AppCompatActivity() {

    var id = "" as String
    var Name = "" as String
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

    override fun onPause() {
        super.onPause()
        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("0")
        }
    }

    //Online status for chat...
    override fun onRestart() {
        super.onRestart()
        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("1")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        id = intent?.getStringExtra("Id").toString()
        Name = intent?.getStringExtra("Name").toString()


        mAuth = FirebaseAuth.getInstance()
        var user_id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (user_id != null) {
            data.child(user_id).child("Online").setValue("1")
        }

        var IDF = ChatPageFragment()
        var bun : Bundle
        bun = Bundle()
        bun.putString("Name",Name)
        bun.putString("Id",id)
        IDF.arguments = bun
        this.supportFragmentManager.beginTransaction().replace(R.id.chat_frame,IDF).commit()

    }


    private fun setFragmentnoInternet(loginFragment: NoInternetFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.chat_frame,loginFragment)
        ft.commit()
    }

}