package com.lig.projemanage.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lig.projemanage.R
import com.lig.projemanage.adapters.MemberListItemsAdapter
import com.lig.projemanage.firebase.FireStoreClass
import com.lig.projemanage.models.Board
import com.lig.projemanage.models.User
import com.lig.projemanage.utils.Constants
import kotlinx.android.synthetic.main.activity_members.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.dialog_search_member.*

class MembersActivity : BaseActivity() {
    private lateinit var mBoardDetails: Board
    private lateinit var mAssignedMemberList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAILS)
        }
        setupActionBar()

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_members_activity)
        val actionBar = supportActionBar

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }

        toolbar_members_activity.setNavigationOnClickListener { onBackPressed() }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_add_member ->{
                dialogSearchMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun dialogSearchMember(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_member)
        dialog.tv_add.setOnClickListener {
                val email = dialog.et_email_search_member.text.toString()
                if(email.isNotEmpty()){
                    dialog.dismiss()
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FireStoreClass().getMemberDetails(this, email)
                }else{
                    Toast.makeText(this@MembersActivity, "please enter members address.", Toast.LENGTH_SHORT).show()
                }
        }
        dialog.tv_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setupMembersList(list: ArrayList<User>){
        mAssignedMemberList = list
        hideProgressDialog()
        rv_members_list.layoutManager = LinearLayoutManager(this)
        rv_members_list.setHasFixedSize(true)

        val adapter = MemberListItemsAdapter(this, list)
        rv_members_list.adapter = adapter
    }

    fun memberDetail(user: User){
        mBoardDetails.assignedTo.add(user.id)
        FireStoreClass().assignMemberToBoard(this, mBoardDetails, user)
    }

    fun memberAssignSuccess(user: User){
        hideProgressDialog()
        mAssignedMemberList.add(user)
        setupMembersList(mAssignedMemberList) // update list
    }


}