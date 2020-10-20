package com.lig.projemanage.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.lig.projemanage.activities.SignInActivity
import com.lig.projemanage.activities.SignUpActivity
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

    fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(com.lig.projemanage.models.User::class.java)!!
                activity.signInSuccess(loggedInUser)
            }.addOnFailureListener {
                e->
                android.util.Log.e(Log, "Error while log in $e")
            }
    }


}