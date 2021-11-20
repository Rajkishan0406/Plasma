package com.example.plasma.Authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.plasma.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import soup.neumorphism.NeumorphButton

class ForgotPassword : BottomSheetDialogFragment(){

    lateinit var mAuth : FirebaseAuth
    lateinit var email : EditText
    lateinit var btn : NeumorphButton
    lateinit var emaill : TextInputLayout
    lateinit var progress : ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        mAuth = FirebaseAuth.getInstance()
        email = view.findViewById(R.id.email)
        btn = view.findViewById(R.id.cardview_forgot_password_button)
        progress = view.findViewById(R.id.cardview_forgot_password_progressbar)
        emaill = view.findViewById(R.id.emailll)

        btn.setOnClickListener(View.OnClickListener {
            var E = email.text.toString()
            if(E.isEmpty())
                Toast.makeText(activity,"Please enter your email",Toast.LENGTH_SHORT).show()
            else {
                emaill.isEnabled = false;
                progress.visibility = View.VISIBLE
                forgotpassword(E)
            }
        })

        return view;
    }

    private fun forgotpassword(e: String) {
        activity?.let {
            mAuth.sendPasswordResetEmail(e)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        progress.visibility = View.INVISIBLE
                        emaill.isEnabled = true
                        Toast.makeText(activity,"Password reset link send to the registered email",Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        progress.visibility = View.INVISIBLE
                        emaill.isEnabled = true
                        Toast.makeText(activity, "Wrong Email id please try again",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}