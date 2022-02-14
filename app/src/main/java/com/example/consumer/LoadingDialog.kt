package com.example.consumer

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity

class LoadingDialog {

    lateinit var activity:Activity
    lateinit var dialog:Dialog

    constructor(activity: FragmentActivity?)
    {
        if (activity != null) {
            this.activity=activity
        }
    }

    fun startLoadingDialog(){

        var builder:AlertDialog.Builder=AlertDialog.Builder(activity)
        var inflater:LayoutInflater=activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog,null))
        builder.setCancelable(true)

        dialog=builder.create()
        dialog.show()

    }

    fun dismissDialog(){

        dialog.dismiss()
    }
}