package com.example.robotjoystick.view.terminal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.robotjoystick.databinding.TerminalFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.BaseViewModel
import com.example.robotjoystick.view.bluetooth.BluetoothFragment
import com.example.robotjoystick.view.news.AppNews

class TerminalFragment : BluetoothFragment<TerminalState, TerminalIntent, TerminalViewModel>() {
    override val viewModel: TerminalViewModel by viewModels { viewModelFactory }

    private lateinit var binding: TerminalFragmentBinding

    private lateinit var adapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TerminalFragmentBinding.inflate(inflater, container, false)
        binding.inputView.btnSend.setOnClickListener {
            send(TerminalIntent.SendButtonClicked(binding.inputView.editMessage.text.toString()))
        }
        adapter = MessageAdapter()
        binding.rvMessages.adapter = adapter
        addBackPressedCallback {
            send(TerminalIntent.BackPressed)
        }
        send(TerminalIntent.ViewCreated)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        send(TerminalIntent.Stopped)
    }

    override fun render(state: TerminalState) {
        renderTitle(state)
        renderMessages(state)
        showNews(state)
    }

    private fun renderTitle(state: TerminalState) {
        if (state.title != null) {
            binding.tvTitle.text = state.title
        } else {
            binding.tvTitle.text = getString(state.defaultTitleRes)
        }
    }

    private fun renderMessages(state: TerminalState) {
        adapter.submitList(state.messages)
    }

    private fun showNews(state: TerminalState) {
        state.news?.let {
            when (it) {
                is TerminalState.News.DisconnectDialog -> showQuitDialog(it)
                is TerminalState.News.Toast -> showToast(it)
            }
            send(TerminalIntent.NewsShown)
        }
    }

    private fun showToast(toast: TerminalState.News.Toast) {
        news.show(
            AppNews.Toast(
            text = getString(toast.text).format(toast.arg)
        ), requireContext())
    }

    private fun showQuitDialog(data: TerminalState.News.DisconnectDialog) {
        news.show(AppNews.Dialog(
            title = getString(data.title).format(data.titleArg),
            positiveText = getString(data.positiveButtonText),
            negativeText = getString(data.negativeButtonText),
            positiveCallback = { _, _ -> send(TerminalIntent.DisconnectConfirmed) },
            negativeCallback = null,
        ), requireContext())
    }

}