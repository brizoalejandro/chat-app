package com.brizoalejandro.chatapp.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.extensions.toUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import java.lang.Exception

class RepositoryService(val context: Context, private val firebase: FirebaseProvider) {

    private val TAG: String = "[RepositoryService]"

    var user: MutableLiveData<User?> = MutableLiveData()

    fun observeUser(lifecycleOwner: LifecycleOwner, observer: Observer<User?>) {
        user.observe(lifecycleOwner, observer)
    }

    fun removeUserObserver(observer: Observer<User?>) { user.removeObserver(observer) }



    fun observerUserFromBD() {

        val ref = firebase.db.collection(firebase.USERS_COLLECTION)
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                var _user = User(
                    snapshot.data?.get("uid") as String,
                    snapshot.data!!["email"] as String,
                    snapshot.data!!["name"] as String,
                    snapshot.data!!["imageUrl"] as String)

                user.value = _user
            } else {
            }
        }
    }

    fun saveUserOnDB(): Promise<Unit, Exception> {
        val deferred = deferred<Unit, Exception>()

        firebase.db.let { db ->
            db.collection(firebase.USERS_COLLECTION)
                .document(firebase.auth.currentUser!!.uid)
                .set(firebase.auth.currentUser?.toUser()!!.asHashmap())
                .addOnSuccessListener { ref ->
                    Log.d(TAG, "User saved")
                    deferred.resolve(Unit)
                }.addOnFailureListener { e ->
                    Log.e(TAG, e.toString())
                    deferred.reject(e)
                }
        }

        return deferred.promise
    }


    fun updateUserName(name: String): Promise<Unit, Exception>{
        val deferred = deferred<Unit, Exception>()

        firebase.db.collection(firebase.USERS_COLLECTION)
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .update("name", name)
            .addOnSuccessListener { ref ->
                Log.d(TAG, "User updated")
                deferred.resolve(Unit)
            }.addOnFailureListener { e ->
                Log.e(TAG, e.toString())
                deferred.reject(e)
            }

        return deferred.promise
    }



}