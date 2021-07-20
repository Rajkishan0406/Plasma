package com.example.plasma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.CovidDetailsFragment
import com.example.plasma.Authentication.LoginFragment
import com.example.plasma.Authentication.ProfileCreationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setFragment(ProfileCreationFragment())

    }

    private fun setFragment(loginFragment: ProfileCreationFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_auth_frame,loginFragment)
        ft.commit()
    }


}