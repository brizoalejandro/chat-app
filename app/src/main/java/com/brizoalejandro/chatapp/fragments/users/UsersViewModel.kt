package com.brizoalejandro.chatapp.fragments.users

import androidx.lifecycle.*
import com.brizoalejandro.chatapp.data.User
import com.brizoalejandro.chatapp.services.AuthService
import com.brizoalejandro.chatapp.services.FirebaseProvider
import org.koin.core.KoinComponent
import org.koin.core.get


class UsersViewModel: ViewModel(), KoinComponent {

    private val firebase: FirebaseProvider = get()
    private val authService: AuthService = get()

    private var users: MutableLiveData<ArrayList<User>> = MutableLiveData()

    fun observeUsers(lifecycleOwner: LifecycleOwner, observer: Observer<ArrayList<User>>) {
        users.observe(lifecycleOwner, observer)
    }

    fun removeUserObserver(observer: Observer<ArrayList<User>>) { users.removeObserver(observer) }

    val isConfigured: Boolean
        get() = users.value != null



    fun requestUsers() {

        val ref = firebase.db.collection("users")
        ref.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
                users.value?.clear()
                val _users: ArrayList<User> = ArrayList()
                for (doc in value) {
                    val uid = doc.data["uid"] as String
                    val _user = User(
                        uid,
                        doc.data["email"] as String,
                        doc.data["name"] as String,
                        doc.data["imageUrl"] as String)
                    if (!_user.email.equals(authService.currentUser?.email, true)){
                        _users.add(_user)
                    }
                }
                users.value = _users
            } else {
                //TODO handling Error
                println("!@# ERROR")
            }
        }
    }

}