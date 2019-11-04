package com.rohit.kotlin.mymessenger.ui.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.ui.fragments.LoadingDialog
import com.rohit.kotlin.mymessenger.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_status_update.view.*
import java.lang.Exception


class SettingsActivity : AppCompatActivity() {
    var databaseRef: DatabaseReference? = null
    var currentUser: FirebaseUser? = null
    var storageRef: StorageReference? = null
    val GALARY_REQ_ID = 1001
    var progressBar: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = "Settings"
        progressBar = LoadingDialog(this)

        databaseRef = FirebaseDatabase.getInstance().reference
        currentUser = FirebaseAuth.getInstance().currentUser
        storageRef = FirebaseStorage.getInstance().reference

        val userId = currentUser!!.uid

        databaseRef = FirebaseDatabase.getInstance().reference
            .child(KEY_DB_USERS)
            .child(userId)

        progressBar?.showProgressDialog()
        databaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("SettingsActivity", " Executing onDataChange() ------------->")

                val displayName = dataSnapshot.child(KEY_INTENT_DISPLAY_NAME).value
                val image = dataSnapshot.child(KEY_INTENT_IMAGE).value.toString()
                val status = dataSnapshot.child(KEY_INTENT_STATUS).value
                val thumbnail = dataSnapshot.child(KEY_INTENT_THUMBNAIL).value

                settingsDisplayNameTxt.text = displayName.toString()
                settingsStatusTxt.text = status.toString()

                if(!KEY_INTENT_DEFAULT_TEXT.equals(image)) {
                    Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.profile_img)
                        .into(settingsProfilePic, object: Callback {
                            override fun onSuccess() {
                                progressBar?.dismissProgressDialog()
                            }
                            override fun onError(e: Exception?) {
                                progressBar?.dismissProgressDialog()
                            }
                        })
                } else {
                    progressBar?.dismissProgressDialog()
                }
                Log.d("SettingsActivity", " Executing onDataChange() ------------->END")
            }

            override fun onCancelled(err: DatabaseError) {
                Log.d("SettingsActivity", " Error in loading database values: $err")
            }
        })

        settingsBtnChangeStatus.setOnClickListener {
            showStatusUpdateDialog()
        }

        settingsBtnChangePic.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALARY_REQ_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if(intent != null && requestCode == GALARY_REQ_ID && resultCode == Activity.RESULT_OK) {
            val image: Uri = intent.data!!
            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result =  CropImage.getActivityResult(intent)
            if(resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                val userId = currentUser!!.uid
                val thumbFile = File(resultUri.path!!)

                // Compress for thumbnail
                val thumbBitmap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(65)
                    .compressToBitmap(thumbFile)

                // Show progress bar while loading data
                progressBar?.showProgressDialog()

                // Upload to Firebase
                val byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
                val thumbByteArray = byteArray.toByteArray()
                val filePath = storageRef!!.child(KEY_DB_CHILD_CHAT_PROFILE_IMAGES)
                    .child(userId + ".jpg")

                // Create another directory for thumbnail images
                val thumbFilePath = storageRef!!.child(KEY_DB_CHILD_CHAT_PROFILE_IMAGES)
                    .child(KEY_STORAGE_CHILD_THUMBNAILS)
                    .child(userId + ".jpg")

                // Upload main image
                val uploadImagetask  = filePath.putFile(resultUri)
                uploadImagetask.addOnCompleteListener {
                    imageTask: Task<UploadTask.TaskSnapshot> ->
                    if(imageTask.isSuccessful) {
                        val urlTask: Task<Uri> = imageTask.result!!.storage.downloadUrl
                        urlTask.addOnCompleteListener {
                            task: Task<Uri> ->
                            if(task.isSuccessful) {
                                val downloadUrl = task.result.toString()

                                // Upload thumbnail image
                                val uploadThumbTask = thumbFilePath.putBytes(thumbByteArray)
                                uploadThumbTask.addOnCompleteListener {
                                    thumbTask: Task<UploadTask.TaskSnapshot> ->
                                    if(thumbTask.isSuccessful) {
                                        val thUrlTask = thumbTask.result!!.storage.downloadUrl
                                        thUrlTask.addOnCompleteListener {
                                            task: Task<Uri> ->
                                            if(task.isSuccessful) {
                                                val thumbnailUrl = task.result.toString()

                                                // Save the images in firebase
                                                val updateObj = HashMap<String, Any>()
                                                updateObj.put(KEY_INTENT_IMAGE, downloadUrl)
                                                updateObj.put(KEY_INTENT_THUMBNAIL, thumbnailUrl)

                                                databaseRef!!.updateChildren(updateObj)
                                                    .addOnCompleteListener {
                                                        task: Task<Void> ->
                                                        if(task.isSuccessful) {
                                                            Toast.makeText(this, "Profile image saved..!", Toast.LENGTH_LONG).show()
//                                                            progressBar?.dismissProgressDialog() // Let it cancel when data loaded again
                                                        }
                                                    }
                                            } else {
                                                Toast.makeText(this, "Profile image loading failed..!", Toast.LENGTH_LONG).show()
                                                progressBar?.dismissProgressDialog()
                                            }
                                        }
                                    } else {
                                        progressBar?.dismissProgressDialog()
                                    }
                                }
                            } else {
                                progressBar?.dismissProgressDialog()
                            }
                        }
                    } else {
                        progressBar?.dismissProgressDialog()
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, intent)
    }


    /**
     * Show dialog to change status view
     */
    private fun showStatusUpdateDialog() {
        val layout = layoutInflater.inflate(R.layout.dialog_status_update, null)
        val etStatus = layout.dialogStatusUpdateET

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Status")
        builder.setView(layout)
        etStatus.setText(settingsStatusTxt.text)
        builder.setPositiveButton(
            "Update",
            DialogInterface.OnClickListener { dialogInterface, which ->
                progressBar?.showProgressDialog()
                databaseRef!!.child(KEY_DB_CHILD_STATUS).setValue(etStatus.text.toString().trim()).addOnCompleteListener {
                    if(it.isSuccessful) {
                        Toast.makeText(this, "Status message updated..!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Status update failed!", Toast.LENGTH_LONG).show()
                    }
                    progressBar?.dismissProgressDialog()
                }
                dialogInterface.dismiss()
            })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialogInterface, which -> dialogInterface.dismiss() })

        val dialog = builder.create()
        dialog.show()
    }
}
