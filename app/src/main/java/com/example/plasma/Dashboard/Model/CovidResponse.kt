package com.example.plasma.Dashboard.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CovidResponse {

    @SerializedName("Active Cases_text")
    @Expose
    private var activeCases: String? = null

    @SerializedName("Country_text")
    @Expose
    private var country: String? = null

    @SerializedName("New Cases_text")
    @Expose
    private var newCases: String? = null

    @SerializedName("New Deaths_text")
    @Expose
    private var newDeaths: String? = null

    @SerializedName("Total Cases_text")
    @Expose
    private var totalCases: String? = null

    @SerializedName("Total Recovered_text")
    @Expose
    private var totalRecovered: String? = null

    @SerializedName("Last Update")
    private var lastUpdate: String? = null



    constructor()

    constructor(activeCases: String?, country: String?, newCases: String?, newDeaths: String?, totalCases : String?, totalRecovered : String?, lastUpdate : String?) {
        this.activeCases = activeCases
        this.country = country
        this.newCases = newCases
        this.newDeaths = newDeaths
        this.totalCases = totalCases
        this.totalRecovered = totalRecovered
        this.lastUpdate = lastUpdate
    }



}