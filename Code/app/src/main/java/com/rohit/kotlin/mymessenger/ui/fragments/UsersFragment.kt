package com.rohit.kotlin.mymessenger.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rohit.kotlin.mymessenger.R
import com.rohit.kotlin.mymessenger.models.User
import com.rohit.kotlin.mymessenger.ui.adapters.UsersAdapter
import com.rohit.kotlin.mymessenger.utils.KEY_DB_USERS
import kotlinx.android.synthetic.main.fragment_users.*

/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : Fragment() {
    var userDatabaseReference: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        userDatabaseReference = FirebaseDatabase.getInstance().reference.child(KEY_DB_USERS)
        val options = FirebaseRecyclerOptions.Builder<User>()
            .setQuery(userDatabaseReference!!, User::class.java)
            .setLifecycleOwner(this)
            .build()

        usersRecyclerView.setHasFixedSize(true)
        usersRecyclerView.layoutManager = linearLayoutManager
        usersRecyclerView.adapter = UsersAdapter(context!!, options)
    }
}
