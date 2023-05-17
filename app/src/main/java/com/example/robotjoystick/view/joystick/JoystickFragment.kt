package com.example.robotjoystick.view.joystick

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.robotjoystick.databinding.JoystickFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.bluetooth.BluetoothFragment
import com.example.robotjoystick.view.bluetoothdevice.RecyclerViewTextAdapter
import com.example.robotjoystick.view.news.AppNews

class JoystickFragment : BluetoothFragment<JoystickState, JoystickIntent, JoystickViewModel>() {
    override val viewModel: JoystickViewModel by viewModels { viewModelFactory }

    private lateinit var binding: JoystickFragmentBinding
    private lateinit var adapter: RecyclerViewTextAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JoystickFragmentBinding.inflate(inflater, container, false)

        requireArguments().getString(DEVICE_NAME)?.let { send(JoystickIntent.ArgumentsReceived(it)) }

        binding.legLeft.setOnDirectionChangedListener {
            send(JoystickIntent.JoystickDirectionChanged(it))
        }

        adapter = RecyclerViewTextAdapter({}) { newSize ->
            binding.msgList.smoothScrollToPosition(newSize - 1)
        }
        binding.msgList.adapter = adapter

        addBackPressedCallback {
            send(JoystickIntent.BackPressed)
        }

        binding.debugSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.msgList.visibility = if (isChecked) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        send(JoystickIntent.Stopped)
    }

    override fun render(state: JoystickState) {
        binding.listTitle.text = state.deviceName
        adapter.submitList(state.messages)
        state.news?.let {
            when (it) {
                is JoystickState.News.DisconnectDialog -> showQuitDialog(it)
                JoystickState.News.PerformHapticFeedback -> {
                    binding.handLeft.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                }
            }
            send(JoystickIntent.NewsShown)
        }
    }

    private fun showQuitDialog(data: JoystickState.News.DisconnectDialog) {
        news.show(AppNews.Dialog(
            title = getString(data.title).format(data.titleArg),
            positiveText = getString(data.positiveButtonText),
            negativeText = getString(data.negativeButtonText),
            positiveCallback = { _, _ -> send(JoystickIntent.DisconnectConfirmed) },
            negativeCallback = null,
        ), requireContext())
    }

    companion object {
        const val DEVICE_NAME = "DEVICE_NAME"
        @JvmStatic
        fun newInstance(deviceName: String): JoystickFragment {
            val fragment = JoystickFragment()
            fragment.arguments = bundleOf(DEVICE_NAME to deviceName)
            return fragment
        }
    }
}