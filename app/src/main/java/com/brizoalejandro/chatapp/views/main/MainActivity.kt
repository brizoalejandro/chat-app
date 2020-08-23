package com.brizoalejandro.chatapp.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.extensions.goTo
import com.brizoalejandro.chatapp.views.auth.AuthActivity
import com.brizoalejandro.chatapp.adapters.ViewPagerAdapter
import com.brizoalejandro.chatapp.fragments.ChatsFragment
import com.brizoalejandro.chatapp.fragments.UsersFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mainPresenter = MainPresenter()

    private var viewPagerAdapter =
        ViewPagerAdapter(supportFragmentManager)

    private val viewPager: ViewPager?
        get() { return view_pager }

    private val tabLayout: TabLayout?
        get() { return tab_layout }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager()
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
