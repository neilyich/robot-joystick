package com.example.robotjoystick.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

interface CiceroneHolder {
    fun cicerone(id: Int): Cicerone<Router>
}