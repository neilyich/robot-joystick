package com.example.robotjoystick.data.bluetooth.permissions

import android.util.Log

suspend fun <T> BluetoothPermissionsManager.withConnectPermissions(action: suspend () -> T): T {
    //checkConnectPermission()
    checkBluetoothEnabled()
    return try {
        action()
    } catch (e: SecurityException) {
        Log.e("PERMISSION", e.message, e)
        throw PermissionsDeniedException(connectPermissions())
    }
}

suspend fun <T> BluetoothPermissionsManager.withScanPermissions(action: suspend () -> T): T {
    //checkScanPermissions()
    checkBluetoothEnabled()
    return try {
        action()
    } catch (e: SecurityException) {
        Log.e("PERMISSION", e.message, e)
        throw PermissionsDeniedException(scanPermissions())
    }
}

suspend fun <T> ignoringPermissions(action: suspend () -> T): T? {
    return try {
        action()
    } catch (e: SecurityException) {
        return null
    }
}