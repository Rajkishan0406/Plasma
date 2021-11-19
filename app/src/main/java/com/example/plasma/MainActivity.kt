package com.example.plasma

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.CovidDetailsFragment
import com.example.plasma.Authentication.LoginFragment
import com.example.plasma.Authentication.ProfileCreationFragment
import com.example.plasma.Authentication.SplashFragment
import com.example.plasma.Dashboard.NoInternetFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth

    public override fun onStart() {
        super.onStart()
        checkConnection()
    }

    override fun onResume() {
        super.onResume()
        checkConnection()
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


            checkConnection()


            setFragment(SplashFragment())

    }

    private fun setFragment(loginFragment: SplashFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_auth_frame,loginFragment)
        ft.commit()
    }

    private fun setFragmentnoInternet(loginFragment: NoInternetFragment) {
        var ft: FragmentTransaction = supportFragmentManager.beginTransaction();
        ft.replace(R.id.main_auth_frame,loginFragment)
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