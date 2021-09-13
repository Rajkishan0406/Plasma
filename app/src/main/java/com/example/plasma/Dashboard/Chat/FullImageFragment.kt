package com.example.plasma.Dashboard.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.plasma.R
import com.squareup.picasso.Picasso

class FullImageFragment : Fragment() {

    lateinit var image : ImageView
    lateinit var bun : Bundle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_full_image, container, false)

        image = view.findViewById(R.id.full_image)

        bun = Bundle()
        bun = this.requireArguments()
        var url : String? = bun.getString("image")

        if(url != null)
        Picasso.get().load(url).into(image)

        return view
    }


}