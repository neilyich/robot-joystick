package com.example.robotjoystick.view.bluetooth

import android.app.Activity
import com.example.robotjoystick.R
import com.example.robotjoystick.data.bluetooth.BluetoothCommunicationException
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothDisabledException
import com.example.robotjoystick.data.bluetooth.permissions.PermissionsDeniedException
import com.example.robotjoystick.domain.bluetooth.BluetoothCommunicationUseCase
import com.example.robotjoystick.view.BaseViewModel
import com.example.robotjoystick.view.Intent
import com.example.robotjoystick.view.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.CoroutineContext

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
        onCoroutine(Dispatchers.IO) {
            bluetoothCommunication?.stop()
            actions.emit(error)
        }
    }

    protected open suspend fun withPermissions(action: suspend () -> Unit) {
        return try {
            action()
        } catch (denied: PermissionsDeniedException) {
            retryWithGrantedPermissions(denied.permissions, action)
        } catch (disabled: BluetoothDisabledException) {
            retryWithBluetoothEnabled(action)
        }
    }

    protected fun launchWithPermissions(action: suspend () -> Unit): Job =
        onCoroutine { withPermissions(action) }

    protected fun launchWithPermissions(context: CoroutineContext, action: suspend () -> Unit): Job =
        onCoroutine(context) { withPermissions(action) }

    private suspend fun retryWithGrantedPermissions(permissions: List<String>, action: suspend () -> Unit) {
        val context = currentCoroutineContext()
        actions.emit(BluetoothAction.PermissionsRequest(permissions) { result ->
            if (!result.containsValue(false)) {
                onCoroutine(context) { action() }
            }
        })
    }

    private suspend fun retryWithBluetoothEnabled(action: suspend () -> Unit) {
        val context = currentCoroutineContext()
        actions.emit(BluetoothAction.EnableBluetoothRequest { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onCoroutine(context) { action() }
            }
        })
    }
}