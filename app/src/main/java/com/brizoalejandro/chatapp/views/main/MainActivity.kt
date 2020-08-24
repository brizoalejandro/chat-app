package com.brizoalejandro.chatapp.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.extensions.goTo
import com.brizoalejandro.chatapp.views.auth.AuthActivity
import com.brizoalejandro.chatapp.adapters.ViewPagerAdapter
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.fragments.chats.ChatsFragment
import com.brizoalejandro.chatapp.fragments.users.UsersFragment
import com.brizoalejandro.chatapp.fragments.dialog.EnterNameFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mainPresenter = MainPresenter()
    private lateinit var viewModel: MainViewModel

    private var userObserver: Observer<User?>? = null

    private var viewPagerAdapter =
        ViewPagerAdapter(supportFragmentManager)

    private val viewPager: ViewPager?
        get() { return view_pager }

    private val tabLayout: TabLayout?
        get() { return tab_layout }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.checkCurrentUser()

        setupViewPager()
    }

    override fun onResume() {
        super.onResume()

        setupObservers()
    }

    override fun onStop() {
        super.onStop()

        userObserver?.let { viewModel.removeUserObserver(it) }
    }



    private fun setupObservers() {
        userObserver = Observer {
            if (it != null && viewModel.askForName) {
                EnterNameFragment.newInstance()?.let {
                    it.show(supportFragmentManager, "enter_name_fragment")
                }
            }
        }

        viewModel.observeUser(this, userObserver!!)
    }


    private fun setupViewPager() {
        viewPagerAdapter.addFragment(ChatsFragment(), "Chats")
        viewPagerAdapter.addFragment(UsersFragment(), "Users")

        viewPager?.adapter = viewPagerAdapter
        tabLayout?.setupWithViewPager(viewPager)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                mainPresenter.logout()
                this.goTo(AuthActivity::class.java)
            }
        }
        return false
    }


}
