package com.example.robotjoystick.view

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.robotjoystick.data.bluetooth.permissions.BluetoothDisabledException
import com.example.robotjoystick.data.bluetooth.permissions.PermissionsDeniedException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow

abstract class BaseViewModel<S: State, I: Intent> : ViewModel() {
    protected abstract val _stateFlow: MutableStateFlow<S>
    val stateFlow: StateFlow<S>
        get() = _stateFlow
    val state get() = stateFlow.value
    private val intentFlow = MutableSharedFlow<I>()//(extraBufferCapacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        viewModelScope.launch {
            intentFlow.collect {
                withContext(Dispatchers.IO) {
                    try {
                        reduce(it)
                    } catch (e: Throwable) {
                        try {
                            handleException(e)
                        } catch (e1: Throwable) {
                            Log.e("BaseViewModel", "Exception while handling exception in reduce", e1)
                        }
                    }
                }
            }
        }
    }

    protected suspend fun emit(state: S) {
        _stateFlow.emit(state)
    }

    protected abstract suspend fun reduce(intent: I)

    suspend fun send(intent: I) {
        intentFlow.emit(intent)
    }

    @CallSuper
    protected open fun handleException(e: Throwable) {
        Log.e("UNCAUGHT EXCEPTION", "", e)
        when (e) {
            is PermissionsDeniedException -> requestPermissions(e.permissions)
            is BluetoothDisabledException -> requestEnableBluetooth()
        }
    }

    private fun requestPermissions(permissions: List<String>) {

    }

    private fun requestEnableBluetooth() {

    }
}
