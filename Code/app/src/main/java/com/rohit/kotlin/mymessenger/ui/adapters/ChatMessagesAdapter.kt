package com.rohit.kotlin.mymessenger.ui.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.models.FriendlyMessage
import com.rohit.kotlin.mymessenger.utils.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatMessagesAdapter(
    val context: Context,
    val options: FirebaseRecyclerOptions<FriendlyMessage>) : FirebaseRecyclerAdapter<FriendlyMessage, ChatMessagesAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_item_message,
                null
            )
        )
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
        model: FriendlyMessage
    ) {
        if(model.text != null) {
            holder.bindView(model)
            val databaseReference = FirebaseDatabase.getInstance().reference
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            val isMe: Boolean = currentUserId.equals(model.id)
            if(isMe) {
                holder.profileUserImgView?.visibility = View.VISIBLE
                holder.profileImgView?.visibility = View.GONE
                holder.messagesContainer?.gravity = Gravity.END

                // Get image URL for ME
                databaseReference.child(KEY_DB_USERS).child(currentUserId!!)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(context, "Unable to load data..!", Toast.LENGTH_SHORT).show()
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val thumbUrl = snapshot.child(KEY_DB_THUMBNAIL).value.toString()
                            val displayName = snapshot.child(KEY_DB_DISPLAY_NAME).value.toString()
                            holder.senderNameTV?.text = "I wrote..."
                            Picasso.get()
                                .load(thumbUrl)
                                .placeholder(R.drawable.profile_img)
                                .into(holder.profileUserImgView)
                        }

                    })
            } else {
                holder.profileUserImgView?.visibility = View.GONE
                holder.profileImgView?.visibility = View.VISIBLE
                holder.messagesContainer?.gravity = Gravity.START

                // Get image URL for ME
                databaseReference.child(KEY_DB_USERS).child(model.id!!)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(context, "Unable to load data..!", Toast.LENGTH_SHORT).show()
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val thumbUrl = snapshot.child(KEY_DB_THUMBNAIL).value.toString()
                            val displayName = snapshot.child(KEY_DB_DISPLAY_NAME).value.toString()
                            holder.senderNameTV?.text = "$displayName wrote..."
                            Picasso.get()
                                .load(thumbUrl)
                                .placeholder(R.drawable.profile_img)
                                .into(holder.profileImgView)
                        }

                    })
            }
        }
    }


    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messagesContainer: LinearLayout? = null
        var messageTV: TextView? = null
        var senderNameTV: TextView? = null
        var profileImgView: CircleImageView? = null
        var profileUserImgView: CircleImageView? = null

        fun bindView(friendlyMessage: FriendlyMessage) {
            messagesContainer = itemView.findViewById(R.id.rowItemMessageContainer)
            messageTV = itemView.findViewById(R.id.rowItemMessageText)
            senderNameTV = itemView.findViewById(R.id.rowItemMessageSenderName)
            profileImgView = itemView.findViewById(R.id.rowItemMessageProfileIV)
            profileUserImgView = itemView.findViewById(R.id.rowItemMessageUserProfileIV)

            messageTV?.text = friendlyMessage.text
            senderNameTV?.text = friendlyMessage.name

        }
    }
}