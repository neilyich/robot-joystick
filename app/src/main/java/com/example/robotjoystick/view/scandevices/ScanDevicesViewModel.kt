package com.example.robotjoystick.view.scandevices

import android.util.Log
import com.example.robotjoystick.domain.bluetooth.ClientBluetoothCommunicationUseCase
import com.example.robotjoystick.domain.bluetooth.ScanBluetoothDevicesUseCase
import com.example.robotjoystick.view.bluetooth.BluetoothViewModel
import com.example.robotjoystick.view.joystick.JoystickScreen
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScanDevicesViewModel @Inject constructor(
    private val scanBluetoothDevices: ScanBluetoothDevicesUseCase,
    private val bluetoothCommunication: ClientBluetoothCommunicationUseCase,
    private val router: Router,
) : BluetoothViewModel<ScanDevicesState, ScanDevicesIntent>(bluetoothCommunication) {
    override val _stateFlow = MutableStateFlow(ScanDevicesState(emptyList()))

    override suspend fun reduce(
        intent: ScanDevicesIntent
    ) {
        when (intent) {
            is ScanDevicesIntent.DeviceClicked -> withPermissions {
                scanBluetoothDevices.stop()
                bluetoothCommunication.start(intent.device)
                router.navigateTo(JoystickScreen(intent.device))
            }
            ScanDevicesIntent.Resumed -> withPermissions {
                Log.i("STARTING SCAN", "aboba")
                scanBluetoothDevices.start { updatedDevices ->
                    emit(state.copy(foundDevices = updatedDevices))
                }
                Log.i("STARTED", "!")
            }
            ScanDevicesIntent.Paused -> withPermissions {
                Log.i("PAUSED", "stopping")
                scanBluetoothDevices.stop()
                Log.i("PAUSED", "stopped")
            }
        }
    }

    override fun handleException(
        e: Throwable
    ) {
        super.handleException(e)
    }
}