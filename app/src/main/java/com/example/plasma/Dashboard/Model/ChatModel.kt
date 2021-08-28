package com.example.plasma.Dashboard.Model

class ChatModel {

    var Message : String? = null
    var Time : String? = null
    var From : String? = null

    constructor(){

    }

    constructor(Message: String?, Time: String?, From: String?) {
        this.Message = Message
        this.Time = Time
        this.From = From
    }

}