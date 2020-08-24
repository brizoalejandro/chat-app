package com.brizoalejandro.chatapp.views.message

import androidx.lifecycle.ViewModel
import com.brizoalejandro.chatapp.services.MessagesService
import org.koin.core.KoinComponent
import org.koin.core.get

class MessageViewModel: ViewModel(), KoinComponent {

    private val TAG = "[MessageViewModel]"
    private val messageService: MessagesService = get()


    fun sendMessage(receiverUid: String, message: String) {

        println("!@# uid ${receiverUid}")

        messageService.sendMessage(receiverUid, message)
            .success {

            }.fail {

            }
    }

}