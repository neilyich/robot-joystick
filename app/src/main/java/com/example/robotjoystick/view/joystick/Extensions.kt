package com.example.robotjoystick.view.joystick

import android.graphics.Path
import android.graphics.PointF
import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

fun cosD(degrees: Float) = cos(toRadians(degrees.toDouble())).toFloat()

fun sinD(degrees: Float) = sin(toRadians(degrees.toDouble())).toFloat()

fun atanD(tan: Float) = toDegrees(atan(tan).toDouble()).toFloat()

fun Path.moveTo(p: PointF) = moveTo(p.x, p.y)

fun Path.lineTo(p: PointF) = lineTo(p.x, p.y)