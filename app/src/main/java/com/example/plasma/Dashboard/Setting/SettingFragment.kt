package com.example.plasma.Dashboard.Setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Authentication.ForgotPassword
import com.example.plasma.Dashboard.Profile.ReportFragment
import com.example.plasma.MainActivity
import com.example.plasma.MotionUIFragment
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
    lateinit var SLayout : FrameLayout
    lateinit var response_number_card : CardView
    lateinit var response_number : TextView
    lateinit var donation_give : CardView
    lateinit var donation_want : CardView
    lateinit var Aboutplasma : CardView
    lateinit var developer : CardView
    lateinit var newi : RequestApplyBottomNavFragment
    lateinit var text_apply : TextView
    var found = 0

    override fun onResume() {
        super.onResume()
        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var requestPlasma = pref.getString("Request","0")

        if(requestPlasma.equals("1")) {
            text_apply.setText("Cancel Plasma Request")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_setting, container, false)

        requireActivity().window.statusBarColor = Color.WHITE

        SLayout = view.findViewById(R.id.SettingFragment)

        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        SLayout.startAnimation(animationx)

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")

        logout = view.findViewById(R.id.logout_btn)
        donation_give = view.findViewById(R.id.donation_give)
        donation_want = view.findViewById(R.id.donation_want)
        text_apply = view.findViewById(R.id.apply_plasma)
        request = view.findViewById(R.id.request_plasma_btn)
        response_number_card = view.findViewById(R.id.response_number_card)
        Aboutplasma = view.findViewById(R.id.about_plasma_donation)
        developer = view.findViewById(R.id.About_Dev)
        response_number = view.findViewById(R.id.response_number)

        var checker = 0 as Int

        if (id != null) {
            data.child(id).child("PlasmaRequest").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        var ss = snapshot.getValue() as String
                        if(ss.equals("0"))
                            checker = 1;
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        if (id != null) {
            data.child(id).child("Donation_Want").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var count = 0 as Int
                        for(donationSnapshot in snapshot.children) {
                            var d = donationSnapshot.getValue() as String
                            Log.i("id : ",""+d)
                            count++;
                        }
                        if(checker == 0) {
                            response_number_card.visibility = View.VISIBLE
                            response_number.setText(count.toString())
                        }
                        else{
                            response_number_card.visibility = View.INVISIBLE
                        }
                    } else {
                        response_number_card.visibility = View.INVISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        donation_give.setOnClickListener(View.OnClickListener {
            setFragmentDonation_Give(Donation_Give_Fragment())
        })

        donation_want.setOnClickListener(View.OnClickListener {
            setFragmentDonation_Want(Donation_Want_Fragment())
        })

        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var requestPlasma = pref.getString("Request","0")

        if(requestPlasma.equals("1")) {
            text_apply.setText("Cancel Plasma Request")
        }


        request.setOnClickListener(View.OnClickListener {
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
            mAuth = FirebaseAuth.getInstance()
            var id = mAuth.currentUser?.uid
            data = FirebaseDatabase.getInstance().getReference("Details")
            if (id != null) {
                data.child(id).child("Online").setValue("0")
            }
            val intent = Intent(getActivity(), MainActivity::class.java)
            mAuth.signOut()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })


        Aboutplasma.setOnClickListener(View.OnClickListener {
            setFragmentAboutPlasma(AboutPlasmaFragment())
        })

        developer.setOnClickListener(View.OnClickListener {
            setFragmentDeveloper(developerFragment())
        })

        return view
    }

    private fun setFragmentDeveloper(forgotFragment: developerFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }

    private fun setFragmentAboutPlasma(forgotFragment: AboutPlasmaFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }


    private fun setFragmentDonation_Give(forgotFragment: Donation_Give_Fragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }

    private fun setFragmentDonation_Want(forgotFragment: Donation_Want_Fragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }



}