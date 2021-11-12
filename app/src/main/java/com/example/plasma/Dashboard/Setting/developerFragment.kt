package com.example.plasma.Dashboard.Setting

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.plasma.R

class developerFragment : Fragment() {

    lateinit var card : CardView
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_developer, container, false)

        requireActivity().window.statusBarColor = Color.parseColor("#048AD3")

        card = view.findViewById(R.id.contact_card)

        card.setCardBackgroundColor(Color.TRANSPARENT)
        card.cardElevation = 0F
        
        return view
    }

}