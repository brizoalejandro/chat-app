package com.brizoalejandro.chatapp.views.auth

import android.app.Activity
import android.content.Context
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.services.AuthService
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
import nl.komponents.kovenant.Promise
import java.lang.Exception
import java.lang.ref.WeakReference

class AuthPresenter(val context: Context, val authService: AuthService): AuthInterface {


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


    fun areFieldsValid(email: TextInputLayout, password: TextInputLayout): Boolean {
        var errors = 0

        if (email.editText?.text.toString() == "") {
            email.isErrorEnabled = true
            email.error = context.getString(R.string.error_field_required)
            errors++
        }
        if (password.editText?.text.toString() == "") {
            password.isErrorEnabled = true
            password.error = context.getString(R.string.error_field_required)
            errors++
        }

        return errors == 0
    }

}