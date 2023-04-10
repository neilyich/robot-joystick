package com.example.robotjoystick.view.terminal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

class TerminalScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        return TerminalFragment()
    }
}