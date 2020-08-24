package com.brizoalejandro.chatapp.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.brizoalejandro.chatapp.data.Chat
import com.brizoalejandro.chatapp.data.User
import com.google.firebase.Timestamp
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import java.lang.Exception

class MessagesService(context: Context,
                      private val repo: RepositoryService,
                      private val firebase: FirebaseProvider) {

    private val TAG: String = "[MessagesService]"


    private var chats: MutableLiveData<ArrayList<Chat>> = MutableLiveData()

    fun observeChats(lifecycleOwner: LifecycleOwner, observer: Observer<ArrayList<Chat>>) {
        chats.observe(lifecycleOwner, observer)
    }

    fun removeChatsObserver(observer: Observer<ArrayList<Chat>>) { chats.removeObserver(observer) }





    fun sendMessage(receiver: String, message: String): Promise<Unit, Exception> {
        val deferred = deferred<Unit, Exception>()

        val data = hashMapOf(
            "sender" to repo.user.value?.uid,
            "receiver" to receiver,
            "message" to message,
            "timestamp" to Timestamp.now()
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