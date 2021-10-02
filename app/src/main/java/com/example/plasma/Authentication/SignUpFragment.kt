package com.example.plasma.Authentication

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.example.plasma.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import soup.neumorphism.NeumorphButton

class SignUpFragment : Fragment() {

    lateinit var email : EditText
    lateinit var pass : EditText
    lateinit var con_pass : EditText
    lateinit var btn : NeumorphButton
    lateinit var pro : ProgressBar

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        requireActivity().window.statusBarColor = Color.parseColor("#00B54B")

        email = view.findViewById(R.id.email)
        pass = view.findViewById(R.id.password)
        con_pass = view.findViewById(R.id.password_conf)
        btn = view.findViewById(R.id.signup_btn)
        pro = view.findViewById(R.id.signup_progress_bar)

        mAuth = FirebaseAuth.getInstance()

        btn.setOnClickListener(View.OnClickListener {
            if(email.text.toString().length == 0 || pass.text.toString().length == 0 || con_pass.text.toString().length == 0){
                Toast.makeText(activity,"Please fill all details",Toast.LENGTH_SHORT).show()
            }
            else if(pass.text.length < 6){
                Toast.makeText(activity,"Password Length must be Greater then 5",Toast.LENGTH_SHORT).show()
            }
            else if(!pass.text.toString().equals(con_pass.text.toString())){
                Toast.makeText(activity,"Confirm Password doesn't match with password!",Toast.LENGTH_SHORT).show()
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
                Toast.makeText(activity,"Wrong email format!",Toast.LENGTH_SHORT).show()
            }
            else{
                pro.visibility = View.VISIBLE
                SignUp(email.text.toString(),pass.text.toString())
            }
        })


        return view
    }

    private fun SignUp(email: String, password: String) {
            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                FcmToken()
                Toast.makeText(activity,"Signup Successfull",Toast.LENGTH_SHORT).show()
                pro.visibility = View.INVISIBLE
                setFragmentProfile(ProfileCreationFragment())
            }.addOnFailureListener {
                Toast.makeText(activity,""+it.message.toString(),Toast.LENGTH_SHORT).show()
                pro.visibility = View.INVISIBLE
            }
    }

    private fun FcmToken() {
        data = mAuth.currentUser?.uid?.let { FirebaseDatabase.getInstance().getReference("Details").child(it) }!!
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result as String
            Log.i("Token : ",""+token)
            data.child("Token").setValue(token)
        })
    }

    private fun setFragmentProfile(forgotFragment: ProfileCreationFragment) {
        var ft: FragmentTransaction? = getFragmentManager()?.beginTransaction()
        if (ft != null) {
            ft.replace(R.id.main_auth_frame, forgotFragment)
        }
        if (ft != null) {
            ft.addToBackStack(null).commit()
        }
    }

}