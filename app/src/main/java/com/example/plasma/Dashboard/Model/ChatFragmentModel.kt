package com.example.plasma.Dashboard.Model

class ChatFragmentModel {

    var Name : String? = null
    var Time : String? = null
    var Blood : String? = null
    var Id : String? = null
    var Last_Message : String? = null
    var online : String? = null
    var last_seen : String? = null
    var from : String? = null

    constructor(){

    }

    constructor(Name: String?, Time: String?,Blood: String?, id: String?, Last_Message: String?, online : String,last_seen : String
    ,from : String) {
        this.Name = Name
        this.Time = Time
        this.Id = id
        this.Blood = Blood
        this.Last_Message = Last_Message
        this.online = online
        this.last_seen = last_seen
        this.from = from
    }

}