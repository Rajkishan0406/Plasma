package com.example.plasma.Dashboard.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.example.plasma.R

class AboutPlasmaFragment : Fragment() {


    lateinit var APLayout : RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_about_plasma, container, false)

        APLayout = view.findViewById(R.id.AboutPlasmaFragment)

        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        APLayout.startAnimation(animationx)

        return view
    }

}