package com.example.robotjoystick.view.terminal

import androidx.lifecycle.viewModelScope
import com.example.robotjoystick.R
import com.example.robotjoystick.domain.bluetooth.AcceptedBluetoothCommunicationUseCase
import com.example.robotjoystick.view.bluetooth.BluetoothViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TerminalViewModel @Inject constructor(
    private val bluetoothCommunication: AcceptedBluetoothCommunicationUseCase,
    private val router: Router,
) : BluetoothViewModel<TerminalState, TerminalIntent>(bluetoothCommunication) {
    override val _stateFlow = MutableStateFlow(TerminalState(defaultTitleRes = R.string.waiting_for_connection))

    private val messagesLock = Any()

    override suspend fun reduce(intent: TerminalIntent) {
        when (intent) {
            TerminalIntent.BackPressed -> {
                val device = bluetoothCommunication.device
                if (device == null) {
                    router.exit()
                } else {
                    emit(state.copy(news = TerminalState.News.DisconnectDialog(
                        title = R.string.dialog_quit_and_disconnect,
                        titleArg = device.name,
                        positiveButtonText = R.string.ok,
                        negativeButtonText = R.string.cancel
                    )))
                }
            }
            is TerminalIntent.SendButtonClicked -> withPermissions {
                emit(state.copy(messages = appendMessage(TerminalState.Message(state.messages.size, "Me", intent.text))))
                val response = bluetoothCommunication.sendAndReceive(intent.text)
                emit(state.copy(messages = appendMessage(TerminalState.Message(state.messages.size + 1, bluetoothCommunication.device!!.name, response))))
            }
            TerminalIntent.Stopped -> withPermissions { bluetoothCommunication.stop() }
            TerminalIntent.NewsShown -> emit(state.copy(news = null))
            TerminalIntent.ViewCreated -> withPermissions {
                startCommunication()
            }
            TerminalIntent.DisconnectConfirmed -> withPermissions {
                emit(TerminalState(defaultTitleRes = R.string.waiting_for_connection))
                bluetoothCommunication.stop()
                startCommunication()
            }
        }
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
        val device = bluetoothCommunication.start()
        emit(state.copy(title = device.name, news = TerminalState.News.Toast(R.string.device_connected, device.name)))
    }
}