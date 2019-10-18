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
import com.rohit.kotlin.mymessenger.ui.fragments.LoadingDialog
import com.rohit.kotlin.mymessenger.utils.*
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var progressDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        progressDialog = LoadingDialog(this)

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
        progressDialog?.showProgressDialog()
        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener {
            task: Task<AuthResult> ->
            if(task.isSuccessful) {
                val currentUser = mAuth?.currentUser
                val userId = currentUser?.uid

                mDatabase = FirebaseDatabase.getInstance().reference
                    .child(KEY_DB_USERS).child(userId!!)

                val userObject = HashMap<String, String>()
                userObject.put(KEY_INTENT_DISPLAY_NAME, displayName)
                userObject.put(KEY_INTENT_STATUS, "Hello there..!")
                userObject.put(KEY_INTENT_IMAGE, KEY_INTENT_DEFAULT_TEXT)
                userObject.put(KEY_INTENT_THUMBNAIL, KEY_INTENT_DEFAULT_TEXT)

                mDatabase!!.setValue(userObject).addOnCompleteListener {
                    task: Task<Void> ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, R.string.toast_msg_account_created, Toast.LENGTH_LONG).show()
                        progressDialog?.dismissProgressDialog()
                        val dashboardIntent = Intent(this, DashboardActivity::class.java)
                        dashboardIntent.putExtra(KEY_INTENT_DISPLAY_NAME, displayName)
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        Toast.makeText(this, "Server error. Your account NOT created..!", Toast.LENGTH_LONG).show()
                        progressDialog?.dismissProgressDialog()
                    }
                }
            } else {
                Toast.makeText(this, "Server error. Unable to create account..!", Toast.LENGTH_LONG).show()
                progressDialog?.dismissProgressDialog()
            }
        }
    }
}
