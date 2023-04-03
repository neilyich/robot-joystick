package com.example.robotjoystick.view.joystick

import android.util.Log
import com.example.robotjoystick.R
import com.example.robotjoystick.domain.bluetooth.BluetoothCommunicationUseCase
import com.example.robotjoystick.view.BaseViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class JoystickViewModel @Inject constructor(
    private val communication: BluetoothCommunicationUseCase,
    private val router: Router,
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
                Log.i("Joystick", "args received")
                emit(state.copy(deviceName = intent.deviceName))
            }
            JoystickIntent.BackPressed -> {
                emit(state.copy(news = JoystickState.News.ShowQuitDialog(
                    title = R.string.dialog_quit_and_disconnect,
                    titleArg = state.deviceName,
                    positiveButtonText = R.string.ok,
                    negativeButtonText = R.string.cancel
                )))
            }
            JoystickIntent.QuitConfirmed -> {
                router.exit()
            }
            JoystickIntent.NewsShown -> emit(state.copy(news = null))
            JoystickIntent.Stopped -> {
                Log.i("BackPressed", "stopping")
                communication.stop()
                Log.i("BackPressed", "stopped")
            }
        }
    }
}