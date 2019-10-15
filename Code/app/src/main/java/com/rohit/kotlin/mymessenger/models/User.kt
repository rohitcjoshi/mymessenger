package com.rohit.kotlin.mymessenger.models

class User() {
    var display_name: String? = null
    var status: String? = null
    var image: String? = null
    var thumbnail: String? = null

    constructor(display_name: String, image: String, thumbnail: String, status: String): this() {
        this.display_name = display_name
        this.status = status
        this.thumbnail = thumbnail
        this.image = image
    }
}