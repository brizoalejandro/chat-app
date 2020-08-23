package com.brizoalejandro.chatapp.services

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import java.lang.ref.WeakReference
import android.util.Log
import com.brizoalejandro.chatapp.extensions.toUser
import com.brizoalejandro.chatapp.ui.auth.AuthInterface
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.koin.core.KoinComponent
import java.lang.Exception


class AuthService(val context: Context): AuthInterface, KoinComponent {

    private val TAG = "[AUTH]"

    val firebaseAuth: FirebaseAuth
        get() { return FirebaseAuth.getInstance() }

    val firebaseDB: FirebaseFirestore
        get() { return  Firebase.firestore }

    val currentUser: FirebaseUser?
        get() { return firebaseAuth.currentUser }



    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    override fun logout() {
        if (isLoggedIn()) {
            firebaseAuth.signOut()
        }
    }


    override fun createUser(name: String, email: String, password: String, activity: WeakReference<Activity>): Promise<FirebaseUser?, Exception> {

        val deferred = deferred<FirebaseUser?, Exception>()

        //AUTH
        firebaseAuth?.let {
            it.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity.get()!!) { result ->
                    if (result.isSuccessful) {
                        Log.d(TAG, "Auth Success")

                        //SAVE user
                        firebaseDB?.let { db ->
                            db.collection("users")
                                .document(it.currentUser!!.uid)
                                .set(it.currentUser?.toUser(name)!!.asHashmap())
                                .addOnSuccessListener { ref ->
                                    Log.d(TAG, "User saved")
                                    deferred.resolve(firebaseAuth?.currentUser)
                                }.addOnFailureListener { e ->
                                    Log.e(TAG + "DB error", e.toString())
                                    deferred.reject(e)
                                }
                        }

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