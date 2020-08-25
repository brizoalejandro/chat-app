package com.brizoalejandro.chatapp.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.extensions.goTo
import com.brizoalejandro.chatapp.views.main.MainActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_auth.*
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.koin.core.KoinComponent
import org.koin.core.get
import java.lang.ref.WeakReference

class AuthActivity : AppCompatActivity(), KoinComponent {

    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null

    private var emailTextLayout: TextInputLayout? = null
    private var passwordTextLayout: TextInputLayout? = null


    private var authPresenter: AuthPresenter = get()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        emailTextLayout = email_input
        passwordTextLayout = password_input
        emailInput = email_input.editText
        passwordInput = password_input.editText

        emailInput?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (emailTextLayout?.isErrorEnabled!!) {
                    emailTextLayout?.isErrorEnabled = false
                    emailTextLayout?.error = null
                }
            }

        })

        passwordInput?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (passwordTextLayout?.isErrorEnabled!!) {
                    passwordTextLayout?.isErrorEnabled = false
                    passwordTextLayout?.error = null
                }
            }

        })

        registration_btn?.let {
            it.setOnClickListener {

                if(authPresenter.areFieldsValid(emailTextLayout!!, passwordTextLayout!!)) {
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

    }

    override fun onStart() {
        super.onStart()

        if (authPresenter.isLoggedIn()) {
            this.goTo(MainActivity::class.java)
        }
    }




}
