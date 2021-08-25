package com.example.plasma.Dashboard.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import soup.neumorphism.NeumorphButton
import java.text.SimpleDateFormat
import java.util.*

class RulesAndRegulationFragment : Fragment() {

    lateinit var btn : NeumorphButton

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    var unique = "" as String
    var number2 = "" as String
    lateinit var male : FrameLayout
    lateinit var female : FrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_rules_and_regulation, container, false)

        male = view.findViewById(R.id.male_frame)
        female = view.findViewById(R.id.female_frame)

        var bun = Bundle()
        bun = this.requireArguments()
        var id : String? = bun.getString("User_Id") as String
        unique = bun.getString("Number") as String
        var gen = bun.getString("Gender") as String

        if(gen.equals("male")){
            male.visibility = View.VISIBLE
            female.visibility = View.INVISIBLE
        }
        else{
            male.visibility = View.INVISIBLE
            female.visibility = View.VISIBLE

        }


        mAuth = FirebaseAuth.getInstance()
        var User_id = mAuth.currentUser?.uid

        data = FirebaseDatabase.getInstance().getReference("Details")

        if(User_id != null) {
            data.child(User_id).child("Profile").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        number2 = snapshot.child("Number").getValue() as String
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }


        btn = view.findViewById(R.id.donate)

        btn.setOnClickListener(View.OnClickListener {
            if(gen.equals("male")){

                if(number2.length > 0) {
                    if (id != null) {
                        data.child(id).child("Donation_Want").child(number2).setValue(User_id)
                    }
                    if (User_id != null) {
                        data.child(User_id).child("Donation_Give").child(unique).setValue(id)
                    }
                    Toast.makeText(activity, "Your response is successfully", Toast.LENGTH_SHORT).show()
                }

            }
            else{

                if(number2.length > 0) {
                    if (id != null) {
                        data.child(id).child("Donation_Want").child(number2).setValue(User_id)
                    }
                    if (User_id != null) {
                        data.child(User_id).child("Donation_Give").child(unique).setValue(id)
                    }
                    Toast.makeText(activity, "Your response is successfully", Toast.LENGTH_SHORT).show()
                }

            }
        })


        return view;
    }

}