package com.rohit.kotlin.mymessenger.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rohit.kotlin.mymessenger.R
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()
        btnCreateNewAccount.setOnClickListener {
            val email = etCreateNewEnterEmail.text.toString().trim()
            val password = etCreateNewEnterPassword.text.toString().trim()
            val displayName = etCreateNewEnterDisplayName.text.toString().trim()

            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(displayName)) {
                createAccount(email, password, displayName)
            } else{
                Toast.makeText(this, R.string.toast_err_enter_all_fields, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createAccount(email: String, password: String, displayName: String) {
        progressBarCreateAcc.visibility = View.VISIBLE
        progressBarCreateAcc.bringToFront()
        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {
            task: Task<AuthResult> ->
            if(task.isSuccessful) {
                val currentUser = mAuth?.currentUser
                val userId = currentUser?.uid

                mDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users").child(userId!!)

                val userObject = HashMap<String, String>()
                userObject.put("display_name", displayName)
                userObject.put("status", "Hello there..!")
                userObject.put("image", "default")
                userObject.put("thumbnail", "default")

                mDatabase!!.setValue(userObject).addOnCompleteListener {
                    task: Task<Void> ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, R.string.toast_msg_account_created, Toast.LENGTH_LONG).show()
                        progressBarCreateAcc.visibility = View.GONE
                        val dashboardIntent = Intent(this, DashboardActivity::class.java)
                        dashboardIntent.putExtra("display_name", displayName)
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        Toast.makeText(this, "Fatal error. Your account NOT created..!", Toast.LENGTH_LONG).show()
                        progressBarCreateAcc.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(this, "Fatal error. Unable to create account..!", Toast.LENGTH_LONG).show()
                progressBarCreateAcc.visibility = View.GONE
            }
        }
    }
}
