package com.example.plasma.Dashboard.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.example.plasma.R

class HelpChatFragment : Fragment() {

    lateinit var help : LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_help_chat, container, false)

        help = view.findViewById(R.id.helpChatFragment)

        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        help.startAnimation(animationx)


        return view
    }
}