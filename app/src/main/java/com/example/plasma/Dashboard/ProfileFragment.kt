package com.example.plasma.Dashboard

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text

class ProfileFragment : Fragment() {

    lateinit var personal : CardView
    lateinit var covid : CardView
    lateinit var personal_frame : FrameLayout
    lateinit var covid_frame : FrameLayout
    lateinit var Boy : LottieAnimationView
    lateinit var Girl : LottieAnimationView
    lateinit var name : TextView
    lateinit var state : TextView
    lateinit var city : TextView
    lateinit var dob : TextView
    lateinit var blood : TextView
    lateinit var contact : TextView
    lateinit var progress : ProgressBar

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid as String
        data = FirebaseDatabase.getInstance().getReference("Details").child(id)

        progress = view.findViewById(R.id.profile_progressbar)
        Boy = view.findViewById(R.id.lottie_boy)
        Girl = view.findViewById(R.id.lottie_girl)
        name = view.findViewById(R.id.user_name)
        state = view.findViewById(R.id.state)
        city = view.findViewById(R.id.City)
        dob = view.findViewById(R.id.dob)
        blood = view.findViewById(R.id.blood_grp)
        contact = view.findViewById(R.id.number)

        personal_frame = view.findViewById(R.id.PersonaL_Frame)
        covid_frame = view.findViewById(R.id.CoronA_Frame)

        data.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    data.child("Profile").addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var sex = snapshot.child("Sex").getValue() as String
                            name.setText(snapshot.child("Name").getValue() as String)
                            state.setText(snapshot.child("State").getValue() as String)
                            city.setText(snapshot.child("City").getValue() as String)
                            dob.setText(snapshot.child("DOB").getValue() as String)
                            blood.setText(snapshot.child("Blood_Grp").getValue() as String)
                            contact.setText(snapshot.child("Number").getValue() as String)
                            progress.visibility = View.INVISIBLE
                            if(sex.equals("male")){
                                Boy.visibility = View.VISIBLE
                                Girl.visibility = View.INVISIBLE
                            }
                            else{
                                Boy.visibility = View.INVISIBLE
                                Girl.visibility = View.VISIBLE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                else{
                    Toast.makeText(activity,"Profile not Added",Toast.LENGTH_SHORT).show()
                    progress.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        covid = view.findViewById(R.id.covid_details)
        personal = view.findViewById(R.id.personal_details)

        covid.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_go)
            personal_frame.startAnimation(animation)
            personal_frame.visibility = View.INVISIBLE
            val animation2 = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_come)
            covid_frame.startAnimation(animation2)
            covid_frame.visibility = View.VISIBLE
        })

        personal.setOnClickListener(View.OnClickListener {
            val animation = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_come)
            personal_frame.startAnimation(animation)
            personal_frame.visibility = View.VISIBLE
            val animation2 = AnimationUtils.loadAnimation(activity, R.anim.frag_transaction_go)
            covid_frame.startAnimation(animation2)
            covid_frame.visibility = View.INVISIBLE
        })

        return view
    }

}