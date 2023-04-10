package com.example.robotjoystick.view.bluetooth

import android.app.Activity
import androidx.lifecycle.viewModelScope
import com.example.robotjoystick.R
import com.example.robotjoystick.data.bluetooth.BluetoothCommunicationException
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothDisabledException
import com.example.robotjoystick.data.bluetooth.permissions.PermissionsDeniedException
import com.example.robotjoystick.domain.bluetooth.BluetoothCommunicationUseCase
import com.example.robotjoystick.view.BaseViewModel
import com.example.robotjoystick.view.Intent
import com.example.robotjoystick.view.State
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class BluetoothViewModel<S: State, I: Intent>(
    private val bluetoothCommunication: BluetoothCommunicationUseCase<*, *>?,
) : BaseViewModel<S, I>() {

    private val actions = MutableSharedFlow<BluetoothAction>(extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val actionsFlow: Flow<BluetoothAction> = actions

    override fun handleException(e: Throwable) {
        super.handleException(e)
        when (e) {
            is BluetoothCommunicationException -> handleBluetoothCommunicationException(e)
        }
    }

    protected open fun handleBluetoothCommunicationException(e: BluetoothCommunicationException) {
        val error = when (e.type) {
            BluetoothCommunicationException.Type.CONNECT -> BluetoothAction.BluetoothError(R.string.could_not_connect)
            BluetoothCommunicationException.Type.SEND -> BluetoothAction.BluetoothError(R.string.could_not_send)
            BluetoothCommunicationException.Type.RECEIVE -> BluetoothAction.BluetoothError(R.string.could_not_receive)
        }
        viewModelScope.launch {
            bluetoothCommunication?.stop()
            actions.emit(error)
        }
    }

    protected suspend fun <R> withPermissions(action: suspend () -> R): R {
        return try {
            action()
        } catch (denied: PermissionsDeniedException) {
            retryWithPermissions(denied.permissions, action) ?: throw denied
        } catch (disabled: BluetoothDisabledException) {
            retryWithBluetoothEnabled(action) ?: throw disabled
        }
    }

    private suspend fun <R> retryWithPermissions(permissions: List<String>, action: suspend () -> R): R? {
        val mutex = Mutex(true)
        var granted = false
        actions.emit(BluetoothAction.PermissionsRequest(permissions) { result ->
            granted = !result.containsValue(false)
            mutex.unlock()
        })
        return mutex.withLock {
            if (granted) action() else null
        }
    }

    private suspend fun <R> retryWithBluetoothEnabled(action: suspend () -> R): R? {
        val mutex = Mutex(true)
        var bluetoothEnabled = false
        actions.emit(BluetoothAction.EnableBluetoothRequest { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                bluetoothEnabled = true
            }
            mutex.unlock()
        })
        return mutex.withLock {
            if (bluetoothEnabled) action() else null
        }
    }
}