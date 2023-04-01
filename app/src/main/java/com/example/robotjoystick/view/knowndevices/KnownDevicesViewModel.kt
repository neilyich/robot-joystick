package com.example.robotjoystick.view.knowndevices

import android.util.Log
import com.example.robotjoystick.domain.bluetooth.BluetoothCommunicationUseCase
import com.example.robotjoystick.domain.bluetooth.GetKnownBluetoothDevicesUseCase
import com.example.robotjoystick.view.BaseViewModel
import com.example.robotjoystick.view.joystick.JoystickScreen
import com.example.robotjoystick.view.scandevices.ScanDevicesScreen
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class KnownDevicesViewModel @Inject constructor(
    private val getKnownBluetoothDevices: GetKnownBluetoothDevicesUseCase,
    private val bluetoothCommunication: BluetoothCommunicationUseCase,
    private val router: Router,
) : BaseViewModel<KnownDevicesState, KnownDevicesIntent>() {

    override val _stateFlow = MutableStateFlow(KnownDevicesState())

    override suspend fun reduce(state: KnownDevicesState, intent: KnownDevicesIntent) {
        when (intent) {
            KnownDevicesIntent.RefreshClicked, KnownDevicesIntent.CreateView -> {
                Log.i("RefreshClicked", "start")
                emit(state.loading())
                delay(1000)
                val knownDevices = getKnownBluetoothDevices()
                Log.i("RefreshClicked", "$knownDevices")
                emit(state.ready().copy(devices = knownDevices))
            }
            KnownDevicesIntent.ScanNewDevicesClicked -> {
                Log.i("ScanNewDevicesClicked", "here")
                router.navigateTo(ScanDevicesScreen())
                //emit(state.copy(navigateTo = KnownDevicesState.Destination.ScanNewDevices))
            }
            is KnownDevicesIntent.DeviceClicked -> {
                Log.i("DeviceClicked", "here")
                bluetoothCommunication.start(intent.device)
                Log.i("DeviceClicked", "connected")
                router.navigateTo(JoystickScreen(intent.device))
                //emit(state.copy(navigateTo = KnownDevicesState.Destination.Joystick(intent.device)))
            }
        }
    }
}