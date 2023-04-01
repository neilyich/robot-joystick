package com.example.robotjoystick

import com.example.robotjoystick.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import java.util.UUID

class RobotJoystickApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }


}