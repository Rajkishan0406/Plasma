package com.example.plasma.Dashboard.Setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plasma.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CovidDetailFragment : Fragment() {

    lateinit var new_case : TextView
    lateinit var death : TextView
    lateinit var recover : TextView
    lateinit var active : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_covid_detail, container, false)


        new_case = view.findViewById(R.id.new_cases_covid)
        death = view.findViewById(R.id.new_death_cases)
        recover = view.findViewById(R.id.total_recovered_covid)
        active = view.findViewById(R.id.active_cases_covid)




        return view
    }

}