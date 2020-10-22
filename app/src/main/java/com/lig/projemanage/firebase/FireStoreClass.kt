package com.lig.projemanage.firebase

import android.app.Activity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.lig.projemanage.activities.*
import com.lig.projemanage.models.Board
import com.lig.projemanage.utils.Constants


class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    private val Log = this.javaClass.simpleName

    fun registerUser(activity: SignUpActivity, userInfo: com.lig.projemanage.models.User){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge()) // passing our custom model in db
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }

    fun getCurrentUserId(): String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                android.util.Log.e(Log, "Board create successfully!")
                Toast.makeText(activity, "Board create successfully!", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                android.util.Log.e(Log, "Board create failure $it!")
            }
    }

    fun updateUserProfileData(activity: MyProfileActivity,
                              userHashMap: HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                android.util.Log.e(Log, "Profile update successfully!")
                activity.profileUptateSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                android.util.Log.e(Log, "Error on $it")
            }
    }

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())  // in the mean time update user information
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(com.lig.projemanage.models.User::class.java)!!

                when(activity){
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity ->{
                        activity.updateNavigationUserDetails(loggedInUser)
                    }
                    is MyProfileActivity ->{
                        activity.setUserDataInUI(loggedInUser)
                    }

                }
            }.addOnFailureListener {
                e->
                when(activity){
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity ->{
                        activity.hideProgressDialog()
                    }
            }
                android.util.Log.e(Log, "Error while log in $e")
            }
    }


}