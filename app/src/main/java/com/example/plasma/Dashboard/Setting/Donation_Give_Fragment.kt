package com.example.plasma.Dashboard.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plasma.Dashboard.Adapter.DonationGiveAdapter
import com.example.plasma.Dashboard.Adapter.PlasmaRequestAdapter
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Donation_Give_Fragment : Fragment() {

    lateinit var recyclerview : RecyclerView
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var data2 : DatabaseReference
    lateinit var progress : ProgressBar
    lateinit var donationArrayList : ArrayList<PlasmaRequestModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_donation__give_, container, false)


        mAuth = FirebaseAuth.getInstance()
        progress = view.findViewById(R.id.progress_donation_give)
        var id = mAuth.currentUser?.uid

        data = id?.let { FirebaseDatabase.getInstance().getReference("Details").child(it) }!!
        data2 = FirebaseDatabase.getInstance().getReference("Details")



        recyclerview = view.findViewById(R.id.recyclerview_donation_give)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        var arrlist = arrayListOf<String>()

        data.child("Donation_Give").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(donationSnapshot in snapshot.children) {
                            var d = donationSnapshot.getValue() as String
                            arrlist.add(d)
                        }
                    }
                else{
                        progress.visibility = View.INVISIBLE
                        Toast.makeText(activity,"No donation responses has done",Toast.LENGTH_SHORT).show()
                    }
                reterival(arrlist)
            }
            override fun onCancelled(error: DatabaseError) {}
        })


        return view
    }

    private fun reterival(d: ArrayList<String>) {
        donationArrayList = arrayListOf<PlasmaRequestModel>()
        for (a: String in d) {
            data2.child(a).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var name = snapshot.child("Profile").child("Name").getValue() as String
                        var city = snapshot.child("Profile").child("City").getValue() as String
                        var state = snapshot.child("Profile").child("State").getValue() as String
                        var blood = snapshot.child("Profile").child("Blood_Grp").getValue() as String
                        var id = snapshot.child("Profile").child("Id").getValue() as String
                        donationArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                    }
                    val adapter = DonationGiveAdapter(donationArrayList)
                    recyclerview.adapter = adapter
                    progress.visibility = View.INVISIBLE
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        }
    }
}