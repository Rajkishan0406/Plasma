package com.example.plasma.Dashboard

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService
import com.example.plasma.DashboardActivity
import com.example.plasma.MainActivity
import com.example.plasma.MapActivity
import com.example.plasma.R

class NoInternetFragment : Fragment() {

    lateinit var btn : CardView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_no_internet, container, false)

        checkConnection()

        btn = view.findViewById(R.id.internet_try)

        btn.setOnClickListener(View.OnClickListener {
            var ans : Boolean = checkAgain()
            if(ans) {
                val intent = Intent(getActivity(), MainActivity::class.java)
                getActivity()?.startActivity(intent)
            }
        })

        return view
    }

    private fun checkAgain(): Boolean {
        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfor = manager.activeNetworkInfo




        if (networkInfor != null) {
            if(networkInfor.type == ConnectivityManager.TYPE_WIFI || networkInfor.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        }
        return false
    }

    private fun checkConnection() {
        val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfor = manager.activeNetworkInfo




        if (networkInfor != null) {
            if(networkInfor.type == ConnectivityManager.TYPE_WIFI || networkInfor.type == ConnectivityManager.TYPE_MOBILE) {
                var intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

  }