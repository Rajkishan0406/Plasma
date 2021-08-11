package com.example.plasma.Dashboard.Model

class PlasmaRequestModel {

    var Name : String? = null
    var City : String? = null
    var State : String? = null
    var Blood : String? = null
    var Id : String? = null

    constructor(){

    }

    constructor(Name: String?, City: String?, State: String?, Blood: String?, Id : String?) {
        this.Name = Name
        this.City = City
        this.State = State
        this.Blood = Blood
        this.Id = Id
    }
}