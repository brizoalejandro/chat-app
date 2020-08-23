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
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.views.message.MessageActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(private var context: Context, listUsers: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var users: ArrayList<User> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    init {
        users = listUsers
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]

        holder.name.text = user.name
        holder.email.text = user.email

        if (user.imageUrl.equals("default")) {

        } else {
            Glide.with(context)
                .load(user.imageUrl)
                .into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("user_uid", user.uid)
            context.startActivity(intent)
        }
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var name: TextView = view.name_text
        var email: TextView = view.email_text
        var imageView: ImageView = view.imageView

    }

}