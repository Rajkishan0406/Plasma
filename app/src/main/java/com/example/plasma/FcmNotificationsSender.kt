package com.example.plasma


import android.app.Activity
import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class FcmNotificationsSender {

    var userFcmToken: String? = null
    var title: String? = null
    var body: String? = null
    var mContext: Context? = null
    var mActivity: Activity? = null

    private var requestQueue: RequestQueue? = null
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey =
        "AAAA8CUd3-U:APA91bFO_mNn5Oap9IumJzDQregwgaazvkdbK9PwmT3DC9Pl2TWlXwNQjvbCoQ9lr5hNVKWyALoif7kyy8zG8P1TRFbq1g8tAj4e90WwR9jxe86uKyFQ0AmJF79F00SsN53D1yv5VYg_"

    fun FcmNotificationsSender(
        userFcmToken: String?,
        title: String?,
        body: String?,
        mContext: Context?,
        mActivity: Activity?
    ) {
        this.userFcmToken = userFcmToken
        this.title = title
        this.body = body
        this.mContext = mContext
        this.mActivity = mActivity
    }


    public fun SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity)
        val mainObj = JSONObject()




}
}