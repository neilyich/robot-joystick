package com.example.robotjoystick.view.scandevices

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

class ScanDevicesScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        return ScanDevicesFragment()
    }
}