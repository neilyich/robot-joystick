package com.example.robotjoystick.view.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.robotjoystick.view.BaseFragment
import com.example.robotjoystick.view.Intent
import com.example.robotjoystick.view.State
import com.example.robotjoystick.view.news.AppNews
import kotlinx.coroutines.launch

abstract class BluetoothFragment<S: State, I: Intent, VM: BluetoothViewModel<S, I>> : BaseFragment<S, I, VM>() {

    private var requestMultiplePermissionsCallback: ((Map<String, Boolean>) -> Unit)? = null
    private lateinit var requestMultiplePermissionsLauncher: ActivityResultLauncher<Array<String>>

    private var enableBluetoothCallback: ((ActivityResult) -> Unit)? = null
    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<android.content.Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestMultiplePermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            requestMultiplePermissionsCallback?.invoke(it)
            requestMultiplePermissionsCallback = null
        }
        enableBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            enableBluetoothCallback?.invoke(it)
            enableBluetoothCallback = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.actionsFlow.collect {
                handleBluetoothAction(it)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    protected open fun handleBluetoothAction(action: BluetoothAction) {
        when (action) {
            is BluetoothAction.PermissionsRequest -> requestPermissions(action)
            is BluetoothAction.EnableBluetoothRequest -> enableBluetooth(action)
            is BluetoothAction.BluetoothError -> showBluetoothError(action)
        }
    }

    private fun requestPermissions(request: BluetoothAction.PermissionsRequest) {
        requestMultiplePermissionsCallback = request.callback
        requestMultiplePermissionsLauncher.launch(request.permissions.toTypedArray())
    }

    private fun enableBluetooth(request: BluetoothAction.EnableBluetoothRequest) {
        enableBluetoothCallback = request.callback
        enableBluetoothLauncher.launch(android.content.Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
    }

    private fun showBluetoothError(error: BluetoothAction.BluetoothError) {
        news.show(AppNews.Toast(
            getString(error.errorDescription).format(error.args)
        ), requireContext())
    }

    override fun onDetach() {
        super.onDetach()
        requestMultiplePermissionsLauncher.unregister()
        enableBluetoothLauncher.unregister()
    }
}