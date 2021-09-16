package com.example.plasma

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MapActivity : AppCompatActivity() , OnMapReadyCallback {

    lateinit var map : GoogleMap


    lateinit var data : DatabaseReference
    lateinit var mAuth: FirebaseAuth
    var id = "" as String
    var current_user = "" as String

    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        id = intent?.getStringExtra("User_Id").toString()

        mAuth = FirebaseAuth.getInstance()
        current_user = mAuth.currentUser?.uid.toString()

        data = FirebaseDatabase.getInstance().getReference("Details")


        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p: GoogleMap?) {

        if (p != null) {
            map = p
        }

        var city = "";
        var lat =  0.0 as Double
        var log = 0.0 as Double


        data.child(current_user).child("Profile").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var ll = snapshot.child("Latitude").getValue() as Double
                    var lg = snapshot.child("Longitute").getValue() as Double

                    var axis2 : LatLng
                    axis2 = LatLng(ll, lg)

                    map.addMarker(MarkerOptions().position(axis2).title("My Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLng(axis2))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })


        data.child(id).child("Profile").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //fetchLocation()
                    if(snapshot.hasChild("Latitude"))
                    lat = snapshot.child("Latitude").getValue() as Double
                    if(snapshot.hasChild("Longitute"))
                    log = snapshot.child("Longitute").getValue() as Double
                    if(snapshot.hasChild("City"))
                    city = snapshot.child("City").getValue() as String
                    Log.i(""+lat+"  "+log,"   "+city)
                    var axis : LatLng
                    axis = LatLng(lat, log)

                    map.addMarker(MarkerOptions().position(axis).title(city))
                    map.moveCamera(CameraUpdateFactory.newLatLng(axis))
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // request permission
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQ_CODE)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { Location ->
            if(Location != null) {
                latitude = Location.latitude
                longitude = Location.longitude
                var axis2 : LatLng
                axis2 = LatLng(latitude,longitude)
                Log.i("My Location "," "+latitude+"  "+longitude)
                if(latitude > 0) {
                    map.addMarker(MarkerOptions().position(axis2).title("My Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLng(axis2))
                }
            }
            else{
                getNewLocation()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed on getting current location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        if ( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&  ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
            return
        }
        fusedLocationClient!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
                //now create locationCallBack variable
        )
    }

    private var locationCallback = object  : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
            //Now we will set the new location
            if (lastLocation != null) {
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
                var axis2 : LatLng
                axis2 = LatLng(latitude,longitude)
                Log.i("My Location "," "+latitude+"  "+longitude)
                if(latitude > 0) {
                    map.addMarker(MarkerOptions().position(axis2).title("My Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLng(axis2))
                }
            }
        }
    }
}