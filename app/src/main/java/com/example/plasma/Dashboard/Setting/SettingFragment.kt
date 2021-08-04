package com.example.plasma.Dashboard.Setting

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.plasma.Authentication.ForgotPassword
import com.example.plasma.MainActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SettingFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var logout : CardView
    lateinit var request : CardView
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
        text_apply = view.findViewById(R.id.apply_plasma)
        request = view.findViewById(R.id.request_plasma_btn)

        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var requestPlasma = pref.getString("Request","0")
        Log.i("Request : "," "+requestPlasma)

        if(requestPlasma.equals("1")) {
            text_apply.setText("Cancel Plasma Request")
        }


        request.setOnClickListener(View.OnClickListener {
                if (id != null) {
                    data.child(id).child("Profile").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                newi = RequestApplyBottomNavFragment()
                                found = 1
                                newi.show(childFragmentManager, "bottom sheet")
                            }
                            else{
                                if(found == 0){
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
                    editor.putString("Request", "0")
                    editor.apply()
                    Log.i("request", "0")
                }
            val intent = Intent(getActivity(), MainActivity::class.java)
            mAuth.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })

        return view
    }
}