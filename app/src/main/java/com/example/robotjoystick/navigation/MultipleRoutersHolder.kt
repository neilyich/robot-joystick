package com.example.robotjoystick.navigation

import com.github.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultipleRoutersHolder @Inject constructor(
    private val ciceroneHolder: CiceroneHolder
) : RouterHolder {

    private lateinit var _router: Router

    override val router: Router
    get() = _router

    fun updateRouter(newChainId: Int) {
        _router = ciceroneHolder.cicerone(newChainId).router
    }
}