package com.brizoalejandro.chatapp.views.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.brizoalejandro.chatapp.R
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {


    var imageView: ImageView? = null
    var textView: TextView? = null
    var toolbar: Toolbar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        imageView = toolbar_imageView
        textView = toolbar_textView
        toolbar = toolbar_message

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Message")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { view ->
            finish()
        }

        val userUid = intent.getStringExtra("user_uid")


    }
}
