package com.example.plasma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Dashboard.Chat.ChatFragment
import com.example.plasma.Dashboard.Home.HomeFragment
import com.example.plasma.Dashboard.Profile.ProfileFragment
import com.example.plasma.Dashboard.Setting.SettingFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class DashboardActivity : AppCompatActivity() {

    lateinit var bn : ChipNavigationBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bn = findViewById(R.id.main_menu)

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

}