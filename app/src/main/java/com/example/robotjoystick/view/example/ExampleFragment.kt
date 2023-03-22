package com.example.robotjoystick.view.example

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.robotjoystick.RobotJoystickApp
import com.example.robotjoystick.databinding.ExampleFragmentBinding
import com.example.robotjoystick.view.BaseFragment
import javax.inject.Inject

class ExampleFragment : BaseFragment<ExampleState, ExampleIntent, ExampleViewModel>() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel: ExampleViewModel by viewModels { viewModelFactory }

    private lateinit var binding: ExampleFragmentBinding

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>
    private lateinit var bluetoothPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        RobotJoystickApp.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enableBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                send(ExampleIntent.BluetoothPermitted)
            } else {
                send(ExampleIntent.BluetoothRejected)
            }
        }
        bluetoothPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            Log.i("GRANTED", "$isGranted")
            send(ExampleIntent.BluetoothPermitted)
        }
        binding = ExampleFragmentBinding.inflate(inflater, container, false)
        binding.joystick.setOnDirectionChangedListener {
            binding.tvNumber.text = it.javaClass.simpleName
        }
        return binding.root
    }

//    override fun onResume() {
//        super.onResume()
//        if (!bluetoothAdapter.isEnabled) {
//            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            enableBluetoothLauncher.launch(intent)
//        }
//    }

    override fun render(state: ExampleState) {
        binding.tvNumber.text = state.value.toString()
        binding.btnAdd.setOnClickListener { send(ExampleIntent.BluetoothPermitted) }
        binding.btnSub.setOnClickListener { send(ExampleIntent.Sub) }
        if (state.scan) {
            binding.tvNumber.text = "Scanning"
        }
        state.askPermissions?.let { bluetoothPermissionLauncher.launch(it.toTypedArray()) }
    }


}