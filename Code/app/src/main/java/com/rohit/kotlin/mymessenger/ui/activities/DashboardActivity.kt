package com.rohit.kotlin.mymessenger.ui.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.ui.adapters.SectionPageAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.title = "Dashboard"

        if(intent.extras != null) {
            val userName = intent.extras!!.get("display_name")
            Toast.makeText(this, userName.toString() + " logged in..!", Toast.LENGTH_LONG).show()
        }

        var sectionPageAdapter = SectionPageAdapter(supportFragmentManager)
        dashViewPagerId.adapter = sectionPageAdapter

        mainTabs.setupWithViewPager(dashViewPagerId)
        mainTabs.setTabTextColors(Color.WHITE, Color.GREEN)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuSettings) {
            // Go to settings page
            startActivity(Intent(this, SettingsActivity::class.java))
        } else if(item.itemId == R.id.menuLogout) {
            // Logout user
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onOptionsItemSelected(item)
        return true
    }
}
