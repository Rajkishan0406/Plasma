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

        recyclerview = view.findViewById(R.id.recyclerview_donation_give)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        donationArrayList = arrayListOf<PlasmaRequestModel>()

        data.child("Donation_Give").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(donationSnapshot in snapshot.children) {
                            var d = donationSnapshot.getValue() as String
                            Toast.makeText(activity,""+d,Toast.LENGTH_SHORT).show()
                        }
                        val adapter = DonationGiveAdapter(donationArrayList)
                        recyclerview.adapter = adapter
                        progress.visibility = View.INVISIBLE
                    }
                else{
                        Toast.makeText(activity,"No donation responses has done",Toast.LENGTH_SHORT).show()
                    }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        return view
    }
}