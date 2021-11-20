package com.example.plasma.Dashboard.Setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import com.example.plasma.R

class AboutPlasmaFragment : Fragment() {


    lateinit var APLayout : ScrollView
    lateinit var site1 : TextView
    lateinit var site2 : TextView
    lateinit var site3 : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_about_plasma, container, false)

        APLayout = view.findViewById(R.id.AboutPlasmaFragment)
        site1 = view.findViewById(R.id.site1)
        site2 = view.findViewById(R.id.site2)
        site3 = view.findViewById(R.id.site3)

        site1.setOnClickListener(View.OnClickListener {
            var url = "https:covidplasma.online/" as String
            var i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        })

        site3.setOnClickListener(View.OnClickListener {
            var url = "http://needplasma.in/" as String
            var i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        })

        site2.setOnClickListener(View.OnClickListener {
            var url = "https://dhoondh.com" as String
            var i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        })


        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        APLayout.startAnimation(animationx)

        return view
    }

}