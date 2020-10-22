package com.lig.projemanage.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lig.projemanage.R
import com.lig.projemanage.firebase.FireStoreClass
import com.lig.projemanage.models.Board
import com.lig.projemanage.utils.Constants
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.IOException

class CreateBoardActivity : BaseActivity() {

    private var mSelectedImageFileUri: Uri? = null

    private lateinit var mUserName: String

    private var mBoardImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        setupActionBar()
        if (intent.hasExtra((Constants.NAME))){
            mUserName = intent.getStringExtra(Constants.NAME) //Passing by intent to avoid get db request
        }

        iv_board_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        btn_create.setOnClickListener {
            if(mSelectedImageFileUri != null){
              uploadBoardImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                createBoard()
            }
        }

    }

    fun boardCreatedSuccessfully(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun createBoard(){
        val assignedUserArrayList: ArrayList<String> = ArrayList()
        assignedUserArrayList.add(getCurrentUserID())

        var board = Board(
            et_board_name.text.toString(),
            mBoardImageURL,
            mUserName,
            assignedUserArrayList
        )
        FireStoreClass().createBoard(this, board)
    }

    private fun uploadBoardImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        val sRef : StorageReference = FirebaseStorage.getInstance()
            .reference.child("BOARD_IMAGE" + System.currentTimeMillis() + "." + Constants.getFileExtersion(this, mSelectedImageFileUri))

        sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
            Log.e("board img urL", it.metadata!!.reference!!.downloadUrl.toString())
            it.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                Log.e("board img urL 2", it.toString())
                mBoardImageURL = it.toString()
                createBoard()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            hideProgressDialog()
        }
    }



    private fun setupActionBar(){
        setSupportActionBar(toolbar_create_board_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = resources.getString(R.string.create_board_title)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_create_board_activity.setNavigationOnClickListener { onBackPressed() }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null){
            mSelectedImageFileUri = data.data

            try {
                Glide // using glide to get media from its url
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(iv_board_image)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // show img
                Constants.showImageChooser(this)
            }
        }else{
            Toast.makeText(baseContext, "You just denied the permission for storage.", Toast.LENGTH_SHORT).show()
        }
    }


}