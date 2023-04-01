package com.example.robotjoystick.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robotjoystick.di.annotations.ViewModelKey
import com.example.robotjoystick.view.joystick.JoystickViewModel
import com.example.robotjoystick.view.knowndevices.KnownDevicesViewModel
import com.example.robotjoystick.view.scandevices.ScanDevicesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(KnownDevicesViewModel::class)
    abstract fun bindKnownBluetoothDevicesViewModel(viewModel: KnownDevicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JoystickViewModel::class)
    abstract fun bindJoystickViewModel(viewModel: JoystickViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanDevicesViewModel::class)
    abstract fun bindScanNewDevicesViewModel(viewModel: ScanDevicesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}