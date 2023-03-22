package com.example.robotjoystick.view.scandevices

import com.example.robotjoystick.domain.bluetooth.ScanBluetoothDevicesUseCase
import com.example.robotjoystick.view.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScanNewBluetoothDevicesViewModel @Inject constructor(
    private val scanBluetoothDevices: ScanBluetoothDevicesUseCase
) : BaseViewModel<ScanNewBluetoothDevicesState, ScanNewBluetoothDevicesIntent>() {
    override val _stateFlow = MutableStateFlow(ScanNewBluetoothDevicesState())

    override suspend fun reduce(
        state: ScanNewBluetoothDevicesState,
        intent: ScanNewBluetoothDevicesIntent
    ) {
        TODO("Not yet implemented")
    }
}