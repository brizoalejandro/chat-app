package com.brizoalejandro.chatapp.views.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.services.RepositoryService
import org.koin.core.KoinComponent
import org.koin.core.get

class MainViewModel: ViewModel(), KoinComponent {

    private val TAG = "[MainViewModel]"

    private val repoService: RepositoryService = get()
    private var configured: Boolean = false

    fun observeUser(lifecycleOwner: LifecycleOwner, observer: Observer<User?>) {
        repoService.observeUser(lifecycleOwner, observer)
    }

    fun removeUserObserver(observer: Observer<User?>) { repoService.removeUserObserver(observer) }



    val askForName: Boolean
        get() = repoService.user.value?.name.equals("anonymous", true)


    fun checkCurrentUser() {
        if (!configured) {
            repoService.observerUserFromBD()
        }
    }


}