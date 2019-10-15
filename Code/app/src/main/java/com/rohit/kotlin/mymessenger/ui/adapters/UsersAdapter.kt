package com.rohit.kotlin.mymessenger.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.models.User
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(databaseQuery: DatabaseReference, val context: Context) :
    FirebaseRecyclerAdapter<User, UsersAdapter.ViewHolder>(
        User::class.java,
        R.layout.row_users_layout,
        UsersAdapter.ViewHolder::class.java,
        databaseQuery
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int, model: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userNameTxt: String? = null
        var userStatusTxt: String? = null
        var userProfilePicLinkTxt: String? = null

        fun bindView(user: User) {
            val userNameTv = itemView.findViewById<TextView>(R.id.rowUserNameTV)
            val userStatusTv = itemView.findViewById<TextView>(R.id.rowUserStatusTV)
            val userThumbnailIv = itemView.findViewById<CircleImageView>(R.id.rowUserThumbnailIV)
        }
    }
}