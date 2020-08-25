package com.brizoalejandro.chatapp.fragments.dialog


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.services.RepositoryService
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_enter_name.*
import kotlinx.android.synthetic.main.fragment_enter_name.view.*
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.koin.core.KoinComponent
import org.koin.core.get

class EnterNameFragment : DialogFragment(), KoinComponent {

    val repositoryService: RepositoryService = get()

    fun EnterNameFragment(){}

    companion object {
        var instanciate: Boolean = false

        fun newInstance(): EnterNameFragment? {
            if (!instanciate) {
                val frag = EnterNameFragment()
                frag.isCancelable = false
                instanciate = true
                return frag
            }
            return null
        }
    }

    private var nameInput: EditText? = null
    private var nameInputLayout: TextInputLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enter_name, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInputLayout = view.name_input
        nameInput = view.name_input.editText
        nameInput?.requestFocus()

        view.name_input.editText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (nameInputLayout?.isErrorEnabled!!) {
                    nameInputLayout?.isErrorEnabled = false
                    nameInputLayout?.error = null
                }
            }

        })


        confirm_btn.setOnClickListener {
            if (nameInputLayout?.editText?.text.toString() == "") {
                nameInputLayout?.isErrorEnabled = true
                nameInputLayout?.error = getString(R.string.error_field_required)
            } else {
                repositoryService.updateUserName(nameInput?.text.toString())
                    .successUi {
                        instanciate = false
                        dismiss()
                    }.failUi {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }
}
