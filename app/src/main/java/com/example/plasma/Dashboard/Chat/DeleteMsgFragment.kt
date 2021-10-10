package com.example.plasma.Dashboard.Chat

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.plasma.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeleteMsgFragment :  BottomSheetDialogFragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var delete : TextView
    lateinit var data : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_delete_msg, container, false)

        mAuth = FirebaseAuth.getInstance()

        var pref = PreferenceManager.getDefaultSharedPreferences(activity)
        var Msg_User = pref.getString("Current_User_Id","0")


        var bun = Bundle()
        bun = this.requireArguments()
        var id = bun.getString("Msgid") as String

        var user_id = mAuth.currentUser?.uid
        data = FirebaseDatabase.getInstance().getReference("Details")

        delete = view.findViewById(R.id.delete)



        delete.setOnClickListener(View. OnClickListener {
            if (user_id != null && Msg_User != null) {
                    data.child(user_id).child("Chatting").child(Msg_User).child("Message").child(id).removeValue().addOnSuccessListener(
                        OnSuccessListener {
                                Toast.makeText(activity,"Message deleted",Toast.LENGTH_SHORT).show()
                                dismiss()
                        }).addOnFailureListener {
                        Toast.makeText(activity,""+it.message.toString(),Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
            }
        })

        return view
    }

}