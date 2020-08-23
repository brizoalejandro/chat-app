package com.brizoalejandro.chatapp.views.auth

import android.app.Activity
import com.brizoalejandro.chatapp.services.AuthService
import com.google.firebase.auth.FirebaseUser
import nl.komponents.kovenant.Promise
import org.koin.core.KoinComponent
import org.koin.core.get
import java.lang.Exception
import java.lang.ref.WeakReference

class AuthPresenter: AuthInterface, KoinComponent {

    private val authService: AuthService = get()


    override fun createUser(email: String,
                            password: String,
                            activity: WeakReference<Activity>): Promise<FirebaseUser?, Exception> {
        return authService.createUser(email, password, activity)
    }

    override fun isLoggedIn(): Boolean {
        return authService.isLoggedIn()
    }

    override fun logout() {
        authService.logout()
    }
}