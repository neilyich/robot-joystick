package com.example.robotjoystick.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFragment<S: State, I: Intent, VM: BaseViewModel<out S, in I>> : Fragment() {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.stateFlow.collect { render(it) }
        }
    }

    protected fun send(intent: I) {
        lifecycleScope.launch {
            viewModel.intentFlow.send(intent)
        }
    }

    abstract fun render(state: S)
}