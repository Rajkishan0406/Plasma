package com.example.plasma.Dashboard.Model

import android.media.Image

class Song_Model {

    var Singer : String? = null
    var Song : String? = null
    var Date : String? = null
    var Type : String? = null
    var Movie : String? = null
    var Image : Int? = null


    constructor(){

    }

    constructor(Singer: String?, Song: String?, Date: String?, Type: String?, Movie: String?, Image: Int) {
        this.Singer = Singer
        this.Song = Song
        this.Date = Date
        this.Type = Type
        this.Movie = Movie
        this.Image = Image
    }


}