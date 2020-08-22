package com.brizoalejandro.chatapp.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.brizoalejandro.chatapp.R
import kotlinx.android.synthetic.main.activity_auth.*
import java.lang.ref.WeakReference

class AuthActivity : AppCompatActivity() {

    private var nameInput: EditText? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null

    private var authPresenter = AuthPresenter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        nameInput = name_input.editText
        emailInput = email_input.editText
        passwordInput = password_input.editText

        registration_btn?.let {
            it.setOnClickListener {
                authPresenter.createUser(
                    nameInput.toString(),
                    emailInput.toString(),
                    passwordInput.toString(),
                    WeakReference(this)
                )
            }
        }

    }


}
