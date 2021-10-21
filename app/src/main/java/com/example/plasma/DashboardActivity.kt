package com.example.plasma

import android.app.Notification
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.ProfileCreationFragment
import com.example.plasma.Dashboard.Chat.ChatFragment
import com.example.plasma.Dashboard.Home.HomeFragment
import com.example.plasma.Dashboard.NoInternetFragment
import com.example.plasma.Dashboard.Profile.ProfileFragment
import com.example.plasma.Dashboard.Setting.CovidDetailFragment
import com.example.plasma.Dashboard.Setting.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class DashboardActivity : AppCompatActivity() {

    lateinit var bn : ChipNavigationBar
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    private lateinit var notificationbadges : View
    private var count = 1 as Int


    //Offline status for chat...
    override fun onPause() {
        super.onPause()
        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("0")
        }
    }

    override fun onResume() {
        super.onResume()
        checkConnection()
    }

    //Online status for chat...
    override fun onRestart() {
        super.onRestart()
        checkConnection()
        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("1")
        }
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        super.onActionModeStarted(mode)
        checkConnection()
        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("1")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        checkConnection()

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("1")
        }


        bn = findViewById(R.id.main_menu)

        //updatebadgeCount(0)

        bn.setOnItemSelectedListener {
            id ->
            when (id) {
                R.id.home -> setFragmentHome(HomeFragment())
                R.id.chat -> setFragmentChat(ChatFragment())
                R.id.profile -> setFragmentProfile(ProfileFragment())
                R.id.settings -> setFragmentSetting(SettingFragment())

            }
        }

         setFragmentHome(HomeFragment())

    }

    private fun setFragmentHome(loginFragment: HomeFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_dashboard_frame,loginFragment)
        ft.commit()
    }

    private fun setFragmentChat(loginFragment: ChatFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_dashboard_frame,loginFragment)
        ft.commit()
    }

    private fun setFragmentProfile(loginFragment: ProfileFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_dashboard_frame,loginFragment)
        ft.commit()
    }

    private fun setFragmentSetting(loginFragment: SettingFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_dashboard_frame,loginFragment)
        ft.commit()
    }


    private fun setFragmentnoInternet(loginFragment: NoInternetFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_dashboard_frame,loginFragment)
        ft.commit()
    }

    private fun checkConnection() {
        val manager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfor = manager.activeNetworkInfo


        if (networkInfor != null) {
            if(networkInfor.type != ConnectivityManager.TYPE_WIFI && networkInfor.type != ConnectivityManager.TYPE_MOBILE) {
                Log.i("Internet Connection : ", " No Connection")
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"No Network Information!",Toast.LENGTH_SHORT).show()
            setFragmentnoInternet(NoInternetFragment())
        }
    }

}