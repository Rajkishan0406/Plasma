package com.example.plasma.Dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.plasma.MainActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var logout : CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_setting, container, false)

        mAuth = FirebaseAuth.getInstance()


       /* logout.setOnClickListener(View.OnClickListener {
            val intent = Intent(getActivity(), MainActivity::class.java)
            mAuth.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })*/

        return view
    }
}