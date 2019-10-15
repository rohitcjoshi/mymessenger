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
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.ui.fragments.LoadingDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var progressDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog = LoadingDialog(this)

        mAuth = FirebaseAuth.getInstance()

        btnLoginId.setOnClickListener {
            val email = etLoginEnterEmail.text.toString().trim()
            val password = etLoginEnterPassword.text.toString().trim()

            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                progressDialog?.showProgressDialog()
                loginUser(email, password)
            } else {
                Toast.makeText(this, R.string.toast_err_enter_all_fields, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener {
            task: Task<AuthResult> ->
            progressDialog?.dismissProgressDialog()
            if(task.isSuccessful) {
                val dashboardIntent = Intent(this, DashboardActivity::class.java)
                dashboardIntent.putExtra("display_name", mAuth?.currentUser?.email)
                startActivity(dashboardIntent)
                finish()
            } else {
                Toast.makeText(this, "Unable to login, " + task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
