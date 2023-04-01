package com.example.robotjoystick.view.joystick

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.robotjoystick.data.bluetooth.device.BluetoothDeviceData
import com.github.terrakok.cicerone.androidx.FragmentScreen

class JoystickScreen(
    private val bluetoothDeviceData: BluetoothDeviceData
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment {
        return JoystickFragment.newInstance(bluetoothDeviceData.name)
    }
}