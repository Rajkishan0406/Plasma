package com.example.plasma.Dashboard.Home

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.plasma.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView

class FilterFragment :  BottomSheetDialogFragment() {

    lateinit var state : EditText
    lateinit var city : EditText
    lateinit var btn : NeumorphButton
    lateinit var bl : CardView
    lateinit var cit : CardView
    lateinit var sta : CardView


    //blood group neumorphCard
    lateinit var a_pos : NeumorphCardView
    lateinit var b_pos : NeumorphCardView
    lateinit var ab_pos : NeumorphCardView
    lateinit var o_pos : NeumorphCardView
    lateinit var a_neg : NeumorphCardView
    lateinit var b_neg : NeumorphCardView
    lateinit var o_neg : NeumorphCardView
    lateinit var ab_neg : NeumorphCardView

    lateinit var A_pos : TextView
    lateinit var B_pos : TextView
    lateinit var AB_pos : TextView
    lateinit var O_pos : TextView
    lateinit var A_neg : TextView
    lateinit var B_neg : TextView
    lateinit var O_neg : TextView
    lateinit var AB_neg : TextView


    var b = 0  as Int
    var c = 0 as Int
    var s = 0 as Int
    var Blood = "" as String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_filter, container, false)


        //blood group addition
        a_pos = view.findViewById(R.id.A_positive_update)
        ab_pos = view.findViewById(R.id.AB_positive_update)
        b_pos = view.findViewById(R.id.B_positive_update)
        o_pos = view.findViewById(R.id.O_positive_update)
        a_neg = view.findViewById(R.id.A_negative_update)
        ab_neg = view.findViewById(R.id.AB_negative_update)
        b_neg = view.findViewById(R.id.B_negative_update)
        o_neg = view.findViewById(R.id.O_negative_update)

        A_pos = view.findViewById(R.id.A_update)
        AB_pos = view.findViewById(R.id.AB_update)
        B_pos = view.findViewById(R.id.B_update)
        O_pos = view.findViewById(R.id.O_update)
        A_neg = view.findViewById(R.id.A_n_update)
        AB_neg = view.findViewById(R.id.AB_n_update)
        B_neg = view.findViewById(R.id.B_n_update)
        O_neg = view.findViewById(R.id.O_n_update)

        bl = view.findViewById(R.id.blood_card)
        cit = view.findViewById(R.id.city_card)
        sta = view.findViewById(R.id.state_card)
        city = view.findViewById(R.id.city_name)
        state = view.findViewById(R.id.state_name)
        btn = view.findViewById(R.id.submit)

        bl.setOnClickListener(View.OnClickListener {
            if(b == 0) {
                b = 1
                bl.setCardBackgroundColor(Color.GREEN)
            }
            else{
                b = 0
                bl.setCardBackgroundColor(Color.WHITE)
            }
         })

        cit.setOnClickListener(View.OnClickListener {
            if(c == 0) {
                c = 1
                cit.setCardBackgroundColor(Color.GREEN)
            }
            else{
                c = 0
                cit.setCardBackgroundColor(Color.WHITE)
            }
        })

        sta.setOnClickListener(View.OnClickListener {
            if(s == 0) {
                s = 1
                sta.setCardBackgroundColor(Color.GREEN)
            }
            else{
                s = 0
                sta.setCardBackgroundColor(Color.WHITE)
            }
        })

        a_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "A+"
            A_pos.setTextColor(Color.RED)
            a_pos.setShapeType(1)
        })

        b_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "B+"
            B_pos.setTextColor(Color.RED)
            b_pos.setShapeType(1)
        })

        ab_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "AB+"
            AB_pos.setTextColor(Color.RED)
            ab_pos.setShapeType(1)
        })

        o_pos.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "O+"
            O_pos.setTextColor(Color.RED)
            o_pos.setShapeType(1)
        })

        a_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "A-"
            A_neg.setTextColor(Color.RED)
            a_neg.setShapeType(1)
        })

        b_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "B-"
            B_neg.setTextColor(Color.RED)
            b_neg.setShapeType(1)
        })

        ab_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "AB-"
            AB_neg.setTextColor(Color.RED)
            ab_neg.setShapeType(1)
        })

        o_neg.setOnClickListener(View.OnClickListener {
            allWhite()
            allFlat()
            Blood = "O-"
            O_neg.setTextColor(Color.RED)
            o_neg.setShapeType(2)
        })

        btn.setOnClickListener(View.OnClickListener {
            var pref = PreferenceManager.getDefaultSharedPreferences(activity)
            pref.apply {
                val editor = pref.edit()
                if(b == 1 && Blood.length == 0){
                    Toast.makeText(activity,"Please select Blood group",Toast.LENGTH_SHORT).show()
                }
                else if(c == 1 && city.text.toString().length == 0){
                    Toast.makeText(activity,"Please type prefer city",Toast.LENGTH_SHORT).show()
                }
                else if(s == 1 && state.text.toString().length == 0){
                    Toast.makeText(activity,"Please type prefer state",Toast.LENGTH_SHORT).show()
                }
                else {
                    if (city.text.toString().length > 0 && c == 1)
                        editor.putString("City", city.text.toString())
                    else
                        editor.putString("City", "0")
                    if (state.text.toString().length > 0 && s == 1)
                        editor.putString("State", state.text.toString())
                    else
                        editor.putString("State", "0")
                    if (Blood.length > 0 && b == 1)
                        editor.putString("Blood", Blood)
                    else
                        editor.putString("Blood", "0")
                    editor.apply()
                    dismiss()
                }
            }
        })



        return view
    }

    private fun allFlat() {
        a_pos.setShapeType(0)
        ab_pos.setShapeType(0)
        b_pos.setShapeType(0)
        o_pos.setShapeType(0)
        ab_neg.setShapeType(0)
        o_neg.setShapeType(0)
        b_neg.setShapeType(0)
        a_neg.setShapeType(0)
    }

    private fun allWhite() {
        A_pos.setTextColor(Color.BLACK)
        B_pos.setTextColor(Color.BLACK)
        O_pos.setTextColor(Color.BLACK)
        AB_pos.setTextColor(Color.BLACK)
        A_neg.setTextColor(Color.BLACK)
        AB_neg.setTextColor(Color.BLACK)
        O_neg.setTextColor(Color.BLACK)
        B_neg.setTextColor(Color.BLACK)
    }


}