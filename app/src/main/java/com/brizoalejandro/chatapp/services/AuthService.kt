package com.brizoalejandro.chatapp.services

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import java.lang.ref.WeakReference
import android.util.Log
import com.brizoalejandro.chatapp.extensions.toUser
import com.brizoalejandro.chatapp.views.auth.AuthInterface
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.koin.core.KoinComponent
import java.lang.Exception


class AuthService(val context: Context,
                  private val repoService: RepositoryService,
                  private val firebase: FirebaseProvider): AuthInterface, KoinComponent {

    private val TAG: String = "[AuthService]"

    val currentUser: FirebaseUser?
        get() { return firebase.auth.currentUser }



    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    override fun logout() {
        if (isLoggedIn()) {
            firebase.auth.signOut()
        }
    }


    override fun createUser(email: String, password: String, activity: WeakReference<Activity>): Promise<FirebaseUser?, Exception> {

        val deferred = deferred<FirebaseUser?, Exception>()

        //AUTH
        firebase.auth.let {
            it.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity.get()!!) { result ->
                    if (result.isSuccessful) {
                        Log.d(TAG, "Auth account created")

                        repoService.saveUserOnDB()
                            .success {
                                deferred.resolve(firebase.auth.currentUser)
                            }. fail { error ->
                                deferred.reject(error)
                            }

                    }
                }
                .addOnFailureListener { error ->
                    //Handle sign in if email already exists
                    if (error is FirebaseAuthUserCollisionException) {
                        Log.d(TAG, "Account already exists. Trying to sign in.")

                        firebase.auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { result ->
                                if (result.isSuccessful) {
                                    Log.d(TAG, "Logged in!")
                                    deferred.resolve(result.result?.user)
                                } else {
                                    deferred.reject(Exception("Error while trying to sign in. Try again later."))
                                }
                            }.addOnFailureListener { e ->
                                Log.e(TAG, e.toString())
                                deferred.reject(e)
                            }
                    } else {
                        Log.e(TAG, error.toString())
                        deferred.reject(error)
                    }

                }
        }



        return deferred.promise
    }

}