package com.example.plasma.Dashboard.Setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.ForgotPassword
import com.example.plasma.Dashboard.Profile.ReportFragment
import com.example.plasma.MainActivity
import com.example.plasma.R
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.math.max

class SettingFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var logout : CardView
    lateinit var request : CardView
    lateinit var corona : CardView
    lateinit var donation_give : CardView
    lateinit var newi : RequestApplyBottomNavFragment
    lateinit var text_apply : TextView
    var found = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_setting, container, false)

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")

        logout = view.findViewById(R.id.logout_btn)
        donation_give = view.findViewById(R.id.donation_give)
        text_apply = view.findViewById(R.id.apply_plasma)
        request = view.findViewById(R.id.request_plasma_btn)
        corona = view.findViewById(R.id.corona)

        donation_give.setOnClickListener(View.OnClickListener {
            setFragmentDonation_Give(Donation_Give_Fragment())
        })

        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var requestPlasma = pref.getString("Request","0")
        Log.i("Request : "," "+requestPlasma)

        if(requestPlasma.equals("1")) {
            text_apply.setText("Cancel Plasma Request")
        }


        request.setOnClickListener(View.OnClickListener {
            Log.i("Clicked"," ho rha hai")
            found = 0
                if (id != null) {
                    data.child(id).child("Profile").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists() && found == 0) {
                                newi = RequestApplyBottomNavFragment()
                                found = 1
                                newi.show(childFragmentManager, "bottom sheet")
                            }
                            else{
                                if(found == 0){
                                    found = 1
                                    Toast.makeText(activity,"Please add your account Details first",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }
        })


        logout.setOnClickListener(View.OnClickListener {
                var pref = PreferenceManager.getDefaultSharedPreferences(activity)
                pref.apply {
                    val editor = pref.edit()
                    editor.putString("Blood","0")
                    editor.putString("City","0")
                    editor.putString("State","0")
                    editor.putString("Request", "0")
                    editor.apply()
                    Log.i("request", "0")
                }
            val intent = Intent(getActivity(), MainActivity::class.java)
            mAuth.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })


        corona.setOnClickListener(View.OnClickListener {
           setFragmentCorona(CovidDetailFragment())
        })

        return view
    }




    private fun setFragmentDonation_Give(forgotFragment: Donation_Give_Fragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.commit()
        }
    }

    private fun setFragmentCorona(forgotFragment: CovidDetailFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.commit()
        }
    }


}