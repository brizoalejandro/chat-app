package com.brizoalejandro.chatapp.services

import android.content.Context
import android.util.Log
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import java.lang.Exception

class MessagesService(context: Context,
                      private val repo: RepositoryService,
                      private val firebase: FirebaseProvider) {

    private val TAG: String = "[MessagesService]"


    fun sendMessage(receiver: String, message: String): Promise<Unit, Exception> {
        val deferred = deferred<Unit, Exception>()

        val data = hashMapOf(
            "sender" to repo.user.value?.uid,
            "receiver" to receiver,
            "message" to message
        )

        firebase.db.let { db ->
            db.collection(firebase.CHATS_COLLECTION)
                .add(data)
                .addOnSuccessListener { ref ->
                    Log.d(TAG, "Message saved")
                    deferred.resolve(Unit)
                }.addOnFailureListener { e ->
                    Log.e(TAG, e.toString())
                    deferred.reject(e)
                }
        }

        return deferred.promise
    }

}