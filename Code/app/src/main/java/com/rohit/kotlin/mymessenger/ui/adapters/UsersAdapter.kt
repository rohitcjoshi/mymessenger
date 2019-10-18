package com.rohit.kotlin.mymessenger.ui.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.models.User
import com.rohit.kotlin.mymessenger.ui.activities.ChatsActivity
import com.rohit.kotlin.mymessenger.ui.activities.ProfileActivity
import com.rohit.kotlin.mymessenger.utils.KEY_INTENT_NAME
import com.rohit.kotlin.mymessenger.utils.KEY_INTENT_PROFILE
import com.rohit.kotlin.mymessenger.utils.KEY_INTENT_STATUS
import com.rohit.kotlin.mymessenger.utils.KEY_INTENT_USER_ID
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(val context: Context, val options: FirebaseRecyclerOptions<User>) :
    FirebaseRecyclerAdapter<User, UsersAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_users_layout, parent))
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int, model: User) {
        val userId = getRef(position).key
        holder.bindView(model)

        holder.itemView.setOnClickListener {
            // Create a dialog to choose 1. Send message, 2. View Profile
            val options = arrayOf("Open Profile", "Send Message")

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Select")
            builder.setItems(options, DialogInterface.OnClickListener { dialogInterface, i ->
                val name = holder.userNameTxt
                val status = holder.userStatusTxt
                val profilePicLink = holder.userProfilePicLink

                if(i == 0) { // Open Profile
                    val profileIntent = Intent(context, ProfileActivity::class.java)
                    profileIntent.putExtra(KEY_INTENT_USER_ID, userId)
                    context.startActivity(profileIntent)
                } else if(i == 1) { // Send Message
                    val chatIntent = Intent(context, ChatsActivity::class.java)
                    chatIntent.putExtra(KEY_INTENT_USER_ID, userId)
                    chatIntent.putExtra(KEY_INTENT_NAME, name)
                    chatIntent.putExtra(KEY_INTENT_STATUS, status)
                    chatIntent.putExtra(KEY_INTENT_PROFILE, profilePicLink)
                    context.startActivity(chatIntent)
                }
            })
            builder.create().show()
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userNameTxt: String? = null
        var userStatusTxt: String? = null
        var userProfilePicLink: String? = null

        fun bindView(user: User) {
            val userNameTv = itemView.findViewById<TextView>(R.id.rowUserNameTV)
            val userStatusTv = itemView.findViewById<TextView>(R.id.rowUserStatusTV)
            val userThumbnailIv = itemView.findViewById<CircleImageView>(R.id.rowUserThumbnailIV)

            // Set data to pass in intent
            userNameTxt = user.display_name
            userStatusTxt = user.status
            userProfilePicLink = user.thumbnail

            // Populate view
            userNameTv.text = user.display_name
            userStatusTv.text = user.status
            Picasso.get()
                .load(userProfilePicLink)
                .placeholder(R.drawable.profile_img)
                .into(userThumbnailIv)
        }
    }
}