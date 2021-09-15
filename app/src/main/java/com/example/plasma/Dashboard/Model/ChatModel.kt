package com.example.plasma.Dashboard.Model

class ChatModel {

    var Message : String? = null
    var Time : String? = null
    var From : String? = null
    var day : String? = null
    var id : String? = null

    constructor(){

    }

    constructor(Message: String?, Time: String?, From: String?, day: String?, id: String) {
        this.Message = Message
        this.Time = Time
        this.From = From
        this.day = day
        this.id = id
    }



}