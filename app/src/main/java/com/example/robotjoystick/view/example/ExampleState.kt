package com.example.robotjoystick.view.example

import com.example.robotjoystick.view.State

data class ExampleState(
    val value: Int,
    val scan: Boolean = false,
    val askPermissions: List<String>? = listOf()
): State