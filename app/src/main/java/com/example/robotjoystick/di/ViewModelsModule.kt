package com.example.robotjoystick.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.robotjoystick.di.annotations.ViewModelKey
import com.example.robotjoystick.view.joystick.JoystickViewModel
import com.example.robotjoystick.view.knowndevices.KnownBluetoothDevicesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(KnownBluetoothDevicesViewModel::class)
    abstract fun bindKnownBluetoothDevicesViewModel(viewModel: KnownBluetoothDevicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JoystickViewModel::class)
    abstract fun bindJoystickViewModel(viewModel: JoystickViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}