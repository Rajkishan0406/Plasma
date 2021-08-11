package com.example.plasma.Dashboard.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import soup.neumorphism.NeumorphButton
import java.text.SimpleDateFormat
import java.util.*

class RulesAndRegulationFragment : Fragment() {

    lateinit var btn : NeumorphButton

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_rules_and_regulation, container, false)

        var bun = Bundle()
        bun = this.requireArguments()
        var id : String? = bun.getString("User_Id") as String

        mAuth = FirebaseAuth.getInstance()
        var User_id = mAuth.currentUser?.uid

        data = FirebaseDatabase.getInstance().getReference("Details")

        btn = view.findViewById(R.id.donate)

        btn.setOnClickListener(View.OnClickListener {
            var sdf = SimpleDateFormat("hh:mm:ss")
            var time = sdf.format(Date())
            if (id != null) {
                data.child(id).child("Donation_Want").child(time).setValue(User_id)
            }
            if (User_id != null) {
                data.child(User_id).child("Donation_Give").child(time).setValue(id)
            }
            Toast.makeText(activity,"Your response is successfully",Toast.LENGTH_SHORT).show()
        })


        return view;
    }

}