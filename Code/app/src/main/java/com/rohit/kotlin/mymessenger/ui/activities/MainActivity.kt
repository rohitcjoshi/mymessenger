package com.rohit.kotlin.mymessenger.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rohit.kotlin.mymessenger.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->
            user = firebaseAuth.currentUser

            if(user != null) {
                // Go to dashboard
                val dashboardIntent = Intent(this, DashboardActivity::class.java)
                dashboardIntent.putExtra("display_name", user?.email)
                startActivity(dashboardIntent)
                finish()
            }
        }

        btnMainAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnMainCreateNewAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if(mAuthListener != null && mAuth != null) {
            mAuth?.addAuthStateListener(mAuthListener!!)
        }
    }

    override fun onStop() {
        super.onStop()
        if(mAuthListener != null && mAuth != null) {
            mAuth?.removeAuthStateListener(mAuthListener!!)
        }
    }
}
