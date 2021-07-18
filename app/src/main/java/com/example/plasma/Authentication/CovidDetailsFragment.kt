package com.example.plasma.Authentication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.plasma.DashboardActivity
import com.example.plasma.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import soup.neumorphism.NeumorphButton
import java.util.*

class CovidDetailsFragment : Fragment() {

    lateinit var skip : TextView
    lateinit var upload : ImageView
    lateinit var sub : NeumorphButton
    lateinit var image : CardView
    lateinit var date : CardView
    lateinit var dd : TextView
    lateinit var prob : EditText
    lateinit var dose_one : CheckBox
    lateinit var dose_two : CheckBox
    lateinit var age : CheckBox
    lateinit var pro : ProgressBar
    var y = ""
    var m = ""
    var d = ""
    private var imageUri: Uri? = null
    private val pickImage = 100
    var checker = 0
    lateinit var storage : StorageReference
    var image_added = 0

    var age_yes = 0 as Int
    var first_yes = 0 as Int
    var second_yes = 0 as Int

    lateinit var mAuth : FirebaseAuth
    lateinit var data : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_covid_details, container, false)


        skip = view.findViewById(R.id.skip)
        sub = view.findViewById(R.id.submit)
        image = view.findViewById(R.id.image_selector)
        dd = view.findViewById(R.id.date_of_report)
        date = view.findViewById(R.id.calendar)
        prob = view.findViewById(R.id.disease)
        dose_one = view.findViewById(R.id.first)
        dose_two = view.findViewById(R.id.second)
        age = view.findViewById(R.id.age)
        upload = view.findViewById(R.id.image_uploaded)
        pro = view.findViewById(R.id.progress)

        image.setOnClickListener(View.OnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        })

        val c = Calendar.getInstance()
        val year  = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mAuth = FirebaseAuth.getInstance()
        var id = mAuth.currentUser?.uid
        data = id?.let { FirebaseDatabase.getInstance().getReference("Details").child(it).child("Covid") }!!
        storage = FirebaseStorage.getInstance().getReference(id)

        date.setOnClickListener(View.OnClickListener {
          val dpd = activity?.let { it1 ->
              DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
                  val kj = mmonth+1 as Int
                  y = myear.toString()
                  d = mday.toString()
                  m = kj.toString()
                  dd.setText(""+mday+"/"+kj+"/"+myear)
              },year,month,day)
                }
            dpd?.show()
            }
        )


        age.setOnClickListener(View.OnClickListener {
            if(age_yes == 1)
                age_yes = 0
            else
                age_yes = 1
        })

        dose_one.setOnClickListener(View.OnClickListener {
            if(first_yes == 1)
                first_yes = 0;
            else
                first_yes = 1
        })

        dose_two.setOnClickListener(View.OnClickListener {
            if(second_yes == 1)
                second_yes = 0
            else
                second_yes = 1
        })

        skip.setOnClickListener(View.OnClickListener {
            var intent = Intent(activity, DashboardActivity::class.java)
            startActivity(intent)
        })

        sub.setOnClickListener(View.OnClickListener {
            if(dd.text.toString().length == 0 || prob.text.toString().length == 0){
                Toast.makeText(activity,"Please fill the text",Toast.LENGTH_SHORT).show()
            }
            else if(second_yes == 1 && first_yes == 0){
                Toast.makeText(activity,"You can't take second dose without first dose!",Toast.LENGTH_SHORT).show()
            }
            else{
                pro.visibility = View.VISIBLE
                var ddd = d + "/" + m + "/" + y
                data.child("Report_Date").setValue(ddd)
                data.child("Disease").setValue(prob.text.toString())
                data.child("First_Dose").setValue(first_yes.toString())
                data.child("Second_Dose").setValue(second_yes.toString())
                data.child("Age").setValue(age_yes.toString())
                storeimage(id)
            }
        })

        return view
    }

    private fun storeimage(id: String) {
        imageUri?.let {
            storage.putFile(it).addOnSuccessListener {
                image_added = 1
                Toast.makeText(activity, "Covid Details added successfully ", Toast.LENGTH_SHORT).show()
                storage.downloadUrl.addOnSuccessListener {
                    Log.i("message : "," "+it)
                    var k = "" as String
                    var r = "" as String
                    r = it.toString()
                    Log.i("Toekn : "," "+r)
                    var data: DatabaseReference
                    data = FirebaseDatabase.getInstance().getReference("Details").child(id)
                    data.child("Covid").child("Token").setValue(r)
                    pro.visibility = View.INVISIBLE
                    var intent = Intent(activity, DashboardActivity::class.java)
                    startActivity(intent)
                }
                checker = -1
                Log.i("image upload : ", "Successfull")
            }
                    .addOnFailureListener(){
                        Toast.makeText(activity, "Some thing went wrong!! please try again later", Toast.LENGTH_SHORT).show()
                        checker = -1
                        Log.i("image upload : ", "Fail")
                        pro.visibility = View.INVISIBLE
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            upload.visibility = View.VISIBLE
        }

    }


}