package com.example.plasma.Dashboard

import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieAnimationView
import com.example.plasma.Authentication.ProfileCreationFragment
import com.example.plasma.Authentication.SignUpFragment
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class ProfileFragment : Fragment() {

    lateinit var personal : CardView
    lateinit var update_personal : CardView
    lateinit var covid : CardView
    lateinit var personal_frame : FrameLayout
    lateinit var covid_frame : FrameLayout
    lateinit var Boy : LottieAnimationView
    lateinit var Girl : LottieAnimationView
    lateinit var name : TextView
    lateinit var state : TextView
    lateinit var pd : TextView
    lateinit var city : TextView
    lateinit var dob : TextView
    lateinit var blood : TextView
    lateinit var contact : TextView
    lateinit var report_card : NeumorphCardView
    lateinit var progress : ProgressBar

    lateinit var disease : TextView
    lateinit var vc : TextView
    lateinit var report : TextView
    var progress_status = 0 as Int

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
        update_personal = view.findViewById(R.id.edit_personal_details)
        Boy = view.findViewById(R.id.lottie_boy)
        Girl = view.findViewById(R.id.lottie_girl)
        name = view.findViewById(R.id.user_name)
        state = view.findViewById(R.id.state)
        city = view.findViewById(R.id.City)
        dob = view.findViewById(R.id.dob)
        pd = view.findViewById(R.id.details)
        blood = view.findViewById(R.id.blood_grp)
        contact = view.findViewById(R.id.number)
        report_card = view.findViewById(R.id.report_card)

        disease = view.findViewById(R.id.Disease)
        vc = view.findViewById(R.id.Vaccination)
        report = view.findViewById(R.id.date_of_report)

        personal_frame = view.findViewById(R.id.PersonaL_Frame)
        covid_frame = view.findViewById(R.id.CoronA_Frame)

        data.addValueEventListener(object : ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    data.child("Profile").addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var sex = snapshot.child("Sex").getValue() as String
                            name.setText(snapshot.child("Name").getValue() as String)
                            state.setText(snapshot.child("State").getValue() as String)
                            city.setText(snapshot.child("City").getValue() as String)
                            var d = snapshot.child("DOB").getValue() as String
                            if(d.length == 8)
                                d = "0" + d
                            dob.setText(d)
                            blood.setText(snapshot.child("Blood_Grp").getValue() as String)
                            contact.setText(snapshot.child("Number").getValue() as String)
                            progress.visibility = View.INVISIBLE
                            progress_status = 1
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

                    data.child("Covid").addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                report_card.visibility = View.VISIBLE
                                disease.setText(snapshot.child("Disease").getValue() as String)
                                var rr = snapshot.child("Report_Date").getValue() as String
                                if(rr.length == 8)
                                    rr = "0" + rr
                                report.setText(rr)
                                var vacc = snapshot.child("First_Dose").getValue() as String
                                Log.i("vcc", vacc)
                                if(vacc.equals("1")){
                                    vacc = snapshot.child("Second_Dose").getValue() as String
                                    if(vacc.equals("1")){
                                        vc.setText("Second Dose")
                                    }
                                    else{
                                        vc.setText("First Dose")
                                    }
                                }
                                else{
                                    vc.setText("Not Yet")
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }
                else{
                    pd.setText("Add Personal Details")
                    Toast.makeText(activity,"Profile not Added",Toast.LENGTH_SHORT).show()
                    progress.visibility = View.INVISIBLE
                    progress_status = 1
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        update_personal.setOnClickListener(View.OnClickListener {
            if(progress_status == 1) {
                setFragmentProfileCreation(UpdateProfileFragment())
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

        report_card.setOnClickListener(View.OnClickListener {
            if(report.text.toString().length > 0){
                setFragmentReport(ReportFragment())
            }
        })

        return view
    }

    private fun setFragmentProfileCreation(forgotFragment: UpdateProfileFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }

    private fun setFragmentReport(forgotFragment: ReportFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }

}