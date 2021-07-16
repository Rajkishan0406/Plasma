package com.example.plasma.Dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.plasma.R

class ProfileFragment : Fragment() {

    lateinit var personal : CardView
    lateinit var covid : CardView
    lateinit var personal_frame : FrameLayout
    lateinit var covid_frame : FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)


        personal_frame = view.findViewById(R.id.PersonaL_Frame)
        covid_frame = view.findViewById(R.id.CoronA_Frame)

        covid = view.findViewById(R.id.covid_details)
        personal = view.findViewById(R.id.personal_details)

        covid.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_go)
            personal_frame.startAnimation(animation)
            personal_frame.visibility = View.INVISIBLE
            val animation2 = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_come)
            covid_frame.startAnimation(animation2)
            covid_frame.visibility = View.VISIBLE
        })

        personal.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_come)
            personal_frame.startAnimation(animation)
            personal_frame.visibility = View.VISIBLE
            val animation2 = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_go)
            covid_frame.startAnimation(animation2)
            covid_frame.visibility = View.INVISIBLE
        })

        return view
    }

}