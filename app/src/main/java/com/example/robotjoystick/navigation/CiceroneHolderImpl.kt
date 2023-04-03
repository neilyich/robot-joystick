package com.example.robotjoystick.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CiceroneHolderImpl @Inject constructor(): CiceroneHolder {

    private val cicerones = mutableMapOf<Int, Cicerone<Router>>()

    override fun cicerone(id: Int): Cicerone<Router> {
        return cicerones.getOrPut(id) {
            Cicerone.create()
        }
    }
}