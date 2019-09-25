package com.rohit.kotlin.mymessenger

import android.app.Application
import okhttp3.internal.Internal.instance

class MessengerApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MessengerApplication? = null

        fun getContext(): MessengerApplication {
            return instance as MessengerApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}