package com.example.plasma.Dashboard.Profile

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.example.plasma.MainActivity
import com.example.plasma.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import soup.neumorphism.NeumorphCardView
import java.nio.file.Files.delete

class ProfileFragment : Fragment() {

    lateinit var personal : CardView
    lateinit var update_personal : CardView
    lateinit var update_covid : CardView
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
    lateinit var delete : CardView
    lateinit var storage : StorageReference

    lateinit var disease : TextView
    lateinit var vc : TextView
    lateinit var report : TextView
    var progress_status = 0 as Int
    var sex = "" as String

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        requireActivity().window.statusBarColor = Color.WHITE

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid as String
        data = FirebaseDatabase.getInstance().getReference("Details").child(id)
        storage = FirebaseStorage.getInstance().getReference(id)

        progress = view.findViewById(R.id.profile_progressbar)
        update_personal = view.findViewById(R.id.edit_personal_details)
        Boy = view.findViewById(R.id.lottie_boy)
        Girl = view.findViewById(R.id.lottie_girl)
        name = view.findViewById(R.id.user_name)
        state = view.findViewById(R.id.state)
        city = view.findViewById(R.id.City)
        dob = view.findViewById(R.id.dob)
        update_covid = view.findViewById(R.id.update_covid_details)
        pd = view.findViewById(R.id.details)
        blood = view.findViewById(R.id.blood_grp)
        contact = view.findViewById(R.id.number)
        report_card = view.findViewById(R.id.report_card)
        delete = view.findViewById(R.id.delete_account)

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
                            if (snapshot.exists()) {
                                if(snapshot.hasChild("Sex")) {
                                    sex = snapshot.child("Sex").getValue() as String
                                    if (sex.equals("male")) {
                                        Boy.visibility = View.VISIBLE
                                        Girl.visibility = View.INVISIBLE
                                    }
                                    else {
                                        Boy.visibility = View.INVISIBLE
                                        Girl.visibility = View.VISIBLE
                                    }
                                }
                                if(snapshot.hasChild("Name"))
                                    name.setText(snapshot.child("Name").getValue() as String)
                                if(snapshot.hasChild("State"))
                                    state.setText(snapshot.child("State").getValue() as String)
                                if(snapshot.hasChild("City"))
                                    city.setText(snapshot.child("City").getValue() as String)
                                if(snapshot.hasChild("DOB")) {
                                    var d = snapshot.child("DOB").getValue() as String
                                    if (d.length == 8)
                                        d = "0" + d
                                    dob.setText(d)
                                }
                                if(snapshot.hasChild("Blood_Grp"))
                                    blood.setText(snapshot.child("Blood_Grp").getValue() as String)
                                if(snapshot.hasChild("Number"))
                                    contact.setText(snapshot.child("Number").getValue() as String)
                                progress.visibility = View.INVISIBLE
                                progress_status = 1
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



        data.addValueEventListener(object : ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    data.child("Covid").addValueEventListener(object : ValueEventListener{
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                report_card.visibility = View.VISIBLE
                                if(snapshot.hasChild("Disease")) {
                                    var DiS = snapshot.child("Disease").getValue() as String
                                    disease.setText(DiS)
                                }
                                if(snapshot.hasChild("Report_Date")) {
                                    var rr = snapshot.child("Report_Date").getValue() as String
                                    if (rr.length == 8)
                                        rr = "0" + rr
                                    report.setText(rr)
                                }
                                if(snapshot.hasChild("First_Dose")) {
                                    var vacc = snapshot.child("First_Dose").getValue() as String
                                    Log.i("vcc", vacc)
                                    if (vacc.equals("1")) {
                                        if (snapshot.hasChild("Second_Dose")) {
                                            vacc = snapshot.child("Second_Dose").getValue() as String
                                            if (vacc.equals("1")) {
                                                vc.setText("Second Dose")
                                            } else {
                                                vc.setText("First Dose")
                                            }
                                        }
                                    }
                                    else {
                                        vc.setText("Not Yet")
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
                else{

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        update_personal.setOnClickListener(View.OnClickListener {
            if(progress_status == 1) {
                var frag = UpdateProfileFragment()
                val bundle = Bundle()
                bundle.putString("name", name.text.toString())
                bundle.putString("state", state.text.toString())
                bundle.putString("city", city.text.toString())
                bundle.putString("dob", dob.text.toString())
                bundle.putString("sex", sex)
                bundle.putString("blood", blood.text.toString())
                bundle.putString("contact", contact.text.toString())
                frag.setArguments(bundle)
                setFragmentProfileCreation(frag)
            }
        })

        update_covid.setOnClickListener(View.OnClickListener {
            if(progress_status == 1){
                var frag = UpdateCovidFragment()
                val bundle = Bundle()
                bundle.putString("reportdate",report.text.toString())
                frag.setArguments(bundle)
                setFragmentCovidUpdation(frag)
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
                var IDF = ReportFragment()
                var bun : Bundle
                bun = Bundle()
                bun.putString("User_Id",id)
                IDF.arguments = bun
                setFragmentReport(IDF)
            }
        })

        delete.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(this.requireContext())
                    .setTitle("Alert")
                    .setMessage("Are you sure you want to delete your Account? Once you delete all your data will be deleted")
                    .setNegativeButton("Cancel") { dialog, which ->
                        Log.i("Message : ", "Canceled")
                    }
                    .setPositiveButton("Delete") { dialog, which ->
                        Log.i("Message : ", "Deleted")
                        progress.visibility = View.VISIBLE
                        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
                        pref.apply {
                            val editor = pref.edit()
                            editor.putString("Request", "0")
                            editor.apply()
                            Log.i("request", "0")
                        }
                        var data: DatabaseReference
                        var delete_it = 0 as Int
                        data = FirebaseDatabase.getInstance().getReference("Details").child(id)
                        if (delete_it == 0) {
                            data.removeValue().addOnCompleteListener(OnCompleteListener {
                                Log.i("data deleted ","Successfully")
                                storage.delete().addOnCompleteListener(OnCompleteListener {
                                    progress.visibility = View.INVISIBLE
                                    delete_it = 1
                                    Log.i("image deleted ","Successfully")
                                    mAuth.currentUser!!.delete().addOnCompleteListener(OnCompleteListener {
                                        Toast.makeText(activity, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(getActivity(), MainActivity::class.java)
                                        getActivity()?.startActivity(intent)
                                    }).addOnFailureListener { OnFailureListener {
                                        Toast.makeText(activity,"Please Login again to delete account",Toast.LENGTH_LONG).show()
                                        progress.visibility = View.INVISIBLE
                                        mAuth.signOut()
                                    } }
                                }).addOnFailureListener(OnFailureListener {
                                    progress.visibility = View.INVISIBLE
                                    delete_it = 1
                                    Log.i("image not deleted ","Successfully")
                                    mAuth.currentUser!!.delete().addOnCompleteListener(OnCompleteListener {
                                        Toast.makeText(activity, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                                        mAuth.signOut()
                                        val intent = Intent(getActivity(), MainActivity::class.java)
                                        getActivity()?.startActivity(intent)
                                    }).addOnFailureListener { OnFailureListener {
                                        Toast.makeText(activity,"Please Login again to delete account",Toast.LENGTH_LONG).show()
                                        progress.visibility = View.INVISIBLE
                                        mAuth.signOut()
                                    } }
                                    })
                            }).addOnFailureListener(OnFailureListener {
                                progress.visibility = View.INVISIBLE
                                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    .show()
        })

        return view
    }

    private fun setFragmentProfileCreation(forgotFragment: UpdateProfileFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.commit()
        }
    }

    private fun setFragmentCovidUpdation(forgotFragment: UpdateCovidFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_dashboard_frame, forgotFragment)
        }
        if (ft != null) {
            ft.commit()
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