package com.brizoalejandro.chatapp.data

import com.google.firebase.Timestamp


data class Chat(
    val sender: String,
    val receiver: String,
    val message: String,
    val timestamp: Timestamp)