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
import com.airbnb.lottie.LottieAnimationView
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView
import java.util.*

class ProfileCreationFragment : Fragment() {

    lateinit var name : EditText
    lateinit var state : EditText
    lateinit var city : EditText
    lateinit var dob : CardView
    lateinit var d : TextView
    lateinit var number : EditText
    lateinit var yes : CardView
    lateinit var no : CardView
    lateinit var sub : NeumorphButton
    lateinit var msg : TextView
    lateinit var male : CardView
    lateinit var female : CardView
    lateinit var Male_anim : LottieAnimationView
    lateinit var Female_anim : LottieAnimationView

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
            Blood = "AB"
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
        state = view.findViewById(R.id.state)
        city = view.findViewById(R.id.City)
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
                    d.text.toString().length == 0 || Blood.length == 0 || number.text.toString().length == 0
                    || status.length == 0 || sex.length == 0){
                Toast.makeText(activity,"Please fill all details",Toast.LENGTH_SHORT).show()
            }
            else if(number.text.toString().length != 10){
                    Toast.makeText(activity,"Wrong number format!",Toast.LENGTH_SHORT).show()
            }
            else{
                data.child("Profile").child("Name").setValue(name.text.trim().toString())
                data.child("Profile").child("State").setValue(state.text.trim().toString())
                data.child("Profile").child("City").setValue(city.text.trim().toString())
                data.child("Profile").child("DOB").setValue(d.text.toString())
                data.child("Profile").child("Blood_Grp").setValue(Blood)
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

}