package com.example.robotjoystick.view.knowndevices

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.robotjoystick.R
import com.example.robotjoystick.RobotJoystickApp
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import com.example.robotjoystick.databinding.KnownBluetoothDevicesFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.bluetoothdevice.BluetoothDeviceDataArrayAdapter
import com.example.robotjoystick.view.joystick.JoystickFragment
import com.example.robotjoystick.view.knowndevices.KnownBluetoothDevicesIntent.*
import javax.inject.Inject

class KnownBluetoothDevicesFragment : BaseFragment<KnownBluetoothDevicesState, KnownBluetoothDevicesIntent, KnownBluetoothDevicesViewModel>() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel: KnownBluetoothDevicesViewModel by viewModels { viewModelFactory }

    private lateinit var binding: KnownBluetoothDevicesFragmentBinding
    private lateinit var listAdapter: BluetoothDeviceDataArrayAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        RobotJoystickApp.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = KnownBluetoothDevicesFragmentBinding.inflate(inflater, container, false)

        binding.btnScanNewDevices.setOnClickListener {
            send(ScanNewDevicesClicked)
        }

        binding.btnRefresh.setOnClickListener {
            send(RefreshClicked)
        }

        listAdapter = BluetoothDeviceDataArrayAdapter(requireContext()) {
            send(BluetoothDeviceClicked(it))
            goToJoystickFragment(it)
        }
        binding.list.adapter = listAdapter

        send(RefreshClicked)

        return binding.root
    }

    override fun render(state: KnownBluetoothDevicesState) {
        if (state.isLoading) {
            binding.listTitle.text = getString(R.string.loading)
        } else {
            binding.listTitle.text = getString(R.string.known_devices)
        }
        listAdapter.submitList(state.devices)
    }

    private fun goToJoystickFragment(bluetoothDeviceData: BluetoothDeviceData) {
        val args = bundleOf(JoystickFragment.DEVICE_NAME to bluetoothDeviceData.name)
        val fragment = JoystickFragment()
        fragment.arguments = args
        parentFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }

}