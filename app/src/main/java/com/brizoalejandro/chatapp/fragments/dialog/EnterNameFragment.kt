package com.brizoalejandro.chatapp.fragments.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.brizoalejandro.chatapp.R
import com.brizoalejandro.chatapp.services.RepositoryService
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enter_name, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.name_input.editText
        nameInput?.requestFocus()

        confirm_btn.setOnClickListener {
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
