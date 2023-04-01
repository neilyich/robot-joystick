package com.example.robotjoystick.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<S: State, I: Intent, VM: BaseViewModel<out S, in I>> : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract val viewModel: BaseViewModel<out S, in I>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.stateFlow.collect { render(it) }
        }
    }

    protected fun send(intent: I) {
        send(viewModel, intent)
    }

    protected fun send(viewModel: BaseViewModel<*, in I>, intent: I) {
        lifecycleScope.launch {
            viewModel.send(intent)
        }
    }

    protected fun addBackPressedCallback(invokeOnBackPressed: Boolean = false, callback: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callback()
                if (invokeOnBackPressed) requireActivity().onBackPressed()
            }
        })
    }

    abstract fun render(state: S)
}