package com.lig.projemanage.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.lig.projemanage.R
import com.lig.projemanage.firebase.FireStoreClass
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //set activity full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        tv_app_name.typeface = typeFace

        Handler().postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            finish() // close activity after very important
        }, 2500)

    }
}