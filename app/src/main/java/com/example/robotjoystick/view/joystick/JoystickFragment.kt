package com.example.robotjoystick.view.joystick

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.robotjoystick.databinding.JoystickFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.bluetoothdevice.RecyclerViewTextAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class JoystickFragment : BaseFragment<JoystickState, JoystickIntent, JoystickViewModel>() {
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

        binding.joystick.setOnDirectionChangedListener {
            send(JoystickIntent.JoystickDirectionChanged(it))
        }

        adapter = RecyclerViewTextAdapter({}) { newSize ->
            binding.msgList.smoothScrollToPosition(newSize - 1)
        }
        binding.msgList.adapter = adapter

        addBackPressedCallback {
            send(JoystickIntent.BackPressed)
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
                is JoystickState.News.ShowQuitDialog -> showQuitDialog(it)
            }
            send(JoystickIntent.NewsShown)
        }
    }

    private fun showQuitDialog(data: JoystickState.News.ShowQuitDialog) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(data.title).format(data.titleArg))
            .setPositiveButton(data.positiveButtonText) { _, _ ->
                send(JoystickIntent.QuitConfirmed)
            }
            .setNegativeButton(data.negativeButtonText, null)
            .show()
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