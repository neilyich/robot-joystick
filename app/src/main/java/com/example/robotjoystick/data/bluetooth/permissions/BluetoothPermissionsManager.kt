package com.example.robotjoystick.data.bluetooth.permissions

interface BluetoothPermissionsManager {
    fun connectPermissions(): List<String>
    fun checkConnectPermission()

    fun scanPermissions(): List<String>
    fun checkScanPermissions()
    fun checkBluetoothEnabled()
}