package com.example.plasma.Authentication

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Dashboard.NoInternetFragment
import com.example.plasma.DashboardActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var frame : LinearLayout
    lateinit var frame2 : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_splash, container, false)


        mAuth = FirebaseAuth.getInstance()
        frame = view.findViewById(R.id.other_half)
        frame2 = view.findViewById(R.id.app)
        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fadein)
        frame.startAnimation(animationx)
        frame2.startAnimation(animationx)

        Handler().postDelayed({
            val currentUser = mAuth.currentUser
            if(currentUser != null){
                val intent = Intent(getActivity(), DashboardActivity::class.java)
                getActivity()?.startActivity(intent)
            }
            else {
                setFragment(LoginFragment())
            }
        }, 2000)

        return view
    }

    private fun setFragment(forgotFragment: LoginFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_auth_frame, forgotFragment)
        }
        if (ft != null) {
            ft.commit()
        }
    }


}