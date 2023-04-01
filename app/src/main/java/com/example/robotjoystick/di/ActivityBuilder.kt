package com.example.robotjoystick.di

import com.example.robotjoystick.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityProviders::class])
    abstract fun bindActivity(): MainActivity
}