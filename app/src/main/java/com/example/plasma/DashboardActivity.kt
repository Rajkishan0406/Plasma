package com.example.plasma

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.plasma.Authentication.LoginFragment
import com.example.plasma.Dashboard.ChatFragment
import com.example.plasma.Dashboard.HomeFragment
import com.example.plasma.Dashboard.ProfileFragment
import com.example.plasma.Dashboard.SettingFragment
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