package com.example.robotjoystick.view.bluetooth

import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes

sealed interface BluetoothAction {
    data class PermissionsRequest(
        val permissions: List<String>,
        val callback: (Map<String, Boolean>) -> Unit,
    ) : BluetoothAction

    data class EnableBluetoothRequest(
        val callback: (ActivityResult) -> Unit,
    ) : BluetoothAction

    data class BluetoothError(
        @StringRes val errorDescription: Int,
        val args: List<Any> = emptyList(),
    ) : BluetoothAction

//
//    data class PermissionsResult(
//        val granted: Boolean,
//    ) : BluetoothAction
}