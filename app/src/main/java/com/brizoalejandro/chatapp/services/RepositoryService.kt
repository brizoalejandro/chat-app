package com.brizoalejandro.chatapp.services

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepositoryService(val context: Context) {

    val firebaseDB: FirebaseFirestore
        get() { return Firebase.firestore }

}