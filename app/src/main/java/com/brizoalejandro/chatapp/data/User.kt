package com.brizoalejandro.chatapp.data

data class User(
    var uid: String,
    var email: String,
    var name: String,
    var imageUrl: String) {


    fun asHashmap(): HashMap<String, String> {
        return hashMapOf(
            "uid" to this.uid,
            "email" to this.email,
            "name" to this.name,
            "imageUrl" to this.imageUrl
        )
    }

}
