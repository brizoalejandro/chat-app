package com.brizoalejandro.chatapp

import android.app.Application
import com.brizoalejandro.chatapp.services.AuthService
import nl.komponents.kovenant.android.startKovenant
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ChatApplication: Application() {

    private val TAG = "[Application]"


    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@ChatApplication)
            modules(appModules)
        }

        startKovenant()

    }
}

val appModules = module {
    single { AuthService(androidContext()) }
}