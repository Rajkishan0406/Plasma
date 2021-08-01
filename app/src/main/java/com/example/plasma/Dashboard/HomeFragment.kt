package com.example.plasma.Dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Adapter.PlasmaRequestAdapter
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    lateinit var recyclerview : RecyclerView
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var plasmaArrayList : ArrayList<PlasmaRequestModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        mAuth = FirebaseAuth.getInstance()
        data = FirebaseDatabase.getInstance().getReference("Details")

        recyclerview = view.findViewById(R.id.recyclerview_plasma_request)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        plasmaArrayList = arrayListOf<PlasmaRequestModel>()

        data.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                plasmaArrayList.clear()
                if(snapshot.exists()){
                    for(plasmarequestSnapshot in snapshot.children) {
                        if (plasmarequestSnapshot.hasChild("PlasmaRequest")) {
                            var ss = plasmarequestSnapshot.child("PlasmaRequest").getValue() as String
                            if (ss.equals("1")) {
                                var name = plasmarequestSnapshot.child("Profile").child("Name").getValue() as String
                                var city = plasmarequestSnapshot.child("Profile").child("City").getValue() as String
                                var state = plasmarequestSnapshot.child("Profile").child("State").getValue() as String
                                var blood = plasmarequestSnapshot.child("Profile").child("Blood_Grp").getValue() as String
                                plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood))
                            }
                        }
                    }
                }
                val adapter = PlasmaRequestAdapter(plasmaArrayList)
                recyclerview.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



        return view
    }

}