package com.example.robotjoystick.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.robotjoystick.data.bluetooth.communicator.BluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.LineBluetoothCommunicator
import com.example.robotjoystick.data.bluetooth.communicator.factory.BluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.communicator.factory.RetryingBluetoothCommunicatorFactory
import com.example.robotjoystick.data.bluetooth.connector.BluetoothConnector
import com.example.robotjoystick.data.bluetooth.connector.factory.BluetoothConnectorFactory
import com.example.robotjoystick.data.bluetooth.connector.RetryingBluetoothConnector
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManager
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothPermissionsManagerImpl
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDevicesScanner
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDevicesScannerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [BluetoothModule.Bindings::class])
class BluetoothModule {
    @Provides
    fun provideBluetoothManager(context: Context): BluetoothManager {
        return context.getSystemService(BluetoothManager::class.java)
    }

    @Provides
    fun provideBluetoothAdapter(bluetoothManager: BluetoothManager): BluetoothAdapter {
        return bluetoothManager.adapter
    }

    @Provides
    fun provideBluetoothConnectorFactory(): BluetoothConnectorFactory {
        return object : BluetoothConnectorFactory {
            override fun create(device: BluetoothDevice): BluetoothConnector {
                return RetryingBluetoothConnector(device, 3)
            }
        }
    }

    @Provides
    fun provideBluetoothCommunicatorFactory(): BluetoothCommunicatorFactory<String, String, out BluetoothCommunicator<String, String>> {
        return RetryingBluetoothCommunicatorFactory(LineBluetoothCommunicator.Factory, 3)
    }

    @Module
    abstract class Bindings {
        @Binds
        abstract fun bindBluetoothPermissionsManager(
            bluetoothPermissionsManagerImpl: BluetoothPermissionsManagerImpl
        ): BluetoothPermissionsManager

        @Binds
        abstract fun bindBluetoothDevicesScanner(
            bluetoothDevicesScannerImpl: BluetoothDevicesScannerImpl
        ): BluetoothDevicesScanner
    }
}