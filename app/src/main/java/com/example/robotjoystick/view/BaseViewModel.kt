package com.example.robotjoystick.view

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<S: State, I: Intent> : ViewModel() {
    protected abstract val _stateFlow: MutableStateFlow<S>
    val stateFlow: StateFlow<S>
        get() = _stateFlow
    val state get() = stateFlow.value

    protected open val defaultCoroutineContext: CoroutineContext = Dispatchers.IO + CoroutineExceptionHandler { _, e -> handleException(e) }

    protected suspend fun emit(state: S) {
        _stateFlow.emit(state)
    }

    protected fun onCoroutine(block: suspend () -> Unit) = onCoroutine(defaultCoroutineContext, block)

    protected open fun onCoroutine(context: CoroutineContext, block: suspend () -> Unit) = viewModelScope.launch(context) {
        block()
    }

    abstract fun onIntent(intent: I)

    @CallSuper
    protected open fun handleException(e: Throwable) {
        Log.e("UNCAUGHT EXCEPTION", "", e)
    }
}
