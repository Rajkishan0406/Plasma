package com.example.plasma.Dashboard.Model

class ChatModel {

    var Message : String? = null
    var Time : String? = null
    var From : String? = null
    var day : String? = null
    var id : String? = null
    var Last_msg_id : String? = null
    var old_msg : String? = null
    var user_Id : String? = null


    constructor(){

    }

    constructor(Message: String?, Time: String?, From: String?, day: String?, id: String,Last_msg_id : String,old_msg : String,user_Id : String) {
        this.Message = Message
        this.Time = Time
        this.From = From
        this.day = day
        this.id = id
        this.Last_msg_id = Last_msg_id
        this.old_msg = old_msg
        this.user_Id = user_Id
    }



}