package com.example.robotjoystick.view.joystick

import android.util.Log
import com.example.robotjoystick.R
import com.example.robotjoystick.domain.bluetooth.ClientBluetoothCommunicationUseCase
import com.example.robotjoystick.domain.bluetooth.ReceiveBluetoothDataUseCase
import com.example.robotjoystick.view.bluetooth.BluetoothViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class JoystickViewModel @Inject constructor(
    communication: ClientBluetoothCommunicationUseCase,
    private val router: Router,
    private val receiveBluetoothData: ReceiveBluetoothDataUseCase,
) : BluetoothViewModel<JoystickState, JoystickIntent>(communication) {
    override val _stateFlow = MutableStateFlow(JoystickState("...", emptyList()))

    override fun onIntent(intent: JoystickIntent) {
        when (intent) {
            is JoystickIntent.JoystickDirectionChanged -> launchWithPermissions {
                emit(state.copy(news = JoystickState.News.PerformHapticFeedback))
                Log.i("THREAD", Thread.currentThread().name)
                //val msg = state.messages.size.toString() + " " + communication.sendAndReceive(intent.direction.javaClass.simpleName)
                //emit(state.copy(messages = state.messages + msg))
            }
            is JoystickIntent.ArgumentsReceived -> onCoroutine {
                Log.i("Joystick", "args received")
                onCoroutine {
                    Log.i("Joystick", "subscribe")
                    receiveBluetoothData.subscribe {
                        Log.i("Joystick", "received: $it")
                        onCoroutine {
                            emit(state.copy(messages = state.messages + it))
                        }
                    }
                }
                emit(state.copy(deviceName = intent.deviceName))
            }
            JoystickIntent.BackPressed -> onCoroutine {
                emit(state.copy(news = JoystickState.News.DisconnectDialog(
                    title = R.string.dialog_quit_and_disconnect,
                    titleArg = state.deviceName,
                    positiveButtonText = R.string.ok,
                    negativeButtonText = R.string.cancel
                )))
            }
            JoystickIntent.DisconnectConfirmed -> {
                router.exit()
            }
            JoystickIntent.NewsShown -> onCoroutine { emit(state.copy(news = null)) }
            JoystickIntent.Stopped -> launchWithPermissions {
                Log.i("BackPressed", "stopping")
                receiveBluetoothData.stop()
                Log.i("BackPressed", "stopped")
            }
        }
    }

    override fun handleException(
        e: Throwable
    ) {
        super.handleException(e)
    }
}