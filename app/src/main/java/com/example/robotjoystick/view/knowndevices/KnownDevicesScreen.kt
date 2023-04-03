package com.example.robotjoystick.view.knowndevices

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

class KnownDevicesScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        return KnownDevicesFragment()
    }
}