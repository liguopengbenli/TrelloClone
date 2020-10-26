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


