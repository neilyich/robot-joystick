package com.example.robotjoystick.view.scandevices

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.robotjoystick.databinding.ScanNewDevicesFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.bluetooth.BluetoothFragment
import com.example.robotjoystick.view.bluetoothdevice.BluetoothDeviceDataArrayAdapter

class ScanDevicesFragment : BluetoothFragment<ScanDevicesState, ScanDevicesIntent, ScanDevicesViewModel>() {
    override val viewModel: ScanDevicesViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ScanNewDevicesFragmentBinding
    private lateinit var adapter: BluetoothDeviceDataArrayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScanNewDevicesFragmentBinding.inflate(inflater, container, false)
        adapter = BluetoothDeviceDataArrayAdapter(requireContext(), clickCallback = {
            send(ScanDevicesIntent.DeviceClicked(it))
        })
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onResume() {
        send(ScanDevicesIntent.Resumed)
        super.onResume()
    }

    override fun onPause() {
        send(ScanDevicesIntent.Paused)
        super.onPause()
    }

    override fun render(state: ScanDevicesState) {
        Log.i("SUBMIT", state.foundDevices.toString())
        adapter.submitList(state.foundDevices)
    }
}