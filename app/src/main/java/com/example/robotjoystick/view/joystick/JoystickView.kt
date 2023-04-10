package com.example.robotjoystick.view.joystick

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.robotjoystick.R
import kotlin.math.min
import kotlin.math.sqrt


class JoystickView(
    context: Context,
    attrs: AttributeSet?
) : View(context, attrs) {

    private val circleColor: Int
    private val circleColorDark: Int
    private val circleShadowColor: Int
    //private val circleShadowRadius: Float

    private val sliderColor: Int
    private val sliderColorLight: Int
    private val sliderShadowColor: Int
    //private val sliderShadowRadius: Float

    private val sliderWeight: Float

    private val circlePaint: Paint
    private val sliderPaint: Paint
    private val arrowPaint: Paint

    private var circleRadius: Float = 0.0f
    private var sliderRadius: Float = 0.0f

    private var xc: Float = 0.0f
    private var yc: Float = 0.0f

    private var sliderXc: Float = -1.0f
    private var sliderYc: Float = -1.0f

    private val arrowPaddingWeight: Float
    private val arrowWeight: Float
    private val arrowHeadWeight: Float
    private val arrowHeadDegrees: Float
    private val arrowColor: Int

    private var arrowPaths: List<Path> = emptyList()

    private var isTouching = false

    private var sliderWasInsideMovementArea = true

    private val directionDetectionDistanceWeight: Float
    private var directionState: JoystickDirection = JoystickDirection.NoDirection

    private var directionChangedListener: ((JoystickDirection) -> Unit)? = {}

    private val lock = Any()

    private val directionMapping = mapOf(
        0 to JoystickDirection.Right,
        1 to JoystickDirection.DownRight,
        2 to JoystickDirection.Down,
        3 to JoystickDirection.DownLeft,
        4 to JoystickDirection.Left,
        5 to JoystickDirection.UpLeft,
        6 to JoystickDirection.Up,
        -2 to JoystickDirection.Up,
        -1 to JoystickDirection.UpRight,
    )

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.JoystickView,
            0, 0).apply {
            try {
                circleColor = getColor(R.styleable.JoystickView_circleColor, context.getColor(R.color.joystick_circle_color))
                circleColorDark = getColor(R.styleable.JoystickView_circleColorDark, context.getColor(R.color.joystick_circle_color_dark))
                circleShadowColor = getColor(R.styleable.JoystickView_circleShadowColor, context.getColor(R.color.joystick_circle_shadow_color))

                sliderColor = getColor(R.styleable.JoystickView_sliderColor, context.getColor(R.color.joystick_slider_color))
                sliderColorLight = getColor(R.styleable.JoystickView_sliderColorLight, context.getColor(R.color.joystick_slider_color_light))
                sliderShadowColor = getColor(R.styleable.JoystickView_sliderShadowColor, context.getColor(R.color.joystick_slider_shadow_color))

                sliderWeight = getFloat(R.styleable.JoystickView_sliderWeight, 0.3f)

                arrowPaddingWeight = getFloat(R.styleable.JoystickView_arrowPaddingWeight, 0.2f)
                arrowWeight = getFloat(R.styleable.JoystickView_arrowWeight, 0.2f)
                arrowHeadWeight = getFloat(R.styleable.JoystickView_arrowHeadLengthWeight, 0.6f)
                arrowHeadDegrees = getFloat(R.styleable.JoystickView_arrowHeadDegrees, 45f)
                arrowColor = getColor(R.styleable.JoystickView_arrowColor, context.getColor(R.color.joystick_arrow_color))
            } finally {
                recycle()
            }
        }
        directionDetectionDistanceWeight = 0.5f
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = circleColor
            style = Paint.Style.FILL
            setShadowLayer(14f, 0f, 7f, circleShadowColor)
        }
        sliderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = sliderColor
            style = Paint.Style.FILL
            setShadowLayer(10f, 0f, 0f, sliderShadowColor)
        }
        arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = arrowColor
            style = Paint.Style.STROKE
            strokeWidth = 10f
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN && isInsideSlider(event)) {
            isTouching = true
            sliderXc = event.x
            sliderYc = event.y
            updateCanvasAfterTouch()
            return true
        }
        if (event?.action == MotionEvent.ACTION_UP) {
            isTouching = false
            sliderXc = xc
            sliderYc = yc
            postDirection(JoystickDirection.NoDirection)
            updateCanvasAfterTouch()
            return true
        }
        if (!isTouching || event == null) {
            return false
        }
        //CoroutineScope(Dispatchers.Default).launch {
            //Log.i("START", "")
            //synchronized(lock) {
        val dxc = event.x - xc
        val dyc = event.y - yc
        val distanceFromCenter = dxc*dxc + dyc*dyc
        val angle = if (dxc > 0) {
            atanD(dyc / dxc)
        } else if (dxc < 0) {
            180f + atanD(dyc / dxc)
        } else if (dyc > 0) {
            90f
        } else {
            270f
        }
        val direction = calcDirection(angle, sqrt(distanceFromCenter))
        postDirection(direction)
        if (isInsideSliderMovementArea(event)) {
            sliderXc = event.x
            sliderYc = event.y
            sliderWasInsideMovementArea = true
        } else {
            val distanceLimit = circleRadius - sliderRadius
            val limitedDxc = distanceLimit * cosD(angle)
            val limitedDyc = distanceLimit * sinD(angle)
            sliderXc = xc + limitedDxc
            sliderYc = yc + limitedDyc
            if (sliderWasInsideMovementArea) {
                sliderWasInsideMovementArea = false
            }
        }
        updateCanvasAfterTouch()
            //}
            //Log.i("END", "")
        //}
        return true
    }

    private fun postDirection(direction: JoystickDirection) {
        if (direction != directionState) {
            directionState = direction
            directionChangedListener?.let { listener ->
                post { listener(direction) }
            }
        }
    }

    fun setOnDirectionChangedListener(listener: (JoystickDirection) -> Unit) {
        directionChangedListener = listener
    }

    private fun calcDirection(angle: Float, distanceFromCenter: Float): JoystickDirection {
        if (distanceFromCenter <= circleRadius * directionDetectionDistanceWeight) {
            return JoystickDirection.NoDirection
        }
        val toAdd = if (angle > 0) 1f else -1f
        val key = ((angle / 22.5f + toAdd) / 2f).toInt()
        return directionMapping[key] ?: JoystickDirection.NoDirection
    }

    private fun updateCanvasAfterTouch() {
        circlePaint.apply {
            shader = RadialGradient(xc, yc, circleRadius, circleColorDark, circleColor, Shader.TileMode.CLAMP)
        }
        sliderPaint.apply {
            shader = RadialGradient(sliderXc, sliderYc, sliderRadius, sliderColorLight, sliderColor, Shader.TileMode.CLAMP)
        }
        postInvalidate()
    }

    private fun isInsideSlider(event: MotionEvent): Boolean {
        val dx = event.x - sliderXc
        val dy = event.y - sliderYc
        return dx*dx + dy*dy < sliderRadius * sliderRadius
    }

    private fun isInsideSliderMovementArea(event: MotionEvent): Boolean {
//        if (directionState != JoystickDirection.NoDirection) {
//            return false
//        }
        val dx = event.x - xc
        val dy = event.y - yc
        return dx*dx + dy*dy < (circleRadius - sliderRadius).let { it * it }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = min(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), getDefaultSize(suggestedMinimumHeight, heightMeasureSpec))
        setMeasuredDimension(w, w)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val padding = 40f
        circleRadius = (min(w, h).toFloat() - padding) / 2.0f
        sliderRadius = circleRadius * sliderWeight
        xc = circleRadius + padding / 2
        yc = xc
        if (sliderXc < 0) {
            sliderXc = xc
        }
        if (sliderYc < 0) {
            sliderYc = yc
        }
        updateArrowPaths()
        arrowPaint.apply {
            strokeWidth = circleRadius * 0.02f
        }
        circlePaint.apply {
            shader = RadialGradient(xc, yc, circleRadius, context.getColor(R.color.joystick_circle_color_dark), context.getColor(R.color.joystick_circle_color), Shader.TileMode.CLAMP)
        }
        sliderPaint.apply {
            shader = RadialGradient(sliderXc, sliderYc, sliderRadius, context.getColor(R.color.joystick_slider_color_light), context.getColor(R.color.joystick_slider_color), Shader.TileMode.CLAMP)
        }
    }

    private fun updateArrowPaths() {
        val axes = listOf(0f, 90f, 180f, 270f)
        val startRadius = circleRadius * (1 - arrowWeight - arrowPaddingWeight)
        val endRadius = circleRadius * (1 - arrowPaddingWeight)
        val arrowLength = endRadius - startRadius
        val arrowHeadLength = arrowLength * arrowHeadWeight
        arrowPaths = axes.map { axis ->
            val xCoef = cosD(axis)
            val yCoef = sinD(axis)
            val startPoint = PointF(startRadius * xCoef, startRadius * yCoef).apply { offset(xc, yc) }
            val endPoint = PointF(endRadius * xCoef, endRadius * yCoef).apply { offset(xc, yc) }
            val arrowAngleDelta = atanD(arrowHeadLength * sinD(arrowHeadDegrees) / (arrowLength - arrowHeadLength * cosD(arrowHeadDegrees)))
            val rightArrowAngle = axis - arrowAngleDelta
            val leftArrowAngle = axis + arrowAngleDelta
            val arrowHeadEndRadius = arrowHeadLength * sinD(arrowHeadDegrees) / sinD(arrowAngleDelta)
            val arrowHeadRightPoint = PointF(arrowHeadEndRadius * cosD(rightArrowAngle), arrowHeadEndRadius * sinD(rightArrowAngle))
                .apply { offset(startPoint.x, startPoint.y) }
            val arrowHeadLeftPoint = PointF(arrowHeadEndRadius * cosD(leftArrowAngle), arrowHeadEndRadius * sinD(leftArrowAngle))
                .apply { offset(startPoint.x, startPoint.y) }
            Path().apply {
                moveTo(startPoint)
                lineTo(endPoint)
                moveTo(arrowHeadRightPoint)
                lineTo(endPoint)
                moveTo(arrowHeadLeftPoint)
                lineTo(endPoint)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: throw IllegalArgumentException("canvas must not be null")
        drawCircle(canvas)
        drawArrows(canvas)
        drawSlider(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        canvas.apply {
            drawCircle(xc, yc, circleRadius, circlePaint)
        }
    }

    private fun drawSlider(canvas: Canvas) {
        canvas.apply {
            drawCircle(sliderXc, sliderYc, sliderRadius, sliderPaint)
        }
    }

    private fun drawArrows(canvas: Canvas) {
        canvas.apply {
            for (p in arrowPaths) {
                drawPath(p, arrowPaint)
            }
        }
    }
}