package com.rohit.kotlin.mymessenger.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.utils.KEY_DB_USERS
import com.rohit.kotlin.mymessenger.utils.KEY_INTENT_USER_ID

class ProfileActivity : AppCompatActivity() {
    var currentUser: FirebaseUser? = null
    var userId: String? = null
    var userDatabaseRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
        userDatabaseRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapShot: DataSnapshot) {
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
