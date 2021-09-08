package com.example.plasma.Authentication

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.DashboardActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class LoginFragment : Fragment() {

    lateinit var new_User : TextView
    lateinit var email : EditText
    lateinit var pass : EditText
    lateinit var btn : NeumorphButton
    lateinit var pro : ProgressBar
    lateinit var top_card : CardView
    lateinit var forgot : TextView
    lateinit var newi : ForgotPassword
    lateinit var data : DatabaseReference
    var found = 0 as Int

    lateinit var mAuth : FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
            val intent = Intent(getActivity(), DashboardActivity::class.java)
            getActivity()?.startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_login, container, false)


        if (activity?.let {
                    ActivityCompat.checkSelfPermission(it,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                } != PackageManager.PERMISSION_GRANTED) {
            // request permission
            activity?.let {
                ActivityCompat.requestPermissions(it,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            }
        }

        top_card = view.findViewById(R.id.top_card)

        val animation = AnimationUtils.loadAnimation(activity, R.anim.top_to_down)
        top_card.startAnimation(animation)

        mAuth = FirebaseAuth.getInstance()
        email = view.findViewById(R.id.email)
        pass = view.findViewById(R.id.password)
        btn = view.findViewById(R.id.login_btn)
        pro = view.findViewById(R.id.login_progress_bar)
        forgot = view.findViewById(R.id.forgot_password_textview)

        newi = ForgotPassword()

        forgot.setOnClickListener(View.OnClickListener {
            newi.show(childFragmentManager,"bottom sheet")
        })

        btn.setOnClickListener(View.OnClickListener {
            if(email.text.toString().length == 0 || pass.text.toString().length == 0)
                Toast.makeText(activity,"Enter email and Password",Toast.LENGTH_SHORT).show()
            else {
                pro.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
                    .addOnSuccessListener {
                        pro.visibility = View.INVISIBLE
                        var intent = Intent(activity,DashboardActivity::class.java)
                        extractPlasmaRequest()
                        startActivity(intent)
                    }.addOnFailureListener {
                    Toast.makeText(activity, "" + it.message.toString(), Toast.LENGTH_SHORT).show()
                        pro.visibility = View.INVISIBLE
                }
            }
        })

        new_User = view.findViewById(R.id.New_User_textview)
        new_User.setOnClickListener(View.OnClickListener {
            setFragmentSignup(SignUpFragment())
        })

        return view
    }



    private fun extractPlasmaRequest() {
        data = FirebaseDatabase.getInstance().getReference("Details")
        var id = mAuth.currentUser?.uid
        if (id != null) {
            data.child(id).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        data.child(id).child("PlasmaRequest").addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    var pref = PreferenceManager.getDefaultSharedPreferences(activity)
                                    pref.apply {
                                        val request = snapshot.getValue() as String
                                        val editor = pref.edit()
                                        editor.putString("Request", request)
                                        editor.apply()
                                        Log.i("request", request)
                                        found = 1;
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }




    private fun setFragmentSignup(forgotFragment: SignUpFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_auth_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }

}