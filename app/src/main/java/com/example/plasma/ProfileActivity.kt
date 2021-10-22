package com.example.plasma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.LoginFragment
import com.example.plasma.Authentication.SignUpFragment
import com.example.plasma.Dashboard.Home.RequestProfileFragment

class ProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val myintent = getIntent()
        val id = myintent.getStringExtra("Id") as String


        val IDF = RequestProfileFragment()
        var bun : Bundle
        bun = Bundle()
        bun.putString("Id",id)
        IDF.arguments = bun
        this.supportFragmentManager.beginTransaction().replace(R.id.profile_frame,IDF).commit()

    }

}