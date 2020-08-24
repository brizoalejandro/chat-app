package com.brizoalejandro.chatapp.views.message

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.brizoalejandro.chatapp.data.Chat
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.services.FirebaseProvider
import com.brizoalejandro.chatapp.services.MessagesService
import com.brizoalejandro.chatapp.services.RepositoryService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.koin.core.KoinComponent
import org.koin.core.get
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MessageViewModel: ViewModel(), KoinComponent {

    private val TAG = "[MessageViewModel]"

    private val messageService: MessagesService = get()
    private val firebase: FirebaseProvider = get()
    private val repo: RepositoryService = get()

    private var chats: MutableLiveData<List<Chat>> = MutableLiveData()

    fun observeChats(lifecycleOwner: LifecycleOwner, observer: Observer<List<Chat>>) {
        chats.observe(lifecycleOwner, observer)
    }

    fun removeChatsObserver(observer: Observer<List<Chat>>) { chats.removeObserver(observer) }

    var isConfigured: Boolean = false

    var receiverUid: String? = null
    var receiverName: String? = null



    fun getMessages() {
        if (isConfigured)
            return


        val ref = firebase.db.collection("chats")
        ref.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
                isConfigured = true
                chats.value = null

                val _chats: ArrayList<Chat> = ArrayList()
                for (doc in value) {
                    val _chat = Chat(
                        doc.data["sender"] as String,
                        doc.data["receiver"] as String,
                        doc.data["message"] as String,
                        doc.data["timestamp"] as Timestamp)
                    if ((_chat.receiver == repo.user.value?.uid && _chat.sender == receiverUid)
                        ||_chat.receiver == receiverUid && _chat.sender == repo.user.value?.uid)
                    {
                        _chats.add(_chat)
                    }
                }
                chats.value = _chats.sortedWith(compareBy { it.timestamp })
            } else {
                //TODO handling Error
                println("!@# ERROR")
            }
        }

    }

    fun sendMessage(message: String): Promise<Unit, Exception> {
        val deferred = deferred<Unit, Exception>()

        println("!@# uid ${receiverUid}")

        messageService.sendMessage(receiverUid!!, message)
            .success {
                deferred.resolve(Unit)
            }.fail {
                deferred.reject(it)
            }

        return deferred.promise
    }

}