package com.example.robotjoystick.view.knowndevices

import android.util.Log
import com.example.robotjoystick.domain.bluetooth.ClientBluetoothCommunicationUseCase
import com.example.robotjoystick.domain.bluetooth.GetKnownBluetoothDevicesUseCase
import com.example.robotjoystick.domain.bluetooth.ReceiveBluetoothDataUseCase
import com.example.robotjoystick.view.bluetooth.BluetoothViewModel
import com.example.robotjoystick.view.joystick.JoystickScreen
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class KnownDevicesViewModel @Inject constructor(
    private val getKnownBluetoothDevices: GetKnownBluetoothDevicesUseCase,
    bluetoothCommunication: ClientBluetoothCommunicationUseCase,
    private val router: Router,
    private val receiveBluetoothData: ReceiveBluetoothDataUseCase,
) : BluetoothViewModel<KnownDevicesState, KnownDevicesIntent>(bluetoothCommunication) {

    override val _stateFlow = MutableStateFlow(KnownDevicesState())

    override fun onIntent(intent: KnownDevicesIntent) {
        when (intent) {
            KnownDevicesIntent.RefreshClicked, KnownDevicesIntent.CreateView -> launchWithPermissions {
                Log.i("RefreshClicked", "start")
                emit(state.loading())
                val knownDevices = getKnownBluetoothDevices()
                Log.i("RefreshClicked", "$knownDevices")
                emit(state.ready().copy(devices = knownDevices))
            }
            is KnownDevicesIntent.DeviceClicked -> launchWithPermissions {
                Log.i("DeviceClicked", "here")
                receiveBluetoothData.start(intent.device)
                Log.i("DeviceClicked", "connected")
                router.navigateTo(JoystickScreen(intent.device))
            }
        }
    }

    override fun handleException(
        e: Throwable
    ) {
        super.handleException(e)
    }
}