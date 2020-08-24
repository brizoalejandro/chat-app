package com.brizoalejandro.chatapp.fragments.chats


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.adapters.UserAdapter
import com.brizoalejandro.chatapp.data.ChatList
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.fragments.users.UsersViewModel
import kotlinx.android.synthetic.main.fragment_chats.view.*

class ChatsFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    var userAdapter: UserAdapter? = null

    private var chatsUsersObserver: Observer<ArrayList<User>>? = null
    private lateinit var viewModel: ChatsViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        recyclerView = view.recycleView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ChatsViewModel::class.java)

        viewModel.getChatsList()
    }

    override fun onResume() {
        super.onResume()

        setupObservers()
    }

    override fun onStop() {
        super.onStop()

        chatsUsersObserver?.let { viewModel.removeUsersChatsObserver(it) }
    }


    private fun setupObservers() {
        chatsUsersObserver = Observer {
            view?.post {
                if (it != null) {
                    userAdapter = UserAdapter(context!!, it)
                    recyclerView.adapter = userAdapter
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }

        viewModel.observeUsersChats(this, chatsUsersObserver!!)
    }


}
