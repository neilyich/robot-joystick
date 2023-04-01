package com.example.robotjoystick.view.scandevices

import android.util.Log
import com.example.robotjoystick.domain.bluetooth.BluetoothCommunicationUseCase
import com.example.robotjoystick.domain.bluetooth.ScanBluetoothDevicesUseCase
import com.example.robotjoystick.view.BaseViewModel
import com.example.robotjoystick.view.joystick.JoystickScreen
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScanDevicesViewModel @Inject constructor(
    private val scanBluetoothDevices: ScanBluetoothDevicesUseCase,
    private val bluetoothCommunication: BluetoothCommunicationUseCase,
    private val router: Router,
) : BaseViewModel<ScanDevicesState, ScanDevicesIntent>() {
    override val _stateFlow = MutableStateFlow(ScanDevicesState(emptyList()))

    override suspend fun reduce(
        state: ScanDevicesState,
        intent: ScanDevicesIntent
    ) {
        when (intent) {
            ScanDevicesIntent.BackButtonClicked -> {
                Log.i("BackButtonClicked", "exiting")
                router.exit()
            }
            is ScanDevicesIntent.DeviceClicked -> {
                scanBluetoothDevices.stop()
                bluetoothCommunication.start(intent.device)
                router.navigateTo(JoystickScreen(intent.device))
            }
            ScanDevicesIntent.Resumed -> {
                val scanStarted = scanBluetoothDevices.start { updatedDevices ->
                    emit(stateFlow.value.copy(foundDevices = updatedDevices))
                }
                Log.i("STARTED", scanStarted.toString())
            }
            ScanDevicesIntent.Paused -> {
                Log.i("PAUSED", "stopping")
                scanBluetoothDevices.stop()
                Log.i("PAUSED", "stopped")
            }
        }
    }
}