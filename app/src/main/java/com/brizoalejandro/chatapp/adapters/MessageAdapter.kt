package com.brizoalejandro.chatapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.data.Chat
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.services.RepositoryService
import com.brizoalejandro.chatapp.views.message.MessageActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.chat_item_left.view.*
import kotlinx.android.synthetic.main.user_item.view.*
import org.koin.core.KoinComponent
import org.koin.core.get

class MessageAdapter(private var context: Context, chats: List<Chat>?): RecyclerView.Adapter<MessageAdapter.ViewHolder>(), KoinComponent {

    private val repo: RepositoryService = get()
    private var chatList: List<Chat>? = ArrayList()

    companion object {
        val MSG_TYPE_LEFT = 0
        val MSG_TYPE_RIGHT = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MSG_TYPE_RIGHT)
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false))
        else
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false))
    }

    override fun getItemCount(): Int {
        return chatList?.size!!
    }

    init {
        chatList = chats
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList?.get(position)

        holder.message.text = chat?.message

    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList?.get(position)?.sender == repo.user.value?.uid)
            MSG_TYPE_RIGHT
        else
            MSG_TYPE_LEFT
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var message: TextView = view.chat_message

    }

}