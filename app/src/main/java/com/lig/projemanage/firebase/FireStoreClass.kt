package com.lig.projemanage.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.lig.projemanage.activities.SignUpActivity
import com.lig.projemanage.utils.Constants

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: com.lig.projemanage.models.User){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge()) // passing our custom model in db
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }
    }

    fun getCurrentUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }


}