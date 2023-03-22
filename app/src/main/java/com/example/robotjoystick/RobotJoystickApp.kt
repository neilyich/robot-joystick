package com.example.robotjoystick

import android.app.Application
import com.example.robotjoystick.di.AppComponent
import com.example.robotjoystick.di.BluetoothModule
import com.example.robotjoystick.di.ContextModule
import com.example.robotjoystick.di.DaggerAppComponent
import java.util.UUID

class RobotJoystickApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
        val BLUETOOTH_UUID: UUID = UUID.fromString("a2439950-ba3a-47f2-af80-9de9db3e5b12")
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
    }


}