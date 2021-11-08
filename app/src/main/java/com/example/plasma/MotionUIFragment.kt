package com.example.plasma

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Adapter.SongAdapter
import com.example.plasma.Dashboard.Model.Song_Model

class MotionUIFragment : Fragment() , SongAdapter.OnItemClickListener {

    lateinit var card : CardView
    lateinit var songList : ArrayList<Song_Model>
    lateinit var recyclerview : RecyclerView
    lateinit var song : TextView
    lateinit var singer : TextView
    lateinit var image : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_motion_u_i, container, false)

        requireActivity().window.statusBarColor = Color.parseColor("#FF1E1C1C")

        card = view.findViewById(R.id.cardStart)
        song = view.findViewById(R.id.song_name)
        singer = view.findViewById(R.id.singer_name)
        image = view.findViewById(R.id.image)

        card.setCardBackgroundColor(Color.TRANSPARENT)
        card.cardElevation = 0F


        recyclerview = view.findViewById(R.id.recycler_view_song)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        songList = arrayListOf<Song_Model>()


        songList.add(Song_Model("Darshan Raval","Asal Mein","12th Nov 2018","Classic","Album",R.drawable.asal_mai))
        songList.add(Song_Model("Darshan Raval","Tera Zikr 2.O","08th Sept 2018","Classic","Album",R.drawable.tera_zikr))
        songList.add(Song_Model("Darshan Raval","Shab Tum Ho","12th Nov 2018","Classic","Album",R.drawable.sab_tum_ho))
        songList.add(Song_Model("Yassir Desai","Tum Ho Mere Pass","08th Sept 2018","Classic","Album",R.drawable.machine))
        songList.add(Song_Model("Pritam","Mehrama","12th Nov 2018","Classic","Album",R.drawable.mehrama))
        songList.add(Song_Model("Darshan Raval","Khabhi Tumhhe","12th Nov 2018","Classic","Album",R.drawable.khabhi_tumhe))
        songList.add(Song_Model("Arjit Singh","Bolna","12th Nov 2018","Classic","Album",R.drawable.bolna))

        val adapter = SongAdapter(songList, this)
        recyclerview.adapter = adapter

        return view
    }

    override fun OnItemClick(position: Int) {
            val clickedItem = songList[position]
            song.setText(clickedItem.Song)
            singer.setText(clickedItem.Singer)
            clickedItem.Image?.let { image.setImageResource(it) }
    }
}