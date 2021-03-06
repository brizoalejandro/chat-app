package com.brizoalejandro.chatapp.views.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import nl.komponents.kovenant.Promise
import java.lang.Exception
import java.lang.ref.WeakReference

interface AuthInterface {

    fun createUser(email: String,
                   password: String,
                   activity: WeakReference<Activity>): Promise<FirebaseUser?, Exception>

    fun isLoggedIn(): Boolean
    fun logout()

}