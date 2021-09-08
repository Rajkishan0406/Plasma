package com.example.plasma.Dashboard.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plasma.R

class ShowMapFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view =  inflater.inflate(R.layout.fragment_show_map, container, false)

        return view
    }
}