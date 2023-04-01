package com.example.robotjoystick.view.knowndevices

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

class KnownDevicesScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        Log.i("CREATE", javaClass.simpleName)
        return KnownDevicesFragment()
    }
}