package com.example.robotjoystick.view.scandevices

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

    // обработка события с экрана поиска доступных устройств
    override fun onIntent(intent: ScanDevicesIntent) {
        when (intent) {
            // при нажатии на устройство в списке
            is ScanDevicesIntent.DeviceClicked -> launchWithPermissions {
                scanBluetoothDevices.stop()
                bluetoothCommunication.start(intent.device)
                router.navigateTo(JoystickScreen(intent.device))
            }
            // при отображении экрана
            ScanDevicesIntent.Resumed -> launchWithPermissions {
                scanBluetoothDevices.start { updatedDevices ->
                    emit(state.copy(foundDevices = updatedDevices))
                }
            }
            // при закрытии экрана
            ScanDevicesIntent.Paused -> launchWithPermissions {
                scanBluetoothDevices.stop()
            }
        }
    }
}