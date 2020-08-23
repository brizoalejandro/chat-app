package com.brizoalejandro.chatapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.extensions.goTo
import com.brizoalejandro.chatapp.ui.auth.AuthActivity


class MainActivity : AppCompatActivity() {

    private val mainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
