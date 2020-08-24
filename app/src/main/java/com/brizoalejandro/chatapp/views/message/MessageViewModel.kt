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
import nl.komponents.kovenant.then
import org.koin.core.KoinComponent
import org.koin.core.get
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MessageViewModel: ViewModel(), KoinComponent {

    private val TAG = "[MessageViewModel]"

    private val messageService: MessagesService = get()
    private val repo: RepositoryService = get()


    fun observeChats(lifecycleOwner: LifecycleOwner, observer: Observer<List<Chat>>) {
        messageService.observeChats(lifecycleOwner, observer)
    }

    fun removeChatsObserver(observer: Observer<List<Chat>>) { messageService.removeChatsObserver(observer) }

    var isConfigured: Boolean = false

    var receiverUid: String? = null
    var receiverName: String? = null



    fun getMessages() {
        if (isConfigured)
            return

        messageService.getMessages(receiverUid!!)
        isConfigured = true
    }

    fun sendMessage(message: String): Promise<Unit, Exception> {
        val deferred = deferred<Unit, Exception>()

        println("!@# uid ${receiverUid}")

        messageService.sendMessage(receiverUid!!, message)
            .then {
                messageService.addChatList(repo.user.value?.uid!!, receiverUid!!)
            }.then {
                messageService.addChatList(receiverUid!!, repo.user.value?.uid!!)
            }.success {
                deferred.resolve(Unit)
            }.fail {
                deferred.reject(it)
            }

        return deferred.promise
    }

}