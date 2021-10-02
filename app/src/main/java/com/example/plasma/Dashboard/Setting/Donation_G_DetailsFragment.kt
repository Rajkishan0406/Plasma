package com.example.plasma.Dashboard.Setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.Dashboard.Chat.ChatPageFragment
import com.example.plasma.Dashboard.Home.RulesAndRegulationFragment
import com.example.plasma.Dashboard.Profile.ReportFragment
import com.example.plasma.MapActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class Donation_G_DetailsFragment : Fragment() {

    lateinit var bun : Bundle
    lateinit var donate : NeumorphButton
    lateinit var name : TextView
    lateinit var age : TextView
    lateinit var gender : TextView
    lateinit var blood : TextView
    lateinit var msg : CardView
    lateinit var map : CardView
    lateinit var call : CardView
    lateinit var city : TextView
    lateinit var state : TextView
    lateinit var report : TextView
    lateinit var vaccination : TextView
    lateinit var disease : TextView
    lateinit var doc : NeumorphCardView
    lateinit var pro : ProgressBar

    var sex = "" as String
    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    var number = "" as String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_donation__g__details, container, false)

        // bundle code for extracting user id....
        bun = Bundle()
        bun = this.requireArguments()
        var id : String? = bun.getString("Id") as String

        data = FirebaseDatabase.getInstance().getReference("Details")


        name = view.findViewById(R.id.name)
        age = view.findViewById(R.id.age)
        gender = view.findViewById(R.id.gender)
        blood = view.findViewById(R.id.blood)
        msg = view.findViewById(R.id.message)
        call = view.findViewById(R.id.call)
        city = view.findViewById(R.id.city)
        state = view.findViewById(R.id.state)
        report = view.findViewById(R.id.date_report)
        map = view.findViewById(R.id.map)
        vaccination = view.findViewById(R.id.Vaccination)
        disease = view.findViewById(R.id.Disease)
        doc = view.findViewById(R.id.reportcard)
        pro = view.findViewById(R.id.progress)


        if (id != null) {
            data.child(id).child("Profile").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        if(snapshot.hasChild("Name"))
                            name.setText(snapshot.child("Name").getValue() as String)
                        if(snapshot.hasChild("State"))
                            state.setText(snapshot.child("State").getValue() as String)
                        if(snapshot.hasChild("City"))
                            city.setText(snapshot.child("City").getValue() as String)
                        if(snapshot.hasChild("Blood_Grp"))
                            blood.setText(snapshot.child("Blood_Grp").getValue() as String)
                        if(snapshot.hasChild("Number"))
                            number = snapshot.child("Number").getValue() as String
                        if(snapshot.hasChild("Sex")) {
                            sex = snapshot.child("Sex").getValue() as String
                            if (sex.equals("male")) {
                                gender.setText("Gender : Male")
                            } else {
                                gender.setText("Gender : Female")
                            }
                        }
                        var status = snapshot.child("Status").getValue() as String
                        if(status.equals("No")){
                            call.visibility = View.INVISIBLE
                        } else
                            call.visibility = View.VISIBLE
                        pro.visibility = View.INVISIBLE
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }

        if (id != null) {
            data.child(id).child("Covid").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
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
                            if (vacc.equals("1")) {
                                if (snapshot.hasChild("Second_Dose")) {
                                    vacc = snapshot.child("Second_Dose").getValue() as String
                                    if (vacc.equals("1")) {
                                        vaccination.setText("Second Dose")
                                    } else {
                                        vaccination.setText("First Dose")
                                    }
                                }
                            } else {
                                vaccination.setText("Not Yet")
                            }
                        }
                        pro.visibility = View.INVISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        doc.setOnClickListener(View.OnClickListener {
            var IDF = ReportFragment()
            var bun : Bundle
            bun = Bundle()
            bun.putString("User_Id",id)
            IDF.arguments = bun
            setFragmentReport(IDF)
        })

        donate = view.findViewById(R.id.cancel_btn)
        mAuth = FirebaseAuth.getInstance()

        var Present_User_Id = mAuth.currentUser?.uid as String

        if(id.equals(Present_User_Id)){
            donate.visibility = View.INVISIBLE
        }

        msg.setOnClickListener(View.OnClickListener {
            if(!id.equals(Present_User_Id)){
                val IDF = ChatPageFragment()
                var bun: Bundle
                bun = Bundle()
                bun.putString("Name", name.text.toString())
                bun.putString("Id", id)
                IDF.arguments = bun
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_dashboard_frame, IDF)?.commit()
            }
        })

        //Map View
        map.setOnClickListener(View.OnClickListener {
            val intent = Intent(getActivity(), MapActivity::class.java)
            intent.putExtra("User_Id",id)
            getActivity()?.startActivity(intent)
        })


        donate.setOnClickListener(View.OnClickListener {
            if (Present_User_Id != null) {
                data.child(Present_User_Id).child("Donation_Give").child(number).removeValue()
            }
            if (id != null && Present_User_Id != null) {
                var user_number = "" as String
                data.child(Present_User_Id).child("Profile").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            user_number = snapshot.child("Number").getValue() as String
                            data.child(id).child("Donation_Want").child(user_number).removeValue()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            }
            var IDF = SettingFragment()
            setFragmentRules(IDF)
        })

        return view
    }

    private fun setFragmentRules(forgotFragment: SettingFragment) {
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
            ft.commit()
        }
    }

}