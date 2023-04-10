package com.example.robotjoystick.di

import com.example.robotjoystick.view.joystick.JoystickFragment
import com.example.robotjoystick.view.knowndevices.KnownDevicesFragment
import com.example.robotjoystick.view.scandevices.ScanDevicesFragment
import com.example.robotjoystick.view.terminal.TerminalFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityProviders {
    @ContributesAndroidInjector
    abstract fun knownBluetoothDevicesFragment(): KnownDevicesFragment
    @ContributesAndroidInjector
    abstract fun scanNewDevicesFragment(): ScanDevicesFragment
    @ContributesAndroidInjector
    abstract fun joystickFragment(): JoystickFragment
    @ContributesAndroidInjector
    abstract fun terminalFragment(): TerminalFragment
}