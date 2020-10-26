package com.lig.projemanage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lig.projemanage.R
import com.lig.projemanage.models.Board
import com.lig.projemanage.utils.Constants

class MembersActivity : AppCompatActivity() {
    private lateinit var mBoardDetails: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAILS)
        }

    }
}