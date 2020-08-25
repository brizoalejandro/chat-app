package com.brizoalejandro.chatapp.extensions

import android.app.Activity
import android.content.Intent

fun Activity.goTo(cls: Class<*> ) {
    val intent = Intent(this, cls)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    this.startActivity(intent)
    this.finish()
}