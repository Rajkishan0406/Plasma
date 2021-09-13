package com.example.plasma.Dashboard.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ortiz.touchview.TouchImageView
import com.squareup.picasso.Picasso

class ReportFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference
    lateinit var storage : StorageReference
    lateinit var image : TouchImageView
    lateinit var pro : ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_report, container, false)

        var bun = Bundle()
        bun = this.requireArguments()
        var id : String? = bun.getString("User_Id")

        data = id?.let { FirebaseDatabase.getInstance().getReference("Details").child(it) }!!
        storage = FirebaseStorage.getInstance().getReference(id)
        image = view.findViewById(R.id.report_image)
        pro = view.findViewById(R.id.progress_image)

        data.child("Covid").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var token = snapshot.child("Token").getValue() as String
                    Picasso.get().load(token).into(image)
                    pro.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        return view
    }
}