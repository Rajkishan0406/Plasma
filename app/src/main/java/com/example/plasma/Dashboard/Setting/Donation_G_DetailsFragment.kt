package com.example.plasma.Dashboard.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plasma.R

class Donation_G_DetailsFragment : Fragment() {

    lateinit var bun : Bundle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_donation__g__details, container, false)

        // bundle code for extracting user id....
        bun = Bundle()
        bun = this.requireArguments()
        var id : String? = bun.getString("Id") as String

        return view
    }

}