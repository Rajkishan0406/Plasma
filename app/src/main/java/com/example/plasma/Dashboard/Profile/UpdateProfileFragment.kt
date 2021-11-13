package com.example.plasma.Dashboard.Profile

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieAnimationView
import com.example.plasma.R
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView
import java.util.*

class UpdateProfileFragment : Fragment() {

    lateinit var name : EditText
    lateinit var location  :TextView
    lateinit var dob : CardView
    lateinit var map : CardView
    lateinit var d : TextView
    lateinit var number : EditText
    lateinit var yes : NeumorphCardView
    lateinit var no : NeumorphCardView
    lateinit var sub : NeumorphButton
    lateinit var msg : TextView
    lateinit var male : CardView
    lateinit var female : CardView
    lateinit var Male_anim : LottieAnimationView
    lateinit var Female_anim : LottieAnimationView
    var plasmaRequest = 0 as Int

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
    var bundle =  Bundle()
    var loc_city = "" as String
    var loc_state = "" as String
    var change = 0 as Int

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_update_profile, container, false)

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = id?.let { FirebaseDatabase.getInstance().getReference("Details").child(it) }!!

        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!

        //blood group addition
        a_pos = view.findViewById(R.id.A_positive_update)
        ab_pos = view.findViewById(R.id.AB_positive_update)
        b_pos = view.findViewById(R.id.B_positive_update)
        o_pos = view.findViewById(R.id.O_positive_update)
        a_neg = view.findViewById(R.id.A_negative_update)
        ab_neg = view.findViewById(R.id.AB_negative_update)
        b_neg = view.findViewById(R.id.B_negative_update)
        o_neg = view.findViewById(R.id.O_negative_update)

        A_pos = view.findViewById(R.id.A_update)
        AB_pos = view.findViewById(R.id.AB_update)
        B_pos = view.findViewById(R.id.B_update)
        O_pos = view.findViewById(R.id.O_update)
        A_neg = view.findViewById(R.id.A_n_update)
        AB_neg = view.findViewById(R.id.AB_n_update)
        B_neg = view.findViewById(R.id.B_n_update)
        O_neg = view.findViewById(R.id.O_n_update)

        name = view.findViewById(R.id.user_name_update)
        d = view.findViewById(R.id.DOB_update)
        dob = view.findViewById(R.id.calendar_update)
        number  = view.findViewById(R.id.number_update)
        Male_anim = view.findViewById(R.id.lottie_boy_update)
        Female_anim = view.findViewById(R.id.lottie_girl_update)
        yes = view.findViewById(R.id.Yes_Number_update)
        no = view.findViewById(R.id.No_Number_update)
        male = view.findViewById(R.id.Male_update)
        female = view.findViewById(R.id.Female_update)
        sub = view.findViewById(R.id.submit_update)
        location = view.findViewById(R.id.location)
        map = view.findViewById(R.id.map)
        msg = view.findViewById(R.id.text_for_number_update)

        bundle = this.requireArguments()
        name.setText(bundle.getString("name"))
        location.setText(bundle.getString("city")+", "+bundle.getString("state"))
        d.setText(bundle.getString("dob"))
        number.setText(bundle.getString("contact"))
        if(bundle.getString("name").toString().length > 0)
            plasmaRequest = 1
        var s = bundle.getString("sex")

        if(s.equals("male")){
            sex = "male"
            Male_anim.visibility = View.VISIBLE
            Female_anim.visibility = View.INVISIBLE
        }
        if(s.equals("female")){
            sex = "female"
            Male_anim.visibility = View.INVISIBLE
            Female_anim.visibility = View.VISIBLE
        }

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




        sub.setOnClickListener(View.OnClickListener {
            if(name.text.toString().length == 0 || loc_city.length == 0 || loc_state.length == 0 ||
                    d.text.toString().length == 0 || Blood.length == 0 || number.text.toString().length == 0
                    || status.length == 0 || sex.length == 0){
                Toast.makeText(activity,"Please fill all details", Toast.LENGTH_SHORT).show()
                Toast.makeText(activity," "+loc_city+"  "+loc_state,Toast.LENGTH_SHORT).show()
            }
            else if(number.text.toString().length != 10){
                Toast.makeText(activity,"Wrong number format!", Toast.LENGTH_SHORT).show()
            }
            else{
                if(plasmaRequest == 0)
                    data.child("PlasmaRequest").setValue("0")
                data.child("Profile").child("Name").setValue(name.text.trim().toString())
                data.child("Profile").child("DOB").setValue(d.text.toString())
                data.child("Profile").child("Blood_Grp").setValue(Blood)
                data.child("Profile").child("Number").setValue(number.text.trim().toString())
                data.child("Profile").child("Status").setValue(status)
                data.child("Profile").child("Sex").setValue(sex)
                Log.i("change : ",""+change)
                if(change == 1) {
                    data.child("Profile").child("State").setValue(loc_state)
                    data.child("Profile").child("City").setValue(loc_city)
                    data.child("Profile").child("Longitute").setValue(longitude)
                    data.child("Profile").child("Latitude").setValue(latitude)
                }
                data.child("Profile").child("Id").setValue(id)
                Toast.makeText(activity,"Personal Details Updated Successfully", Toast.LENGTH_SHORT).show()
                setFragmentProfile(ProfileFragment())
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

        map.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity,"Make sure your GPS location is ON!",Toast.LENGTH_SHORT).show()
            fetchLocation()
        })

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
                change = 1
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
                change = 1
            }
            else
                Toast.makeText(activity,"Please make sure your gps location is ON!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun getCityName(lat : Double , long : Double) : String {

        var cityName = "" as String
        if(activity != null) {
            var geoCoder = Geocoder(activity, Locale.getDefault())
            var Address = geoCoder?.getFromLocation(lat, long, 1)

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

    private fun setFragmentProfile(forgotFragment: ProfileFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.commit()
        }
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

}