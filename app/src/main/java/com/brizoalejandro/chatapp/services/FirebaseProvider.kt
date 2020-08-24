package com.brizoalejandro.chatapp.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseProvider {

    val auth: FirebaseAuth
        get() { return FirebaseAuth.getInstance() }

    val db: FirebaseFirestore
        get() { return Firebase.firestore }


    val USERS_COLLECTION: String = "users"
    val CHATS_COLLECTION: String = "chats"
    val CHAT_LIST_COLLECTION: String = "chat-list"

}