package com.example.robotjoystick.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {
    @Provides
    @Singleton
    fun provideCicerone(): Cicerone<Router> {
        return Cicerone.create()
    }

    @Provides
    fun provideRouter(cicerone: Cicerone<Router>): Router {
        return cicerone.router
    }

    @Provides
    fun provideNavigationHolder(cicerone: Cicerone<Router>): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }
}