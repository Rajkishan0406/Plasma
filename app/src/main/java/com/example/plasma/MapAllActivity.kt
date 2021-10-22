package com.example.plasma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MapAllActivity: AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    lateinit var map : GoogleMap

    var earthRadius = 3958.75
    var user_id = "" as String
    lateinit var data : DatabaseReference
    lateinit var mAuth: FirebaseAuth
    var id = "" as String
    var current_user = "" as String
    lateinit var mar : Marker

    //Offline status for chat...
    override fun onPause() {
        super.onPause()
        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("0")
        }
    }

    //Online status for chat...
    override fun onRestart() {
        super.onRestart()
        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (id != null) {
            data.child(id).child("Online").setValue("1")
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_all)

        mAuth = FirebaseAuth.getInstance()
        user_id = mAuth.currentUser?.uid.toString()
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (user_id != null) {
            data.child(user_id).child("Online").setValue("1")
        }


        mAuth = FirebaseAuth.getInstance()
        current_user = mAuth.currentUser?.uid.toString()

        data = FirebaseDatabase.getInstance().getReference("Details")


        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapall) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }

    override fun onMapReady(p: GoogleMap?) {

        if (p != null) {
            map = p
        }

        var city = "" as String
        var blood = "" as String
        var ll = 0.0 as Double
        var lg = 0.0 as Double

        data.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    var id = snap.key as String
                    Log.i("Id : "," "+id)
                    if(snapshot.child(id).hasChild("PlasmaRequest")){
                        var k = snapshot.child(id).child("PlasmaRequest").getValue() as String
                        Log.i(" Request : "," "+k)
                        if(k.equals("1")){


                            ll = snapshot.child(id).child("Profile").child("Latitude").getValue() as Double
                            lg = snapshot.child(id).child("Profile").child("Longitute").getValue() as Double
                            city = snapshot.child(id).child("Profile").child("City").getValue() as String
                            blood = snapshot.child(id).child("Profile").child("Blood_Grp").getValue() as String


                            var axis2: LatLng
                            axis2 = LatLng(ll, lg)

                            if(user_id.equals(id)) {
                                mar = map.addMarker(MarkerOptions().position(axis2).title("My Location"))
                                mar.tag = id
                            }
                            else {
                                 mar = map.addMarker(MarkerOptions().position(axis2).title(city + " " + blood))
                                 mar.tag = id
                                }
                            map.moveCamera(CameraUpdateFactory.newLatLng(axis2))


                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        map.setOnMarkerClickListener(this)

    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val clickCount = marker?.tag as String


        var intent = Intent(this,ProfileActivity::class.java)
        intent.putExtra("Id",clickCount)
        startActivity(intent)

        return false
    }
}


