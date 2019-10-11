package com.rohit.kotlin.mymessenger.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.rohit.kotlin.mymessenger.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    var databaseRef: DatabaseReference? = null
    var currentUser: FirebaseUser? = null
    var storageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = "Settings"

        databaseRef = FirebaseDatabase.getInstance().reference
        currentUser = FirebaseAuth.getInstance().currentUser

        val userId = currentUser!!.uid

        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userId)

        databaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val displayName = dataSnapshot.child("display_name").value
                val image = dataSnapshot.child("image").value
                val status = dataSnapshot.child("status").value
                val thumbnail = dataSnapshot.child("thumbnail").value

                settingsDisplayNameTxt.text = displayName.toString()
                settingsStatusTxt.text = status.toString()
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
