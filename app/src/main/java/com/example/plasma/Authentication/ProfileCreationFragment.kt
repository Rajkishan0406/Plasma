package com.example.plasma.Authentication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieAnimationView
import com.example.plasma.DashboardActivity
import com.example.plasma.R
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView
import java.util.*
import java.util.jar.Manifest

class ProfileCreationFragment : Fragment() {

    lateinit var name : EditText
    lateinit var location  :TextView
    lateinit var dob : CardView
    lateinit var d : TextView
    lateinit var number : EditText
    lateinit var yes : NeumorphCardView
    lateinit var no : NeumorphCardView
    lateinit var sub : NeumorphButton
    lateinit var msg : TextView
    lateinit var male : CardView
    lateinit var female : CardView
    lateinit var map : CardView
    lateinit var Male_anim : LottieAnimationView
    lateinit var Female_anim : LottieAnimationView
    lateinit var frame : RelativeLayout

    //blood group neumorphCard
    lateinit var a_pos : NeumorphCardView
    lateinit var b_pos : NeumorphCardView
    lateinit var ab_pos : NeumorphCardView
    lateinit var o_pos : NeumorphCardView
    lateinit var a_neg : NeumorphCardView
    lateinit var b_neg : NeumorphCardView
    lateinit var o_neg : NeumorphCardView
    lateinit var ab_neg : NeumorphCardView

    lateinit var A_pos : TextView
    lateinit var B_pos : TextView
    lateinit var AB_pos : TextView
    lateinit var O_pos : TextView
    lateinit var A_neg : TextView
    lateinit var B_neg : TextView
    lateinit var O_neg : TextView
    lateinit var AB_neg : TextView

