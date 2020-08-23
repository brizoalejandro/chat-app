package com.brizoalejandro.chatapp.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.extensions.goTo
import com.brizoalejandro.chatapp.views.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.koin.core.KoinComponent
import java.lang.ref.WeakReference

class AuthActivity : AppCompatActivity(), KoinComponent {

    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null

    private var authPresenter = AuthPresenter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        emailInput = email_input.editText
        passwordInput = password_input.editText

        registration_btn?.let {
            it.setOnClickListener {
                loading_layout?.visibility = View.VISIBLE
                authPresenter.createUser(
                    emailInput?.text.toString(),
                    passwordInput?.text.toString(),
                    WeakReference(this)
                ).successUi { user ->
                    if (user != null) {
                        this.goTo(MainActivity::class.java)
                    }
                }.failUi { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }.always {
                    loading_layout?.visibility = View.GONE
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        if (authPresenter.isLoggedIn()) {
            this.goTo(MainActivity::class.java)
        }
    }

}
