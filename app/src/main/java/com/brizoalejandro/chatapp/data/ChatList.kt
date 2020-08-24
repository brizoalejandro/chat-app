package com.brizoalejandro.chatapp.data

import com.google.firebase.Timestamp

data class ChatList(val uid: String?,
                    val withUid: String,
                    val status: String,
                    val timestamp: Timestamp)