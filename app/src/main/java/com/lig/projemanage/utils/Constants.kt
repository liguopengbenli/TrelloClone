package com.lig.projemanage.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.lig.projemanage.activities.MyProfileActivity

object Constants{
    const val  USERS: String = "users"
    const val BOARDS: String = "boards"
    const val IMAGE = "image"
    const val  NAME = "name"
    const val  MOBILE = "mobile"
    const val  ASSIGNED_TO: String = "assignedTo"
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val DOCUMENT_ID: String = "documentID"
    const val TASK_LIST: String = "taskList"
    const val BOARD_DETAILS: String = "board_details"
    const val ID:String = "id"
    const val EMAIL:String = "email"
    const val BOARD_MEMBER_LIST : String = "board_members_list"
    const val SELECT: String = "Select"
    const val UN_SELECT: String = "UnSelect"

    const val TASK_LIST_ITEM_POSITION: String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION: String = "card_list_item_position"

    const val PROJEMANAG_PREFERENCE = "ProjemanagPrefs"
    const val FCM_TOKEN_UPDATE = "fcmTokenUpdated"
    const val FCM_TOKEN = "fcmToken"


    // FCM notification
    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_SERVER_KEY:String = "AAAA1ji-45U:APA91bEMHgGzrEwPpIOkEAOiYrxNpbt_GwwHV8nvmuCI4TkXQjLyHS4YxOqrCrVeu6xmTjSyUgZB8xlpYKAj2m2n4Ev7m2Yp65GpqgA_1OZa1dt0hd_3ZU_mU7MFOzWCzAsatMZHaz4u"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"

    fun getFileExtersion(activity: Activity, uri: Uri?):String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    fun showImageChooser(activity: Activity)
    {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
}


