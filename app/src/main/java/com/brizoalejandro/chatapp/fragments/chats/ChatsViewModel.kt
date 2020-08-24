package com.brizoalejandro.chatapp.fragments.chats

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.brizoalejandro.chatapp.data.ChatList
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.services.MessagesService
import org.koin.core.KoinComponent
import org.koin.core.get

class ChatsViewModel: ViewModel(), KoinComponent {

    val messageService: MessagesService = get()

    fun observeChatList(lifecycleOwner: LifecycleOwner, observer: Observer<List<ChatList>>) { messageService.observeChatList(lifecycleOwner, observer) }
    fun removeChatListObserver(observer: Observer<List<ChatList>>) { messageService.removeChatListObserver(observer) }

    fun observeUsersChats(lifecycleOwner: LifecycleOwner, observer: Observer<ArrayList<User>>) { messageService.observeUserChats(lifecycleOwner, observer) }
    fun removeUsersChatsObserver(observer: Observer<ArrayList<User>>) { messageService.removeUsersChatsObserver(observer) }


    fun getChatsList() {
        messageService.getChatLists()
    }

}