    var Blood = "" as String
    var sex = "" as String
    var status  = "" as String
    var y = ""
    var m = ""
    var D = ""
    var loc_city = "" as String
    var loc_state = "" as String

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile_creation, container, false)

        requireActivity().window.statusBarColor = Color.WHITE

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = id?.let { FirebaseDatabase.getInstance().getReference("Details").child(it) }!!

        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!

        frame = view.findViewById(R.id.ProfileCreationFragment)

        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        frame.startAnimation(animationx)

        //blood group addition
        a_pos = view.findViewById(R.id.A_positive)
        ab_pos = view.findViewById(R.id.AB_positive)
        b_pos = view.findViewById(R.id.B_positive)
        o_pos = view.findViewById(R.id.O_positive)
        a_neg = view.findViewById(R.id.A_negative)
        ab_neg = view.findViewById(R.id.AB_negative)
        b_neg = view.findViewById(R.id.B_negative)
        o_neg = view.findViewById(R.id.O_negative)

        A_pos = view.findViewById(R.id.A)
        AB_pos = view.findViewById(R.id.AB)
        B_pos = view.findViewById(R.id.B)
        O_pos = view.findViewById(R.id.O)
        A_neg = view.findViewById(R.id.A_n)
        AB_neg = view.findViewById(R.id.AB_n)
        B_neg = view.findViewById(R.id.B_n)
        O_neg = view.findViewById(R.id.O_n)

        a_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "A+"
            A_pos.setTextColor(Color.RED)
            a_pos.setShapeType(1)
        })

        b_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "B+"
            B_pos.setTextColor(Color.RED)
            b_pos.setShapeType(1)
        })

        ab_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "AB+"
            AB_pos.setTextColor(Color.RED)
            ab_pos.setShapeType(1)
        })

        o_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "O+"
            O_pos.setTextColor(Color.RED)
            o_pos.setShapeType(1)
        })

        a_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "A-"
            A_neg.setTextColor(Color.RED)
            a_neg.setShapeType(1)
        })

        b_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "B-"
            B_neg.setTextColor(Color.RED)
            b_neg.setShapeType(1)
        })

        ab_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "AB-"
            AB_neg.setTextColor(Color.RED)
            ab_neg.setShapeType(1)
        })

        o_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "O-"
            O_neg.setTextColor(Color.RED)
            o_neg.setShapeType(2)
        })


        name = view.findViewById(R.id.user_name)
        d = view.findViewById(R.id.DOB)
        dob = view.findViewById(R.id.calendar)
        number  = view.findViewById(R.id.number)
        Male_anim = view.findViewById(R.id.lottie_boy)
        Female_anim = view.findViewById(R.id.lottie_girl)
        yes = view.findViewById(R.id.Yes_Number)
        no = view.findViewById(R.id.No_Number)
        male = view.findViewById(R.id.Male)
        female = view.findViewById(R.id.Female)
        sub = view.findViewById(R.id.submit)
        map = view.findViewById(R.id.map)
        location = view.findViewById(R.id.location)
        msg = view.findViewById(R.id.text_for_number)

        male.setOnClickListener(View.OnClickListener {
            sex = "male"
            Male_anim.visibility = View.VISIBLE
            Female_anim.visibility = View.INVISIBLE
        })

        female.setOnClickListener(View.OnClickListener {
            sex = "female"
            Male_anim.visibility = View.INVISIBLE
            Female_anim.visibility = View.VISIBLE
        })

        yes.setOnClickListener(View.OnClickListener {
            status = "Yes"
            yes.setShapeType(1)
            no.setShapeType(0)
        })

        no.setOnClickListener(View.OnClickListener {
            status = "No"
            no.setShapeType(1)
            yes.setShapeType(0)
        })


        map.setOnClickListener(View.OnClickListener {

            fetchLocation()

            if(location.text.toString().equals("Location")){
                Toast.makeText(activity,"Make sure your location gps in ON!",Toast.LENGTH_SHORT).show()
            }

        })

        sub.setOnClickListener(View.OnClickListener {
            if(name.text.toString().length == 0 || loc_city.length == 0 || loc_state.length == 0 ||
                    d.text.toString().length == 0 || Blood.length == 0 || number.text.toString().length == 0
                    || status.length == 0 || sex.length == 0){
                Toast.makeText(activity,"Please fill all details",Toast.LENGTH_SHORT).show()
            }
            else if(number.text.toString().length != 10){
                    Toast.makeText(activity,"Wrong number format!",Toast.LENGTH_SHORT).show()
            }
            else{
                data.child("Profile").child("Name").setValue(name.text.trim().toString())
                data.child("Profile").child("State").setValue(loc_state)
                data.child("Profile").child("City").setValue(loc_city)
                data.child("Profile").child("DOB").setValue(d.text.toString())
                data.child("Profile").child("Blood_Grp").setValue(Blood)
                data.child("Profile").child("Number").setValue(number.text.trim().toString())
                data.child("Profile").child("Status").setValue(status)
                data.child("Profile").child("Sex").setValue(sex)
                data.child("Profile").child("Longitute").setValue(longitude)
                data.child("Profile").child("Latitude").setValue(latitude)
                data.child("PlasmaRequest").setValue("0")
                data.child("Profile").child("Id").setValue(id)
                Toast.makeText(activity,"Personal Details Filled Successfully",Toast.LENGTH_SHORT).show()
                var intent = Intent(activity, DashboardActivity::class.java)
                startActivity(intent)
            }
        })

        val c = Calendar.getInstance()
        val year  = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        dob.setOnClickListener(View.OnClickListener {
            val dpd = activity?.let { it1 ->
                DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
                    val kj = mmonth+1 as Int
                    y = myear.toString()
                    D = mday.toString()
                    m = kj.toString()
                    d.setText(""+mday+"/"+kj+"/"+myear)
                },year,month,day)
            }
            dpd?.show()
        }
        )

        return view
    }

    // Map Implementation
    private fun fetchLocation() {

        if (activity?.let {
                    ActivityCompat.checkSelfPermission(it,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                } != PackageManager.PERMISSION_GRANTED) {
            // request permission
            activity?.let {
                ActivityCompat.requestPermissions(it,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
            }
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { Location ->
            if(Location != null) {
                latitude = Location.latitude
                longitude = Location.longitude
                loc_city = getCityName(latitude,longitude)
                loc_state = getStateName(latitude,longitude)
                location.setText(loc_city+", "+loc_state)
            }
            else{
                getNewLocation()
            }
        }
                .addOnFailureListener {
                    Toast.makeText(activity, "Failed on getting current location", Toast.LENGTH_SHORT).show()
                }
    }
    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        if (activity?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED && activity?.let { ActivityCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity,"Permission Denied",Toast.LENGTH_SHORT).show()
            return
        }
        fusedLocationClient!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
                //now create locationCallBack variable
        )
    }
    private var locationCallback = object  : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
            //Now we will set the new location
            if(lastLocation != null) {
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
                loc_city = getCityName(latitude,longitude)
                loc_state = getStateName(latitude,longitude)
                location.setText(loc_city+", "+loc_state)
                }
            else
                Toast.makeText(activity,"Please make sure your gps location is ON!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun getCityName(lat : Double , long : Double) : String {

        var cityName = "" as String
        if(activity != null) {
            var geoCoder = Geocoder(activity, Locale.getDefault())
            var Address = geoCoder.getFromLocation(lat, long, 1)

            cityName = Address.get(0).locality
        }

        return cityName
    }
    private fun getStateName(lat : Double , long : Double) : String {

        var stateName = "" as String
        if(activity != null) {
            var geoCoder = Geocoder(activity, Locale.getDefault())
            var Address = geoCoder.getFromLocation(lat, long, 1)

            stateName = Address.get(0).adminArea
        }

        return stateName
    }

    private fun allFlat() {
        a_pos.setShapeType(0)
        ab_pos.setShapeType(0)
        b_pos.setShapeType(0)
        o_pos.setShapeType(0)
        ab_neg.setShapeType(0)
        o_neg.setShapeType(0)
        b_neg.setShapeType(0)
        a_neg.setShapeType(0)
    }

    private fun allWhite() {
        A_pos.setTextColor(Color.BLACK)
        B_pos.setTextColor(Color.BLACK)
        O_pos.setTextColor(Color.BLACK)
        AB_pos.setTextColor(Color.BLACK)
        A_neg.setTextColor(Color.BLACK)
        AB_neg.setTextColor(Color.BLACK)
        O_neg.setTextColor(Color.BLACK)
        B_neg.setTextColor(Color.BLACK)
    }

    private fun setFragmentCovid(forgotFragment: CovidDetailsFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_auth_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }


    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(activity, "You need to grant permission to access location",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}