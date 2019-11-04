package com.rohit.kotlin.mymessenger.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.models.FriendlyMessage
import com.rohit.kotlin.mymessenger.ui.adapters.ChatMessagesAdapter
import com.rohit.kotlin.mymessenger.utils.KEY_DB_MESSAGES
import com.rohit.kotlin.mymessenger.utils.KEY_INTENT_NAME
import com.rohit.kotlin.mymessenger.utils.KEY_INTENT_USER_ID
import kotlinx.android.synthetic.main.activity_chats.*

class ChatsActivity : AppCompatActivity() {

    var userId: String? = null
    var databaseReference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    var layoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        supportActionBar!!.title = "Messages"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().reference

        userId = intent.extras?.getString(KEY_INTENT_USER_ID)
        layoutManager = LinearLayoutManager(this)
        layoutManager?.stackFromEnd = true

        val options = FirebaseRecyclerOptions.Builder<FriendlyMessage>()
            .setQuery(databaseReference!!.child(KEY_DB_MESSAGES), FriendlyMessage::class.java)
            .setLifecycleOwner(this)
            .build()

        chatsRecyclerView.layoutManager = layoutManager
        chatsRecyclerView.adapter = ChatMessagesAdapter(this, options)

        chatsSendMessageBtn.setOnClickListener {
            val currentUserName = intent.extras?.get(KEY_INTENT_NAME).toString()
            if(!TextUtils.isEmpty(currentUserName)) {
                val friendlyMessage = FriendlyMessage(firebaseUser!!.uid,
                                chatsMessageInputText.text.toString().trim(), currentUserName)

                databaseReference!!.child(KEY_DB_MESSAGES).push().setValue(friendlyMessage)
                chatsMessageInputText.setText("")
            }
        }
    }
}
