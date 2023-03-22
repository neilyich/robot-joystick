package com.example.robotjoystick.di

import com.example.robotjoystick.view.example.ExampleFragment
import com.example.robotjoystick.view.joystick.JoystickFragment
import com.example.robotjoystick.view.knowndevices.KnownBluetoothDevicesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, ViewModelsModule::class, BluetoothModule::class])
interface AppComponent {
    fun inject(fragment: ExampleFragment)
    fun inject(fragment: KnownBluetoothDevicesFragment)
    fun inject(fragment: JoystickFragment)
}