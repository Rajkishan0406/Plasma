package com.example.plasma.Dashboard.Setting

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.plasma.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class RequestApplyBottomNavFragment : BottomSheetDialogFragment() {

    lateinit var progress : ProgressBar
    lateinit var frame1 : FrameLayout
    lateinit var frame2 : FrameLayout
    lateinit var frame3 : FrameLayout
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var cancel : NeumorphButton
    lateinit var request : NeumorphButton
    lateinit var card : CardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_request_apply_bottom_nav, container, false)

        mAuth = FirebaseAuth.getInstance()
        data = FirebaseDatabase.getInstance().getReference("Details")
        var id = mAuth.currentUser?.uid

        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var requestPlasma = pref.getString("Request","0")

        progress = view.findViewById(R.id.Request_Plasma_ProgressBar)
        frame1 = view.findViewById(R.id.cancel_request)
        cancel = view.findViewById(R.id.cancel_request_button)
        frame2 = view.findViewById(R.id.Request_Plasma)
        request = view.findViewById(R.id.plasma_request_button)
        frame3 = view.findViewById(R.id.frame3)

        if(requestPlasma.equals("1")){
            progress.visibility = View.INVISIBLE
            frame1.visibility = View.VISIBLE
        }

        cancel.setOnClickListener(View.OnClickListener {
            if (id != null) {
                data.child(id).child("PlasmaRequest").setValue("0")
                Toast.makeText(activity,"Your Plasma Request has been canceled", Toast.LENGTH_SHORT).show()
                var pref = PreferenceManager.getDefaultSharedPreferences(activity)
                pref.apply {
                    val editor = pref.edit()
                    editor.putString("Request", "0")
                    editor.apply()
                }
                dismiss()
            }
        })

        if (id != null && requestPlasma.equals("0")) {
            data.child(id).child("Covid").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        progress.visibility = View.INVISIBLE
                        frame2.visibility = View.VISIBLE
                    }
                    else{
                        progress.visibility = View.INVISIBLE
                        frame3.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        request.setOnClickListener(View.OnClickListener {
            if (id != null) {
                data.child(id).child("PlasmaRequest").setValue("1")
                Toast.makeText(activity, "You have requested for Plasma", Toast.LENGTH_SHORT).show()
                var pref = PreferenceManager.getDefaultSharedPreferences(activity)
                pref.apply {
                    val editor = pref.edit()
                    editor.putString("Request", "1")
                    editor.apply()
                }
            }
            dismiss()
        })

        return view
    }


}