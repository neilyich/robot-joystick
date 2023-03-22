package com.example.robotjoystick.view.joystick

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.robotjoystick.RobotJoystickApp
import com.example.robotjoystick.databinding.JoystickFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.bluetoothdevice.BluetoothDeviceDataArrayAdapter
import com.example.robotjoystick.view.bluetoothdevice.RecyclerViewTextAdapter
import javax.inject.Inject

class JoystickFragment : BaseFragment<JoystickState, JoystickIntent, JoystickViewModel>() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel: JoystickViewModel by viewModels { viewModelFactory }

    private lateinit var binding: JoystickFragmentBinding
    private lateinit var adapter: RecyclerViewTextAdapter

    companion object {
        const val DEVICE_NAME = "DEVICE_NAME"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        RobotJoystickApp.appComponent.inject(this)
    }

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

        return binding.root
    }

    override fun render(state: JoystickState) {
        binding.listTitle.text = state.deviceName
        adapter.submitList(state.messages)
    }
}