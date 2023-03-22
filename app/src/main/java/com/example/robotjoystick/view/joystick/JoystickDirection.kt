package com.example.robotjoystick.view.joystick

enum class VerticalDirection {
    UP, DOWN, NO_MOVEMENT
}

enum class HorizontalDirection {
    RIGHT, LEFT, NO_MOVEMENT
}

sealed class JoystickDirection(
    val vertical: VerticalDirection,
    val horizontal: HorizontalDirection
) {
    object Up : JoystickDirection(VerticalDirection.UP, HorizontalDirection.NO_MOVEMENT)
    object Down : JoystickDirection(VerticalDirection.DOWN, HorizontalDirection.NO_MOVEMENT)
    object NoDirection : JoystickDirection(VerticalDirection.NO_MOVEMENT, HorizontalDirection.NO_MOVEMENT)
    object Right : JoystickDirection(VerticalDirection.NO_MOVEMENT, HorizontalDirection.RIGHT)
    object Left : JoystickDirection(VerticalDirection.NO_MOVEMENT, HorizontalDirection.LEFT)
    object UpRight : JoystickDirection(VerticalDirection.UP, HorizontalDirection.RIGHT)
    object UpLeft : JoystickDirection(VerticalDirection.UP, HorizontalDirection.LEFT)
    object DownRight : JoystickDirection(VerticalDirection.DOWN, HorizontalDirection.RIGHT)
    object DownLeft : JoystickDirection(VerticalDirection.DOWN, HorizontalDirection.LEFT)
}
