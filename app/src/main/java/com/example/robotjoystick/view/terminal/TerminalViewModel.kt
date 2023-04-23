package com.example.robotjoystick.view.terminal

import android.util.Log
import com.example.robotjoystick.R
import com.example.robotjoystick.domain.bluetooth.AcceptedBluetoothCommunicationUseCase
import com.example.robotjoystick.view.bluetooth.BluetoothViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TerminalViewModel @Inject constructor(
    private val bluetoothCommunication: AcceptedBluetoothCommunicationUseCase,
    private val router: Router,
) : BluetoothViewModel<TerminalState, TerminalIntent>(bluetoothCommunication) {
    override val _stateFlow = MutableStateFlow(TerminalState(defaultTitleRes = R.string.waiting_for_connection))

    private val messagesLock = Any()

    override fun onIntent(intent: TerminalIntent) {
        Log.i("TerminalViewModel", "onIntent ${intent.javaClass.simpleName} start")
        when (intent) {
            TerminalIntent.BackPressed -> {
                val device = bluetoothCommunication.device
                if (device == null) {
                    router.exit()
                } else {
                    onCoroutine {
                        emit(state.copy(news = TerminalState.News.DisconnectDialog(
                            title = R.string.dialog_quit_and_disconnect,
                            titleArg = device.name,
                            positiveButtonText = R.string.ok,
                            negativeButtonText = R.string.cancel
                        )))
                    }
                }
            }
            is TerminalIntent.SendButtonClicked -> launchWithPermissions {
                emit(state.copy(messages = appendMessage(TerminalState.Message(state.messages.size, "Me", intent.text))))
                val response = bluetoothCommunication.sendAndReceive(intent.text)
                emit(state.copy(messages = appendMessage(TerminalState.Message(state.messages.size + 1, bluetoothCommunication.device!!.name, response))))
            }
            TerminalIntent.Stopped -> launchWithPermissions { bluetoothCommunication.stop() }
            TerminalIntent.NewsShown -> onCoroutine { emit(state.copy(news = null)) }
            TerminalIntent.ViewCreated -> {
                Log.i("TerminalViewModel", "viewCreated start")
                launchWithPermissions {
                    Log.i("TerminalViewModel", "viewCreated coroutine start")
                    startCommunication()
                    Log.i("TerminalViewModel", "viewCreated coroutine end")
                }
                Log.i("TerminalViewModel", "viewCreated end")
            }
            TerminalIntent.DisconnectConfirmed -> launchWithPermissions {
                emit(TerminalState(defaultTitleRes = R.string.waiting_for_connection))
                bluetoothCommunication.stop()
                startCommunication()
            }
        }
        Log.i("TerminalViewModel", "onIntent ${intent.javaClass.simpleName} end")
    }

    override fun handleException(
        e: Throwable
    ) {
        super.handleException(e)
    }

    private fun appendMessage(message: TerminalState.Message): List<TerminalState.Message> {
        return synchronized(messagesLock) {
            val curState = state
            val id = curState.messages.size
            curState.messages + message.copy(id = id)
        }
    }

    private suspend fun startCommunication() {
        Log.i("TerminalViewModel", "listening for incoming connections")
        val device = bluetoothCommunication.start()
        Log.i("TerminalViewModel", "received new connection")
        emit(state.copy(title = device.name, news = TerminalState.News.Toast(R.string.device_connected, device.name)))
    }
}