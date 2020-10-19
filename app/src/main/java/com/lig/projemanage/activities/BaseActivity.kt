package com.lig.projemanage.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lig.projemanage.R

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }



}