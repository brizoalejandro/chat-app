package com.brizoalejandro.chatapp.services

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import java.lang.ref.WeakReference
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.koin.core.KoinComponent
import java.lang.Exception


class AuthService(val context: Context): KoinComponent {

    private val TAG = "[AUTH]"

    var firebaseAuth: FirebaseAuth? = null

    val currentUser: FirebaseUser?
        get() { return firebaseAuth?.currentUser }


    init {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun createUser(name: String?, email: String?, password: String?, activity: WeakReference<Activity>): Promise<FirebaseUser?, Exception> {

        val deferred = deferred<FirebaseUser?, Exception>()
        

        firebaseAuth?.let {
            it.createUserWithEmailAndPassword(email ?: "", password?: "")
                .addOnCompleteListener(activity.get()!!) { result ->
                    if (result.isSuccessful) {
                        Log.d(TAG, "Authentication Success")

                        deferred.resolve(firebaseAuth?.currentUser)
                    }
                }
                .addOnFailureListener { error ->
                    Log.e(TAG, error.toString())
                    deferred.reject(error)
                }
        }

        return deferred.promise
    }

}