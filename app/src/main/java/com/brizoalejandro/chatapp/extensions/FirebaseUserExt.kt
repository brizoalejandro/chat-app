package com.brizoalejandro.chatapp.extensions

import com.brizoalejandro.chatapp.data.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(name: String): User {
    return User(this.uid, this.email!!, name, "default")
}