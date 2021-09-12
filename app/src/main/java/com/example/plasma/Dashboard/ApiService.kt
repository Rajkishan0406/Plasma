package com.example.plasma.Dashboard

import android.telecom.Call
import com.example.plasma.Dashboard.Model.CovidResponse

import retrofit2.http.GET
import retrofit2.http.Headers


interface ApiService {

    @GET("v1/india")
    fun getIndiaData(): retrofit2.Call<CovidResponse>

}