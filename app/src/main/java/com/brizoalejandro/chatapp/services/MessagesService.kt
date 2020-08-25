package com.brizoalejandro.chatapp.services

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.brizoalejandro.chatapp.data.Chat
import com.brizoalejandro.chatapp.data.ChatList
import com.brizoalejandro.chatapp.data.User
import com.google.firebase.Timestamp
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import java.lang.Exception

class MessagesService(context: Context,
                      private val repo: RepositoryService,
                      private val firebase: FirebaseProvider) {

    private val TAG: String = "[MessagesService]"


    private var isChatConfigured: Boolean = false
    private var chats: MutableLiveData<List<Chat>> = MutableLiveData()
    fun observeChats(lifecycleOwner: LifecycleOwner, observer: Observer<List<Chat>>) { chats.observe(lifecycleOwner, observer) }
    fun removeChatsObserver(observer: Observer<List<Chat>>) { chats.removeObserver(observer) }


    private var isChatListConfigured: Boolean = false
    private var chatList: MutableLiveData<List<ChatList>> = MutableLiveData()
    fun observeChatList(lifecycleOwner: LifecycleOwner, observer: Observer<List<ChatList>>) { chatList.observe(lifecycleOwner, observer) }
    fun removeChatListObserver(observer: Observer<List<ChatList>>) { chatList.removeObserver(observer) }

    private var usersChats: MutableLiveData<ArrayList<User>> = MutableLiveData()
    fun observeUserChats(lifecycleOwner: LifecycleOwner, observer: Observer<ArrayList<User>>) { usersChats.observe(lifecycleOwner, observer) }
    fun removeUsersChatsObserver(observer: Observer<ArrayList<User>>) { usersChats.removeObserver(observer) }



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


    fun getMessages(receiverUid: String) {

        val ref = firebase.db.collection(firebase.CHATS_COLLECTION)
        ref.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
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
                //TODO handling
            }
        }

    }


    fun addChatList(uid: String, withUid: String): Promise<Unit, Exception> {
        val deferred = deferred<Unit, Exception>()

        val timestamp = Timestamp.now()

        val data = hashMapOf(
            "uid" to uid,
            "withUid" to withUid,
            "status" to "active",
            "timestamp" to timestamp
        )

        firebase.db.let { db ->
            db.collection(firebase.CHAT_LIST_COLLECTION)
                .document("$uid-$withUid")
                .set(data)
                .addOnSuccessListener { ref ->
                    Log.d(TAG, "Chat list saved")
                    deferred.resolve(Unit)
                }.addOnFailureListener { e ->
                    Log.e(TAG, e.toString())
                    deferred.reject(e)
                }
        }

        return deferred.promise
    }



    fun getChatLists() {
        if (isChatListConfigured)
            return

        val ref = firebase.db.collection(firebase.CHAT_LIST_COLLECTION).whereEqualTo("uid", firebase.auth.currentUser?.uid)
        ref.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
                chats.value = null
                isChatListConfigured = true

                val _chatList: ArrayList<ChatList> = ArrayList()
                for (doc in value) {
                    val _chat = ChatList(
                        doc.data["uid"] as String?,
                        doc.data["withUid"] as String,
                        doc.data["status"] as String,
                        doc.data["timestamp"] as Timestamp)
                    _chatList.add(_chat)
                }
                chatList.value = _chatList.sortedWith(compareBy { it.timestamp })

                requestUsers()

            } else {
                //TODO handling Error
                println("!@# ERROR")
            }
        }

    }


    private fun requestUsers() {

        val ref = firebase.db.collection("users")
        ref.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {

                usersChats.value?.clear()

                val _users: ArrayList<User> = ArrayList()
                for (doc in value) {
                    val uid = doc.data["uid"] as String
                    val _user = User(
                        uid,
                        doc.data["email"] as String,
                        doc.data["name"] as String,
                        doc.data["imageUrl"] as String)

                    for (chat in chatList?.value!!) {

                        if (chat.withUid == _user.uid) {
                            _users.add(_user)
                        }
                    }
                }

                usersChats.value = _users

            } else {
                //TODO handling Error
                println("!@# ERROR")
            }
        }
    }


}