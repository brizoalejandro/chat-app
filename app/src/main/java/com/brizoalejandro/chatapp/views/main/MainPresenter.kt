package com.brizoalejandro.chatapp.views.main

import com.brizoalejandro.chatapp.services.AuthService
import org.koin.core.KoinComponent
import org.koin.core.get

class MainPresenter: KoinComponent {

    private val authService: AuthService = get()

    fun logout() {
        authService.logout()
    }

}