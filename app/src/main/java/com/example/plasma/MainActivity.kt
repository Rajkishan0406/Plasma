package com.example.plasma

import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.CovidDetailsFragment
import com.example.plasma.Authentication.LoginFragment
import com.example.plasma.Authentication.ProfileCreationFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth

    public override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(MainActivity@this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



            setFragment(LoginFragment())

    }

    private fun setFragment(loginFragment: LoginFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_auth_frame,loginFragment)
        ft.commit()
    }


}