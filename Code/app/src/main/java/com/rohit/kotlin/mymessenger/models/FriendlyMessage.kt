package com.rohit.kotlin.mymessenger.models

class FriendlyMessage() {
    var id: String? = null
    var name: String? = null
    var text: String? = null

    constructor(id:String, text: String, name:String): this() {
        this.id = id
        this.name = name
        this.text = text
    }
}