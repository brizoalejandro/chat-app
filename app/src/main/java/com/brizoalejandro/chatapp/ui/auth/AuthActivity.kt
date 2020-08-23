package com.brizoalejandro.chatapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.services.AuthService
import com.brizoalejandro.chatapp.ui.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.koin.core.KoinComponent
import org.koin.core.get
import java.lang.ref.WeakReference

class AuthActivity : AppCompatActivity(), KoinComponent {

    private var nameInput: EditText? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null

    private var authPresenter = AuthPresenter()
    private val authService: AuthService = get()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        nameInput = name_input.editText
        emailInput = email_input.editText
        passwordInput = password_input.editText

        registration_btn?.let {
            it.setOnClickListener {
                loading_layout?.visibility = View.VISIBLE
                authPresenter.createUser(
                    nameInput?.text.toString(),
                    emailInput?.text.toString(),
                    passwordInput?.text.toString(),
                    WeakReference(this)
                ).successUi { user ->
                    if (user != null)
                        goToHome()
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

        if (authService.isLoggedIn()) {
            goToHome()
        }
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}
