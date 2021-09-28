package com.example.plasma

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plasma.Dashboard.Model.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class MapActivity : AppCompatActivity() , OnMapReadyCallback {

    lateinit var map : GoogleMap

    var earthRadius = 3958.75
    lateinit var data : DatabaseReference
    lateinit var mAuth: FirebaseAuth
    var id = "" as String
    var current_user = "" as String

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
        setContentView(R.layout.activity_map)

        mAuth = FirebaseAuth.getInstance()
        var user_id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")
        if (user_id != null) {
            data.child(user_id).child("Online").setValue("1")
        }

        id = intent?.getStringExtra("User_Id").toString()

        mAuth = FirebaseAuth.getInstance()
        current_user = mAuth.currentUser?.uid.toString()

        data = FirebaseDatabase.getInstance().getReference("Details")


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(p: GoogleMap?) {

        if (p != null) {
            map = p
        }

        var city = "";
        var lat =  0.0 as Double
        var log = 0.0 as Double
        var ll = 0.0 as Double
        var lg = 0.0 as Double

        var go = 0 as Int

        data.child(id).child("Profile").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ll = snapshot.child("Latitude").getValue() as Double
                    lg = snapshot.child("Longitute").getValue() as Double
                    city = snapshot.child("City").getValue() as String

                    var axis2: LatLng
                    axis2 = LatLng(ll, lg)
                    go++;

                    map.addMarker(MarkerOptions().position(axis2).title(city))
                    map.moveCamera(CameraUpdateFactory.newLatLng(axis2))

                } else {
                    Log.i("No location found", " error " + id)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        data.child(current_user).child("Profile").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("Latitude") && snapshot.hasChild("Longitute")) {
                        lat = snapshot.child("Latitude").getValue() as Double
                        log = snapshot.child("Longitute").getValue() as Double
                        var axis: LatLng
                        axis = LatLng(lat, log)
                        go++;
                        getDistance(lat,log,ll,lg)
                        map.addMarker(MarkerOptions().position(axis).title("My Location"))
                        map.moveCamera(CameraUpdateFactory.newLatLng(axis))

                        var originLocation : LatLng
                        var destinationLocation : LatLng

                        originLocation = LatLng(ll, lg)
                        destinationLocation = LatLng(lat, log)

                        //val urll = getDirectionURL(originLocation, destinationLocation, R.string.map_key.toString())
                        //GetDirection(urll).execute()
                    }
                } else
                    Log.i("No location found", " error")
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }

    private fun getDistance(lat: Double, log: Double, ll: Double, lg: Double) {
        val dLat = Math.toRadians(lat - ll)
        val dLng = Math.toRadians(log - lg)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(ll)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val dist = (earthRadius * c).toInt()
        Toast.makeText(this,"Distance to be covered : "+dist+" KM",Toast.LENGTH_SHORT).show()
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        @ExperimentalStdlibApi
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e: Exception){
                e.printStackTrace()
            }
            return result
        }


        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            map.addPolyline(lineoption)
        }
    }

    @ExperimentalStdlibApi
    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }
}