package com.rohit.kotlin.mymessenger.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rohit.kotlin.mymessenger.ui.fragments.ChatsFragment
import com.rohit.kotlin.mymessenger.ui.fragments.UsersFragment

class SectionPageAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> {
                return UsersFragment()
            }
            1 -> {
                return ChatsFragment()
            }
        }
        return null!!
    }

    override fun getCount(): Int {
        // 2 tabs -> 2 fragments
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "USERS"
            1 -> return "CHATS"
        }
        return super.getPageTitle(position)
    }
}