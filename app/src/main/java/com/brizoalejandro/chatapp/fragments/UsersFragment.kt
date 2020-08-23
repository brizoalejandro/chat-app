package com.brizoalejandro.chatapp.fragments


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
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.viewmodels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_users.view.*

class UsersFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    var userAdapter: UserAdapter? = null

    private lateinit var viewModel: UsersViewModel

    private var usersObserver: Observer<ArrayList<User>>? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

        if (!viewModel.isConfigured) {
            viewModel.requestUsers()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        recyclerView = view.recycle_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }


    override fun onResume() {
        super.onResume()

        setupObservers()
    }

    override fun onStop() {
        super.onStop()

        usersObserver?.let { viewModel.removeUserObserver(it) }
    }


    private fun setupObservers() {
        usersObserver = Observer {
            view?.post {
                userAdapter = UserAdapter(context!!, it)
                recyclerView.adapter = userAdapter
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }

        viewModel.observeUsers(this, usersObserver!!)
    }


}
