package com.example.robotjoystick.view.knowndevices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.robotjoystick.R
import com.example.robotjoystick.databinding.KnownDevicesFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.bluetoothdevice.BluetoothDeviceDataArrayAdapter
import com.example.robotjoystick.view.knowndevices.KnownDevicesIntent.*

class KnownDevicesFragment : BaseFragment<KnownDevicesState, KnownDevicesIntent, KnownDevicesViewModel>() {
    override val viewModel: KnownDevicesViewModel by viewModels { viewModelFactory }

    private lateinit var binding: KnownDevicesFragmentBinding
    private lateinit var listAdapter: BluetoothDeviceDataArrayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = KnownDevicesFragmentBinding.inflate(inflater, container, false)

        binding.btnScanNewDevices.setOnClickListener {
            send(ScanNewDevicesClicked)
        }

        binding.btnRefresh.setOnClickListener {
            send(RefreshClicked)
        }

        listAdapter = BluetoothDeviceDataArrayAdapter(requireContext()) {
            send(DeviceClicked(it))
        }
        binding.list.adapter = listAdapter

        send(CreateView)

        return binding.root
    }

    override fun render(state: KnownDevicesState) {
        if (state.isLoading) {
            binding.listTitle.text = getString(R.string.loading)
        } else {
            binding.listTitle.text = getString(R.string.known_devices)
        }
        listAdapter.submitList(state.devices)
//        state.navigateTo?.let {
//            when (it) {
//                is KnownDevicesState.Destination.Joystick -> goToJoystickFragment(it.device)
//                KnownDevicesState.Destination.ScanNewDevices -> goToScanNewDevicesFragment()
//            }
//        }
    }

//    private fun goToJoystickFragment(bluetoothDeviceData: BluetoothDeviceData) {
//        val args = bundleOf(JoystickFragment.DEVICE_NAME to bluetoothDeviceData.name)
//        val fragment = JoystickFragment()
//        fragment.arguments = args
//        performNavigation(fragment)
//    }
//
//    private fun goToScanNewDevicesFragment() {
//        val fragment = ScanDevicesFragment()
//        performNavigation(fragment)
//    }
//
//    private fun performNavigation(fragment: Fragment) {
//        Log.i("navigating", "here")
//        parentFragmentManager.beginTransaction()
//            .addToBackStack(null)
//            .replace(R.id.fragmentContainerView, fragment)
//            .commit()
//        send(NavigationPerformed)
//    }

}