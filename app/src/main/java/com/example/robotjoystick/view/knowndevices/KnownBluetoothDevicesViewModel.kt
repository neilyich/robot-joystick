package com.example.robotjoystick.view.knowndevices

import android.util.Log
import com.example.robotjoystick.domain.bluetooth.BluetoothCommunicationUseCase
import com.example.robotjoystick.domain.bluetooth.GetKnownBluetoothDevicesUseCase
import com.example.robotjoystick.view.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class KnownBluetoothDevicesViewModel @Inject constructor() : BaseViewModel<KnownBluetoothDevicesState, KnownBluetoothDevicesIntent>() {
    @Inject
    lateinit var getKnownBluetoothDevices: GetKnownBluetoothDevicesUseCase
    @Inject
    lateinit var connectBluetoothDeviceData: BluetoothCommunicationUseCase

    override val _stateFlow = MutableStateFlow(KnownBluetoothDevicesState())

    override suspend fun reduce(state: KnownBluetoothDevicesState, intent: KnownBluetoothDevicesIntent) {
        when (intent) {
            KnownBluetoothDevicesIntent.RefreshClicked -> {
                Log.i("RefreshClicked", "start")
                emit(state.loading())
                delay(1000)
                val knownDevices = getKnownBluetoothDevices()
                Log.i("RefreshClicked", "$knownDevices")
                emit(state.ready().copy(devices = knownDevices))
            }
            KnownBluetoothDevicesIntent.ScanNewDevicesClicked -> {
                Log.i("ScanNewDevicesClicked", "here")
                connectBluetoothDeviceData.cancel()
            }
            is KnownBluetoothDevicesIntent.BluetoothDeviceClicked -> {
                Log.i("BluetoothDeviceClicked", "here")
                connectBluetoothDeviceData.start(intent.bluetoothDeviceData)
            }
        }
    }
}