package com.example.robotjoystick.data.bluetooth.permissions

class PermissionsDeniedException(
    val permissions: List<String>
) : Exception(permissions.toString()) {
}