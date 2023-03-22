package com.example.robotjoystick.view.joystick

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.robotjoystick.domain.bluetooth.BluetoothCommunicationUseCase
import com.example.robotjoystick.view.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JoystickViewModel @Inject constructor(
    private val communication: BluetoothCommunicationUseCase
) : BaseViewModel<JoystickState, JoystickIntent>() {
    override val _stateFlow = MutableStateFlow(JoystickState("...", emptyList()))

    override suspend fun reduce(state: JoystickState, intent: JoystickIntent) {
        when (intent) {
            is JoystickIntent.JoystickDirectionChanged -> {
                Log.i("THREAD", Thread.currentThread().name)
                val msg = state.messages.size.toString() + " " + communication.sendAndReceive(intent.direction.javaClass.simpleName)
                emit(state.copy(messages = state.messages + msg))
            }
            is JoystickIntent.ArgumentsReceived -> {
                emit(state.copy(deviceName = intent.deviceName))
            }
        }
    }
}