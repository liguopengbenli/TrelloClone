package com.lig.projemanage.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.lig.projemanage.activities.*
import com.lig.projemanage.models.Board
import com.lig.projemanage.utils.Constants


class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    private val TAG = this.javaClass.simpleName

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

    fun getBoardDetails(activity: TaskListActivity, documentId: String){
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i(TAG, document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentID = document.id
                activity.boardDetails(board)

            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(TAG, "Error while creating new board $it")
            }

    }

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                android.util.Log.e(TAG, "Board create successfully!")
                Toast.makeText(activity, "Board create successfully!", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                android.util.Log.e(TAG, "Board create failure $it!")
            }
    }

    fun getBoardList(activity: MainActivity){
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i(TAG, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for(i in document.documents){
                    val board = i.toObject(Board::class.java)!!
                    board.documentID = i.id
                    boardList.add(board)
                }
                activity.populateBoardsListToUI(boardList)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(TAG, "Error while creating new board $it")
            }
    }

    fun updateUserProfileData(activity: Activity,
                              userHashMap: HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                android.util.Log.e(TAG, "Profile update successfully!")
                when(activity){
                    is MainActivity ->{
                        activity.tokenUpdateSuccess()
                    }
                    is MyProfileActivity ->{
                        activity.profileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener {e ->
                when(activity){
                    is MainActivity ->{
                        activity.hideProgressDialog()
                    }
                    is MyProfileActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                android.util.Log.e(TAG, "Error on $e")
            }
    }
      // readBoardsList: for deciding if we reload or not
    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){
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
                        activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
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
                android.util.Log.e(TAG, "Error while log in $e")
            }
    }

    fun addUpdateTaskList(activity: Activity, board: Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList
        mFireStore.collection(Constants.BOARDS)
            .document(board.documentID)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.i(TAG, "Task List update success")
                if(activity is TaskListActivity){
                    activity.addUpdateTaskListSuccess()
                }
                else if (activity is CardDetailsActivity){
                    activity.addUpdateTaskListCardSuccess()
                }
            }
            .addOnFailureListener {
                if(activity is TaskListActivity){
                    activity.hideProgressDialog()

                }else if(activity is CardDetailsActivity){
                    activity.hideProgressDialog()
                }
                Log.e(TAG, "Task List update fail")
            }
    }

    fun getAssignedMembersListDetails(activity: Activity, assignedTo: ArrayList<String>){
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo) // to compare the filed id with assigned to
            .get() //get all the element according to condition
            .addOnSuccessListener {
               document ->
                val usersList: ArrayList<com.lig.projemanage.models.User> = ArrayList()

                for (i in document.documents){
                    val user = i.toObject(com.lig.projemanage.models.User::class.java)!!
                    usersList.add(user)
                }

                if(activity is MembersActivity){
                    activity.setupMembersList(usersList)
                }else if (activity is TaskListActivity){
                    activity.boardMemberDetailsList(usersList)
                }
            }
            .addOnFailureListener {
                if(activity is MembersActivity) {
                    activity.hideProgressDialog()
                }else if (activity is TaskListActivity){
                    activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName, "Error while creating a board $it")
            }
    }

    fun getMemberDetails(activity: MembersActivity, email: String){
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener {
                document ->
                if(document.documents.size > 0){
                    val user = document.documents[0].toObject(com.lig.projemanage.models.User::class.java)!!
                    activity.memberDetail(user)
                }else{
                    activity.hideProgressDialog()
                    activity.showErrorSnackBar("No such member found")
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(TAG, "Error while getting user details", it)
            }
    }

    fun assignMemberToBoard(activity: MembersActivity, board: Board, user: com.lig.projemanage.models.User){
        val assignedToHashMap = HashMap<String, Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo
        mFireStore.collection(Constants.BOARDS)
            .document(board.documentID)
            .update(assignedToHashMap)
            .addOnSuccessListener {
                activity.memberAssignSuccess(user)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(TAG, "Error while creating a board", it)
            }
    }


}