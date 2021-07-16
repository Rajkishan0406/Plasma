package com.example.plasma.Authentication

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import soup.neumorphism.NeumorphButton
import java.util.*

class ProfileCreationFragment : Fragment() {

    lateinit var name : EditText
    lateinit var state : EditText
    lateinit var city : EditText
    lateinit var blood : EditText
    lateinit var dob : CardView
    lateinit var d : TextView
    lateinit var number : EditText
    lateinit var yes : CardView
    lateinit var no : CardView
    lateinit var sub : NeumorphButton
    lateinit var msg : TextView
    lateinit var male : CardView
    lateinit var female : CardView

    var sex = "" as String
    var hashset = HashSet<String>(8)
    var status  = "" as String
    var y = ""
    var m = ""
    var D = ""

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile_creation, container, false)

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = id?.let { FirebaseDatabase.getInstance().getReference("Details").child(it) }!!

        //hashset blood group addition
        hashset.add("A+")
        hashset.add("O+")
        hashset.add("B+")
        hashset.add("AB+")
        hashset.add("A-")
        hashset.add("O-")
        hashset.add("B-")
        hashset.add("AB-")

        name = view.findViewById(R.id.user_name)
        state = view.findViewById(R.id.state)
        city = view.findViewById(R.id.City)
        d = view.findViewById(R.id.DOB)
        dob = view.findViewById(R.id.calendar)
        blood = view.findViewById(R.id.blood_group)
        number  = view.findViewById(R.id.number)
        yes = view.findViewById(R.id.Yes_Number)
        no = view.findViewById(R.id.No_Number)
        male = view.findViewById(R.id.Male)
        female = view.findViewById(R.id.Female)
        sub = view.findViewById(R.id.submit)
        msg = view.findViewById(R.id.text_for_number)

        male.setOnClickListener(View.OnClickListener {
            sex = "male"
            male.setCardBackgroundColor(Color.LTGRAY)
            female.setCardBackgroundColor(Color.WHITE)
        })

        female.setOnClickListener(View.OnClickListener {
            sex = "female"
            female.setCardBackgroundColor(Color.LTGRAY)
            male.setCardBackgroundColor(Color.WHITE)
        })

        yes.setOnClickListener(View.OnClickListener {
            status = "Yes"
            msg.setText("Do You Want Your Number To Display In Our App, For Direct Conatct? YES")
            yes.setCardBackgroundColor(Color.LTGRAY)
            no.setCardBackgroundColor(Color.WHITE)
        })

        no.setOnClickListener(View.OnClickListener {
            status = "No"
            msg.setText("Do You Want Your Number To Display In Our App, For Direct Conatct? NO")
            no.setCardBackgroundColor(Color.LTGRAY)
            yes.setCardBackgroundColor(Color.WHITE)
        })

        sub.setOnClickListener(View.OnClickListener {
            if(name.text.toString().length == 0 || state.text.toString().length == 0 || city.text.toString().length == 0 ||
                    d.text.toString().length == 0 || blood.text.toString().length == 0 || number.text.toString().length == 0
                    || status.length == 0 || sex.length == 0){
                Toast.makeText(activity,"Please fill all details",Toast.LENGTH_SHORT).show()
            }
            else if(number.text.toString().length != 10){
                    Toast.makeText(activity,"Wrong number format!",Toast.LENGTH_SHORT).show()
            }
            else if(!hashset.contains(blood.text.toString())){
                Toast.makeText(activity,"Not a valid blood group",Toast.LENGTH_SHORT).show()
            }
            else{
                data.child("Profile").child("Name").setValue(name.text.trim().toString())
                data.child("Profile").child("State").setValue(state.text.trim().toString())
                data.child("Profile").child("City").setValue(city.text.trim().toString())
                data.child("Profile").child("DOB").setValue(d.text.toString())
                data.child("Profile").child("Blood_Grp").setValue(blood.text.trim().toString())
                data.child("Profile").child("Number").setValue(number.text.trim().toString())
                data.child("Profile").child("Status").setValue(status)
                data.child("Profile").child("Sex").setValue(sex)
                Toast.makeText(activity,"Personal Details Filled Successfully",Toast.LENGTH_SHORT).show()
                setFragmentCovid(CovidDetailsFragment())
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

    private fun setFragmentCovid(forgotFragment: CovidDetailsFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_auth_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }

}