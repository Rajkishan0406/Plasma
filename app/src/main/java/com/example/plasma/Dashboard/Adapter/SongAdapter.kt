package com.example.plasma.Dashboard.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Adapter.SongAdapter.ViewHolder
import com.example.plasma.Dashboard.Model.Song_Model
import com.example.plasma.R
import com.squareup.picasso.Picasso

class SongAdapter(var song_ad: ArrayList<Song_Model>,
                  private val listener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.song_details,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var PR = song_ad[position]

        holder.song.text = PR.Song
        holder.date.text = "Release Date : " + PR.Date
        holder.movie.text = "Movie : " + PR.Movie
        holder.singer.text = PR.Singer
        holder.type.text = "Type : " + PR.Type
        PR.Image?.let { holder.image.setImageResource(it) }



    }

    override fun getItemCount(): Int {
        return song_ad.size
    }

   inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var song = itemView.findViewById(R.id.song_name) as TextView
        var singer  = itemView.findViewById(R.id.singer_name) as TextView
        var type = itemView.findViewById(R.id.type) as TextView
        var date = itemView.findViewById(R.id.release) as TextView
        var movie = itemView.findViewById(R.id.movie) as TextView
        var image = itemView.findViewById(R.id.song_image) as ImageView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.OnItemClick(position)
            }
        }
    }

    interface  OnItemClickListener {
        fun OnItemClick(position : Int)
    }

}