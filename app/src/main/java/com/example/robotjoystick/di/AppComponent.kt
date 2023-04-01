package com.example.robotjoystick.di

import android.app.Application
import com.example.robotjoystick.RobotJoystickApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    MainActivityProviders::class,
    ActivityBuilder::class,
    ContextModule::class,
    ViewModelsModule::class,
    BluetoothModule::class,
    NavigationModule::class,
])
interface AppComponent : AndroidInjector<RobotJoystickApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}