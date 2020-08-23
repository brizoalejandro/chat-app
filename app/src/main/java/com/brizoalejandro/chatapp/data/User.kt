package com.brizoalejandro.chatapp.data

data class User(
    var uid: String,
    var email: String,
    var name: String) {


    fun asHashmap(): HashMap<String, String> {
        return hashMapOf(
            "email" to this.email,
            "name" to this.name
        )
    }

}
