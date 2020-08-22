package com.brizoalejandro.chatapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.ui.auth.AuthPresenter
import kotlinx.android.synthetic.main.activity_auth.*
import nl.komponents.kovenant.ui.failUi
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {


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
                    nameInput?.text.toString(),
                    emailInput?.text.toString(),
                    passwordInput?.text.toString(),
                    WeakReference(this)
                ).success {

                }.failUi { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}
