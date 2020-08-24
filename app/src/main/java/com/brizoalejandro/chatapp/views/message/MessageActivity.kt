package com.brizoalejandro.chatapp.views.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.adapters.MessageAdapter
import com.brizoalejandro.chatapp.data.Chat
import kotlinx.android.synthetic.main.activity_message.*
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi

class MessageActivity : AppCompatActivity() {

    private lateinit var viewModel: MessageViewModel

    var imageView: ImageView? = null
    var textView: TextView? = null
    var toolbar: Toolbar? = null
    var messageTextView: EditText? = null
    var sendButton: Button? = null

    lateinit var recyclerView: RecyclerView
    var messageAdapter: MessageAdapter? = null

    private var chatObserver: Observer<List<Chat>>? = null


    companion object {
        val RECEIVER_NAME = "receiver_name"
        val RECEIVER_UID = "receiver_uid"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        viewModel = ViewModelProviders.of(this).get(MessageViewModel::class.java)
        viewModel.receiverUid = intent.getStringExtra(RECEIVER_UID)
        viewModel.receiverName = intent.getStringExtra(RECEIVER_NAME)

        obtainUiElements()

        sendButton?.setOnClickListener {
            if (messageTextView?.text.toString() != "") {
                viewModel.sendMessage(messageTextView?.text.toString())
                    .successUi {
                        messageTextView?.text?.clear()
                    }.failUi {
                        Toast.makeText(this, "Oops! Try try to send again", Toast.LENGTH_SHORT).show()
                    }

            }
        }

        configureRecycle()
        configureToolbar()

        viewModel.getMessages()
    }

    override fun onResume() {
        super.onResume()

        setupObservers()
    }

    override fun onStop() {
        super.onStop()

        chatObserver?.let { viewModel.removeChatsObserver(it) }
    }

    private fun setupObservers() {
        chatObserver = Observer {
            if (it != null) {
                messageAdapter = MessageAdapter(this, it)
                recyclerView.adapter = messageAdapter
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }

        viewModel.observeChats(this, chatObserver!!)
    }


    private fun configureRecycle() {
        recyclerView?.setHasFixedSize(true)
        val linearLayout = LinearLayoutManager(this)
        linearLayout.stackFromEnd = true
        recyclerView.layoutManager = linearLayout
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(viewModel.receiverName)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { view ->
            finish()
        }
    }

    private fun obtainUiElements() {
        imageView = toolbar_imageView
        textView = toolbar_textView
        toolbar = toolbar_message
        recyclerView = messages_recycleView
        messageTextView = message_input.editText
        sendButton = send_btn

    }

}
