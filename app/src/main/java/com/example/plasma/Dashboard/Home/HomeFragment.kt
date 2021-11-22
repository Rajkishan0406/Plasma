package com.example.plasma.Dashboard.Home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.plasma.Dashboard.Adapter.PlasmaRequestAdapter
import com.example.plasma.Dashboard.Model.PlasmaRequestModel
import com.example.plasma.MapActivity
import com.example.plasma.MapAllActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    lateinit var recyclerview : RecyclerView
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var progress : ProgressBar
    lateinit var Refresh : SwipeRefreshLayout
    lateinit var HLayout : RelativeLayout
    lateinit var plasmaArrayList : ArrayList<PlasmaRequestModel>
    lateinit var request_size : TextView
    lateinit var no_data_animation : LottieAnimationView
    var size = 0 as Int
    lateinit var lottie : LottieAnimationView
    lateinit var map : CardView
    lateinit var frag : FilterFragment
    lateinit var filter : TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        requireActivity().window.statusBarColor = Color.WHITE


        var pref = PreferenceManager.getDefaultSharedPreferences(activity)

        mAuth = FirebaseAuth.getInstance()
        no_data_animation = view.findViewById(R.id.nodata_animation)
        progress = view.findViewById(R.id.progress_load)
        Refresh = view.findViewById(R.id.refresh)
        request_size = view.findViewById(R.id.total_count)
        map = view.findViewById(R.id.mapAll)
        data = FirebaseDatabase.getInstance().getReference("Details")

        recyclerview = view.findViewById(R.id.recyclerview_plasma_request)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(activity)

        plasmaArrayList = arrayListOf<PlasmaRequestModel>()

        filter = view.findViewById(R.id.filter)

        frag = FilterFragment()
        filter.setOnClickListener(View.OnClickListener {
            frag.show(childFragmentManager,"bottom sheet")
        })

        data.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                plasmaArrayList.clear()
                var CITY = pref.getString("City", "0") as String
                var STATE = pref.getString("State", "0") as String
                var BLOOD = pref.getString("Blood", "0") as String
                if (snapshot.exists()) {
                    for (plasmarequestSnapshot in snapshot.children) {
                        if (plasmarequestSnapshot.hasChild("PlasmaRequest")) {
                            var ss = plasmarequestSnapshot.child("PlasmaRequest").getValue() as String
                            if (ss.equals("1")) {
                                var name = plasmarequestSnapshot.child("Profile").child("Name").getValue() as String
                                var city = plasmarequestSnapshot.child("Profile").child("City").getValue() as String
                                var state = plasmarequestSnapshot.child("Profile").child("State").getValue() as String
                                var blood = plasmarequestSnapshot.child("Profile").child("Blood_Grp").getValue() as String
                                var id = plasmarequestSnapshot.child("Profile").child("Id").getValue() as String
                                if (CITY.toString().equals("0") && STATE.toString().equals("0") && BLOOD.toString().equals("0")){
                                    plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    }
                                else {
                                    if (CITY.toString().equals(city) && STATE.toString().equals("0") && BLOOD.toString().equals("0"))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    if (STATE.toString().equals(state) && CITY.toString().equals("0") && BLOOD.toString().equals("0"))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    if (BLOOD.toString().equals(blood) && CITY.toString().equals("0") && STATE.toString().equals("0"))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    if (BLOOD.toString().equals(blood) && CITY.toString().equals(city) && STATE.toString().equals("0"))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    if (BLOOD.toString().equals(blood) && CITY.toString().equals("0") && STATE.toString().equals(state))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    if (BLOOD.toString().equals("0") && CITY.toString().equals(city) && STATE.toString().equals(state))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    if (BLOOD.toString().equals(blood) && CITY.toString().equals(city) && STATE.toString().equals(state))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                }
                            }
                        }
                    }
                    var x = plasmaArrayList.size as Int
                    if(x == 0) {
                        no_data_animation.visibility = View.VISIBLE
                        recyclerview.visibility = View.INVISIBLE
                    }
                    val adapter = PlasmaRequestAdapter(plasmaArrayList)
                    recyclerview.adapter = adapter
                    progress.visibility = View.INVISIBLE
                    request_size.setText("Total Request : " + x)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })


        Refresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            data.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    plasmaArrayList.clear()
                    var CITY = pref.getString("City", "0") as String
                    var STATE = pref.getString("State", "0") as String
                    var BLOOD = pref.getString("Blood", "0") as String
                    if (snapshot.exists()) {
                        for (plasmarequestSnapshot in snapshot.children) {
                            if (plasmarequestSnapshot.hasChild("PlasmaRequest")) {
                                var ss = plasmarequestSnapshot.child("PlasmaRequest").getValue() as String
                                if (ss.equals("1")) {
                                    var name = plasmarequestSnapshot.child("Profile").child("Name").getValue() as String
                                    var city = plasmarequestSnapshot.child("Profile").child("City").getValue() as String
                                    var state = plasmarequestSnapshot.child("Profile").child("State").getValue() as String
                                    var blood = plasmarequestSnapshot.child("Profile").child("Blood_Grp").getValue() as String
                                    var id = plasmarequestSnapshot.child("Profile").child("Id").getValue() as String
                                    if (CITY.toString().equals("0") && STATE.toString().equals("0") && BLOOD.toString().equals("0"))
                                        plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    else {
                                        if (CITY.toString().equals(city) && STATE.toString().equals("0") && BLOOD.toString().equals("0"))
                                            plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                        if (STATE.toString().equals(state) && CITY.toString().equals("0") && BLOOD.toString().equals("0"))
                                            plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                        if (BLOOD.toString().equals(blood) && CITY.toString().equals("0") && STATE.toString().equals("0"))
                                            plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                        if (BLOOD.toString().equals(blood) && CITY.toString().equals(city) && STATE.toString().equals("0"))
                                            plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                        if (BLOOD.toString().equals(blood) && CITY.toString().equals("0") && STATE.toString().equals(state))
                                            plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                        if (BLOOD.toString().equals("0") && CITY.toString().equals(city) && STATE.toString().equals(state))
                                            plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                        if (BLOOD.toString().equals(blood) && CITY.toString().equals(city) && STATE.toString().equals(state))
                                            plasmaArrayList.add(PlasmaRequestModel(name, city, state, blood, id))
                                    }
                                }
                            }
                        }
                        val adapter = PlasmaRequestAdapter(plasmaArrayList)
                        recyclerview.adapter = adapter
                        progress.visibility = View.INVISIBLE
                        var x = plasmaArrayList.size as Int
                        request_size.setText("Total Request : " + x)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
            Refresh.isRefreshing = false
        })


        //Map call
        map.setOnClickListener(View.OnClickListener {
            val intent = Intent(getActivity(), MapAllActivity::class.java)
            getActivity()?.startActivity(intent)
        })

        return view
    }
}