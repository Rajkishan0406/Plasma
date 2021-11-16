package com.example.plasma.Dashboard.Setting

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.plasma.R

class developerFragment : Fragment() {

    lateinit var card : CardView
    lateinit var git : ImageView
    lateinit var linkedln : ImageView
    lateinit var DLayout : LinearLayout
    lateinit var insta : ImageView
    lateinit var resume : TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_developer, container, false)

        requireActivity().window.statusBarColor = Color.parseColor("#048AD3")

        DLayout = view.findViewById(R.id.DeveloperFragment)

        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        DLayout.startAnimation(animationx)

        card = view.findViewById(R.id.contact_card)
        git = view.findViewById(R.id.github)
        linkedln = view.findViewById(R.id.linkedln)
        insta = view.findViewById(R.id.instagram)
        resume = view.findViewById(R.id.resume)

        card.setCardBackgroundColor(Color.TRANSPARENT)
        card.cardElevation = 0F

        git.setOnClickListener(View.OnClickListener {
            var url = "https://github.com/Rajkishan0406" as String
            var i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        })

        linkedln.setOnClickListener(View.OnClickListener {
            var url = "https://www.linkedin.com/in/raj-kishan-0a02491b8/" as String
            var i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        })

        insta.setOnClickListener(View.OnClickListener {
            var url = "https://www.instagram.com/rajkishan04/" as String
            var i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        })

        resume.setOnClickListener(View.OnClickListener {
            var url = "https://drive.google.com/file/d/14IGzDWfgVd3O7FB9oDsoWqbP5rHgTF8Y/view?usp=drivesdk" as String
            var i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            startActivity(i)
        })


        return view
    }

}