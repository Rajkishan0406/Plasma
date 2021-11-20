package com.example.plasma.Authentication

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.DashboardActivity
import com.example.plasma.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class LoginFragment : Fragment() {

    lateinit var new_User : TextView
    lateinit var email : EditText
    lateinit var pass : EditText
    lateinit var btn : NeumorphButton
    lateinit var pro : ProgressBar
    lateinit var frame : FrameLayout
    lateinit var top_card : CardView
    lateinit var forgot : TextView
    lateinit var newi : ForgotPassword
    lateinit var data : DatabaseReference
    var found = 0 as Int
    lateinit var emaill : TextInputLayout
    lateinit var passss : TextInputLayout

    lateinit var mAuth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_login, container, false)


        frame = view.findViewById(R.id.login_frame1)

        requireActivity().window.statusBarColor = Color.parseColor("#00B54B")


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
        emaill = view.findViewById(R.id.emialll)
        passss = view.findViewById(R.id.passs)
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
                emaill.isEnabled = false
                passss.isEnabled = false
                new_User.isEnabled = false
                forgot.isEnabled = false
                pro.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(email.text.toString(), pass.text.toString())
                    .addOnSuccessListener {
                        pro.visibility = View.INVISIBLE
                        var intent = Intent(activity,DashboardActivity::class.java)
                        extractPlasmaRequest()
                        startActivity(intent)
                    }.addOnFailureListener {
                        emaill.isEnabled = true
                        passss.isEnabled = true
                        new_User.isEnabled = true
                        forgot.isEnabled = true
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

    private fun FcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result as String
            // Log and toast
            Log.i("Token : ",""+token)
            mAuth.currentUser?.uid?.let { data.child(it).child("Token").setValue(token) }
        })
    }

    private fun extractPlasmaRequest() {
        data = FirebaseDatabase.getInstance().getReference("Details")
        var id = mAuth.currentUser?.uid
        FcmToken()
        if (id != null) {
            data.child(id).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        data.child(id).child("PlasmaRequest").addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    if(activity != null){
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