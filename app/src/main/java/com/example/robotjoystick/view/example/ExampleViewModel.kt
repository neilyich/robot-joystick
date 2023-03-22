package com.example.robotjoystick.view.example

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.example.robotjoystick.domain.bluetooth.ScanBluetoothDevicesUseCase
import com.example.robotjoystick.view.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExampleViewModel @Inject constructor() : BaseViewModel<ExampleState, ExampleIntent>() {
    override val _stateFlow = MutableStateFlow(ExampleState(0))

    @Inject
    lateinit var scanBluetoothDevicesUseCase: ScanBluetoothDevicesUseCase

    @SuppressLint("MissingPermission")
    override suspend fun reduce(state: ExampleState, intent: ExampleIntent) {
        viewModelScope.launch {
            when (intent) {
                is ExampleIntent.Add -> emit(state.copy(value = state.value + 1))
                is ExampleIntent.Sub -> emit(state.copy(value = state.value - 1))
                is ExampleIntent.BluetoothRejected -> emit(state.copy(value = -100))
                is ExampleIntent.BluetoothPermitted -> {
                    emit(state.copy(askPermissions = emptyList()))
                    emit(state.copy(value = 100, scan = true))
//                    try {
//                        val devices = scanBluetoothDevicesUseCase.knownDevices()
//                        devices.forEach {
//                            Log.i("BLUETOOTH", "${it.name}, ${it.address}")
//                        }
//                        val started = scanBluetoothDevicesUseCase.startScan {
//                            Log.i("BLUETOOTH-NEW", "${it.name}, ${it.address}")
//                        }
//                        Log.i("STARTED", "$started")
//                        emit(state.copy(value = devices.size, scan = false))
//                    } catch (e: PermissionsDeniedException) {
//                        emit(state.copy(askPermissions = e.permissions))
//                    }
                }
            }
        }
    }
}