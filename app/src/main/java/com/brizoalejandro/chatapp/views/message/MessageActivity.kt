package com.brizoalejandro.chatapp.views.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.brizoalejandro.chatapp.R
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    private lateinit var viewModel: MessageViewModel

    var imageView: ImageView? = null
    var textView: TextView? = null
    var toolbar: Toolbar? = null
    var messageRecyclerView: RecyclerView? = null
    var messageTextView: EditText? = null
    var sendButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        viewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)

        val receiverUid = intent.getStringExtra("user_uid")

        imageView = toolbar_imageView
        textView = toolbar_textView
        toolbar = toolbar_message
        messageRecyclerView = messages_recycleView
        messageTextView = message_input.editText
        sendButton = send_btn

        sendButton?.setOnClickListener {
            if (messageTextView?.text.toString() != "") {
                viewModel.sendMessage(receiverUid!!, messageTextView?.text.toString())
            }
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Message")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { view ->
            finish()
        }



    }
}
