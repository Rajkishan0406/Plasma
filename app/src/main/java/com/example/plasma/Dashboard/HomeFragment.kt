package com.example.plasma.Dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    lateinit var user : TextView
    lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)


        user = view.findViewById(R.id.user)
        mAuth = FirebaseAuth.getInstance()


        return view
    }

}