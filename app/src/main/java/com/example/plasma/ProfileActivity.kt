package com.example.plasma

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.LoginFragment
import com.example.plasma.Authentication.SignUpFragment
import com.example.plasma.Dashboard.Home.RequestProfileFragment
import com.example.plasma.Dashboard.NoInternetFragment

class ProfileActivity : AppCompatActivity() {


    override fun onStart() {
        super.onStart()
        checkConnection()
    }

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

    private fun setFragmentnoInternet(loginFragment: NoInternetFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.profile_frame,loginFragment)
        ft.commit()
    }

    private fun checkConnection() {
        val manager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfor = manager.activeNetworkInfo

        if (networkInfor != null) {
            if(networkInfor.type != ConnectivityManager.TYPE_WIFI && networkInfor.type != ConnectivityManager.TYPE_MOBILE) {
                setFragmentnoInternet(NoInternetFragment())
            }
        }
        else{
            setFragmentnoInternet(NoInternetFragment())
        }

    }


}