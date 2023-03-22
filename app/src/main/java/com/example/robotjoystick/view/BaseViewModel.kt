package com.example.robotjoystick.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<S: State, I: Intent> : ViewModel() {
    protected abstract val _stateFlow: MutableStateFlow<S>
    val stateFlow: StateFlow<S>
        get() = _stateFlow
    val intentFlow = Channel<I>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            intentFlow.consumeAsFlow().collect {
                viewModelScope.launch(Dispatchers.Default) {
                    reduce(stateFlow.value, it) // todo: race condition in state.messages
                }
            }
        }
    }

    protected suspend fun emit(state: S) {
        //viewModelScope.launch {
            _stateFlow.emit(state)
        //}
    }

    protected abstract suspend fun reduce(state: S, intent: I)
}
