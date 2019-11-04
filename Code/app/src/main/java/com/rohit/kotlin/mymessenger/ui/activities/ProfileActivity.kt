package com.rohit.kotlin.mymessenger.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.ui.fragments.LoadingDialog
import com.rohit.kotlin.mymessenger.utils.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.lang.Exception

class ProfileActivity : AppCompatActivity() {
    var currentUser: FirebaseUser? = null
    var userId: String? = null
    var userDatabaseRef: DatabaseReference? = null
    var progressBar: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressBar = LoadingDialog(this)

        if(intent.extras != null) {
            userId = intent.extras?.get(KEY_INTENT_USER_ID).toString()
            currentUser = FirebaseAuth.getInstance().currentUser
            userDatabaseRef = FirebaseDatabase.getInstance().reference
                .child(KEY_DB_USERS)
                .child(userId!!)

            setupUserProfile()
        }
    }

    private fun setupUserProfile() {
        progressBar?.showProgressDialog()
        userDatabaseRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapShot: DataSnapshot) {
                val displayName = snapShot.child(KEY_INTENT_DISPLAY_NAME).value.toString()
                val status = snapShot.child(KEY_DB_CHILD_STATUS).value.toString()
                val image = snapShot.child(KEY_INTENT_IMAGE).value.toString()

                profileDisplayNameTxt.text = displayName
                profileStatusTxt.text = status
                Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.happy_woman)
                    .into(profileImageView, object: Callback {
                        override fun onSuccess() {
                            progressBar?.dismissProgressDialog()
                        }
                        override fun onError(e: Exception?) {
                            progressBar?.dismissProgressDialog()
                        }
                    })
            }
            override fun onCancelled(error: DatabaseError) {
                progressBar?.dismissProgressDialog()
            }
        })
    }
}
