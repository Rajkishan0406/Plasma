package com.example.plasma.Dashboard.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView
import java.text.SimpleDateFormat
import java.util.*

class RulesAndRegulationFragment : Fragment() {

    lateinit var btn : NeumorphButton
    lateinit var btn2 : NeumorphButton
    lateinit var frame : RelativeLayout

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    var unique = "" as String
    var number2 = "" as String
    var disease = "-1"
    var cancer = "-1"
    var Weight = "-1"
    var pregnent = "-1"
    lateinit var male : FrameLayout
    lateinit var female : FrameLayout
    // male frame
    lateinit var disease_yes : NeumorphCardView
    lateinit var disease_no : NeumorphCardView
    lateinit var cancer_yes : NeumorphCardView
    lateinit var cancer_no : NeumorphCardView
    lateinit var weight_male : EditText
    //female frame
    lateinit var female_disease_yes : NeumorphCardView
    lateinit var female_disease_no : NeumorphCardView
    lateinit var female_cancer_yes : NeumorphCardView
    lateinit var female_cancer_no : NeumorphCardView
    lateinit var female_pregnent_yes : NeumorphCardView
    lateinit var female_pregnent_no : NeumorphCardView
    lateinit var weight_female : EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_rules_and_regulation, container, false)

        male = view.findViewById(R.id.male_frame)
        female = view.findViewById(R.id.female_frame)

        frame = view.findViewById(R.id.RulesFragment)
        val animationx = AnimationUtils.loadAnimation(activity, R.anim.fragment_trans)
        frame.startAnimation(animationx)


        var bun = Bundle()
        bun = this.requireArguments()
        var id : String? = bun.getString("User_Id") as String
        unique = bun.getString("Number") as String
        var gen = bun.getInt("Gender") as Int

        Log.i("Gender : "," "+gen)

        if(gen == 1){
            Log.i("Gender : ","Male frame ON ")
            male.visibility = View.VISIBLE
            female.visibility = View.INVISIBLE
        }
        if(gen == -1){
            Log.i("Gender : ","Female frame ON ")
            male.visibility = View.INVISIBLE
            female.visibility = View.VISIBLE
        }

        disease_yes = view.findViewById(R.id.disease_text_Yes)
        disease_no = view.findViewById(R.id.disease_text_No)
        cancer_yes = view.findViewById(R.id.cancer_text_Yes)
        cancer_no = view.findViewById(R.id.cancer_text_No)
        weight_male = view.findViewById(R.id.weight_text_male)
        female_disease_yes = view.findViewById(R.id.female_disease_text_Yes)
        female_disease_no = view.findViewById(R.id.female_disease_text_No)
        female_cancer_yes = view.findViewById(R.id.female_cancer_text_Yes)
        female_cancer_no = view.findViewById(R.id.female_cancer_text_No)
        female_pregnent_yes = view.findViewById(R.id.female_Pregnant_text_Yes)
        female_pregnent_no = view.findViewById(R.id.female_Pregnant_text_No)
        weight_female = view.findViewById(R.id.female_weight_text_female)

        disease_yes.setOnClickListener(View.OnClickListener {
            disease_yes.setShapeType(1)
            disease_no.setShapeType(0)
            disease = "1"
        })

        disease_no.setOnClickListener(View.OnClickListener {
            disease_yes.setShapeType(0)
            disease_no.setShapeType(1)
            disease = "0"
        })

        cancer_yes.setOnClickListener(View.OnClickListener {
            cancer_yes.setShapeType(1)
            cancer_no.setShapeType(0)
            cancer = "1"
        })

        cancer_no.setOnClickListener(View.OnClickListener {
            cancer_no.setShapeType(1)
            cancer_yes.setShapeType(0)
            cancer = "0"
        })

        female_disease_yes.setOnClickListener(View.OnClickListener {
            female_disease_yes.setShapeType(1)
            female_disease_no.setShapeType(0)
            disease = "1"
        })

        female_disease_no.setOnClickListener(View.OnClickListener {
            female_disease_yes.setShapeType(0)
            female_disease_no.setShapeType(1)
            disease = "0"
        })

        female_cancer_yes.setOnClickListener(View.OnClickListener {
            female_cancer_yes.setShapeType(1)
            female_cancer_no.setShapeType(0)
            cancer = "1"
        })

        female_cancer_no.setOnClickListener(View.OnClickListener {
            female_cancer_no.setShapeType(1)
            female_cancer_yes.setShapeType(0)
            cancer = "0"
        })

        female_pregnent_yes.setOnClickListener(View.OnClickListener {
            female_pregnent_yes.setShapeType(1)
            female_pregnent_no.setShapeType(0)
            pregnent = "1"
        })

        female_pregnent_no.setOnClickListener(View.OnClickListener {
            female_pregnent_no.setShapeType(1)
            female_pregnent_yes.setShapeType(0)
            pregnent = "0"
        })

        mAuth = FirebaseAuth.getInstance()
        var User_id = mAuth.currentUser?.uid

        data = FirebaseDatabase.getInstance().getReference("Details")

        if(User_id != null) {
            data.child(User_id).child("Profile").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        number2 = snapshot.child("Number").getValue() as String
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }


        btn = view.findViewById(R.id.donate_male)

        btn2 = view.findViewById(R.id.donate_female)

        btn.setOnClickListener(View.OnClickListener {
            if(gen.equals("male")){
                var w = 0 as Int
                if(weight_male.text.toString().length > 0)
                w = weight_male.text.toString().trim().toInt()
                if(cancer.equals("-1") || disease.equals("-1") || weight_male.text.toString().length == 0){
                    Toast.makeText(activity,"please fill the details correctly",Toast.LENGTH_SHORT).show()
                }
                else {
                    if (w < 60) {
                        Toast.makeText(activity, "your Weight is less then required weight for donation! You can't donate Plasma", Toast.LENGTH_SHORT).show()
                    } else if (cancer.equals("1")) {
                        Toast.makeText(activity, "You can't donate plasma because of cancer survivor", Toast.LENGTH_SHORT).show()
                    } else if (disease.equals("1")) {
                        Toast.makeText(activity, "You have some serious disease you can't donate Plasma", Toast.LENGTH_SHORT).show()
                    } else {
                        if (number2.length > 0) {
                            if (id != null) {
                                data.child(id).child("Donation_Want").child(number2).setValue(User_id)
                            }
                            if (User_id != null) {
                                data.child(User_id).child("Donation_Give").child(unique).setValue(id)
                            }
                            Toast.makeText(activity, "Your response is successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else{
                if(number2.length > 0) {
                    var w = 0 as Int
                    if(weight_female.text.toString().length > 0)
                    w = weight_female.text.toString().trim().toInt()
                    if (cancer.equals("-1") || disease.equals("-1") || weight_female.text.toString().length == 0 || pregnent.equals("-1")) {
                        Toast.makeText(activity, "please fill the details correctly", Toast.LENGTH_SHORT).show()
                    } else {
                        if (w < 60) {
                            Toast.makeText(activity, "your Weight is less then required weight for donation! You can't donate Plasma", Toast.LENGTH_SHORT).show()
                        } else if (cancer.equals("1")) {
                            Toast.makeText(activity, "You can't donate plasma because of cancer survivor", Toast.LENGTH_SHORT).show()
                        } else if (disease.equals("1")) {
                            Toast.makeText(activity, "You have some serious disease you can't donate Plasma", Toast.LENGTH_SHORT).show()
                        } else if (pregnent.equals("1")) {
                            Toast.makeText(activity, "You can't donate because of past pregnancy", Toast.LENGTH_SHORT).show()
                        } else {
                            if (id != null) {
                                data.child(id).child("Donation_Want").child(number2).setValue(User_id)
                            }
                            if (User_id != null) {
                                data.child(User_id).child("Donation_Give").child(unique).setValue(id)
                            }
                            Toast.makeText(activity, "Your response is successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })

        btn2.setOnClickListener(View.OnClickListener {
            if(gen.equals("male")){
                var w = 0 as Int
                if(weight_male.text.toString().length > 0)
                    w = weight_male.text.toString().trim().toInt()
                if(cancer.equals("-1") || disease.equals("-1") || weight_male.text.toString().length == 0){
                    Toast.makeText(activity,"please fill the details correctly",Toast.LENGTH_SHORT).show()
                }
                else {
                    if (w < 60) {
                        Toast.makeText(activity, "your Weight is less then required weight for donation! You can't donate Plasma", Toast.LENGTH_SHORT).show()
                    } else if (cancer.equals("1")) {
                        Toast.makeText(activity, "You can't donate plasma because of cancer survivor", Toast.LENGTH_SHORT).show()
                    } else if (disease.equals("1")) {
                        Toast.makeText(activity, "You have some serious disease you can't donate Plasma", Toast.LENGTH_SHORT).show()
                    } else {
                        if (number2.length > 0) {
                            if (id != null) {
                                data.child(id).child("Donation_Want").child(number2).setValue(User_id)
                            }
                            if (User_id != null) {
                                data.child(User_id).child("Donation_Give").child(unique).setValue(id)
                            }
                            Toast.makeText(activity, "Your response is successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else{
                if(number2.length > 0) {
                    var w = 0 as Int
                    if(weight_female.text.toString().length > 0)
                        w = weight_female.text.toString().trim().toInt()
                    if (cancer.equals("-1") || disease.equals("-1") || weight_female.text.toString().length == 0 || pregnent.equals("-1")) {
                        Toast.makeText(activity, "please fill the details correctly", Toast.LENGTH_SHORT).show()
                    } else {
                        if (w < 60) {
                            Toast.makeText(activity, "your Weight is less then required weight for donation! You can't donate Plasma", Toast.LENGTH_SHORT).show()
                        } else if (cancer.equals("1")) {
                            Toast.makeText(activity, "You can't donate plasma because of cancer survivor", Toast.LENGTH_SHORT).show()
                        } else if (disease.equals("1")) {
                            Toast.makeText(activity, "You have some serious disease you can't donate Plasma", Toast.LENGTH_SHORT).show()
                        } else if (pregnent.equals("1")) {
                            Toast.makeText(activity, "You can't donate because of past pregnancy", Toast.LENGTH_SHORT).show()
                        } else {
                            if (id != null) {
                                data.child(id).child("Donation_Want").child(number2).setValue(User_id)
                            }
                            if (User_id != null) {
                                data.child(User_id).child("Donation_Give").child(unique).setValue(id)
                            }
                            Toast.makeText(activity, "Your response is successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })

        return view;
    }

}