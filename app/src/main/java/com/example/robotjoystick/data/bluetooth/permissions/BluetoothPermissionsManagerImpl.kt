package com.example.robotjoystick.data.bluetooth.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import javax.inject.Inject

class BluetoothPermissionsManagerImpl @Inject constructor(
    private val context: Context
) : BluetoothPermissionsManager {
    override fun connectPermissions(): List<String> {
        val permissionToCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            Manifest.permission.BLUETOOTH_ADMIN
        }
        return listOf(permissionToCheck)
    }

    override fun checkConnectPermission() {
        checkPermissions(connectPermissions())
    }

    override fun scanPermissions(): List<String> {
        val permissionToCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_SCAN
        } else {
            Manifest.permission.BLUETOOTH_ADMIN
        }
        return listOf(
            permissionToCheck,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun checkScanPermissions() {
        checkPermissions(scanPermissions())
    }

    private fun checkPermissions(permissions: List<String>) {
        val deniedPermissions = permissions.filter { context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
        if (deniedPermissions.isNotEmpty()) {
            throw PermissionsDeniedException(deniedPermissions)
        }
    }
}