package com.ysered.circleprogress.view

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.ysered.circleprogress.R

/**
 * Custom view to show circular progress.
 */
class CircleProgressViewKt(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : View(context, attrs, defStyleAttr) {

    // defaults
    private val DEFAULT_PROGRESS_STROKE_WIDTH = 16f
    private val DEFAULT_START_PROGRESS_ANGLE = 0f
    private val DEFAULT_END_PROGRESS_ANGLE = 360f
    private val DEFAULT_MAX_PROGRESS = 100

    // flags
    private val showGradientProgress: Boolean
    private val isRoundedStroke: Boolean

    // colors
    private val progressBackgroundColor: Int
    private val startProgressColor: Int
    private val endProgressColor: Int
    private val progressPathColor: Int
    private val progressTextColor: Int

    // progress settings
    private val progressStrokeWidth: Float
    private val startAngle: Float
    private val endAngle: Float
    private val maxProgress: Int
    private var sweepAngle: Float = 0f

    // paints
    private val backgroundPaint: Paint
    private val progressPaint: Paint
    private val progressPathPaint: Paint
    private val textPaint: Paint

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0)
        try {
            progressBackgroundColor = a.getColor(R.styleable.CircleProgressView_progressBackgroundColor, Color.WHITE)
            showGradientProgress = a.getBoolean(R.styleable.CircleProgressView_showGradientProgress, false)
            startProgressColor = a.getColor(R.styleable.CircleProgressView_startProgressColor, Color.BLACK)
            endProgressColor = a.getColor(R.styleable.CircleProgressView_endProgressColor, Color.BLACK)
            progressPathColor = a.getColor(R.styleable.CircleProgressView_progressPathColor, Color.LTGRAY)
            progressStrokeWidth = a.getDimension(R.styleable.CircleProgressView_progressStrokeWidth, DEFAULT_PROGRESS_STROKE_WIDTH)
            isRoundedStroke = a.getBoolean(R.styleable.CircleProgressView_roundedProgressStroke, true)
            progressTextColor = a.getColor(R.styleable.CircleProgressView_progressTextColor, Color.BLACK)
            startAngle = a.getFloat(R.styleable.CircleProgressView_startAngle, DEFAULT_START_PROGRESS_ANGLE)
            endAngle = a.getFloat(R.styleable.CircleProgressView_endAngle, DEFAULT_END_PROGRESS_ANGLE)
            maxProgress = a.getInt(R.styleable.CircleProgressView_maxProgress, DEFAULT_MAX_PROGRESS)
        } finally {
            a.recycle()
        }

        // initialize paints
        backgroundPaint = Paint()
        backgroundPaint.color = progressBackgroundColor

        progressPaint = Paint()
        with(progressPaint) {
            isAntiAlias = true
            strokeWidth = progressStrokeWidth
            style = Paint.Style.STROKE
            strokeCap = if (isRoundedStroke) Paint.Cap.ROUND else Paint.Cap.BUTT
        }

        progressPathPaint = Paint(progressPaint)
        progressPathPaint.color = progressPathColor

        textPaint = Paint()
        with(textPaint) {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            strokeWidth = 0f
            color = progressTextColor
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    var isShowText: Boolean = false

    var progressText: String = ""
        set(value) {
            isShowText = !TextUtils.isEmpty(value)
            if (isShowText) {
                field = value
                invalidate()
            }
        }

    var progress: Int = 0
        set(value) {
            field = value
            sweepAngle = endAngle / maxProgress * progress
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = measuredWidth
        viewHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawProgress(canvas)
        if (isShowText) {
            drawText(canvas, progressText)
        }
    }

    private fun drawProgress(canvas: Canvas) {
        val diameter = (Math.min(width, height) - progressStrokeWidth).toInt()
        val centerX = width / 2
        val centerY = height / 2

        // background
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), (diameter / 2).toFloat(), backgroundPaint)

        if (showGradientProgress) {
            val colors = intArrayOf(startProgressColor, endProgressColor)
            val gradientShader = SweepGradient(centerX.toFloat(), centerY.toFloat(), colors, null)
            progressPaint.shader = gradientShader
            // to start gradient from bottom center instead of right center
            canvas.rotate(90f, centerX.toFloat(), centerY.toFloat())
        } else {
            progressPaint.color = startProgressColor
        }

        // progress
        val progressBounds = RectF(progressStrokeWidth, progressStrokeWidth, diameter.toFloat(), diameter.toFloat())
        canvas.save()
        canvas.drawArc(progressBounds, startAngle, endAngle, false, progressPathPaint)
        canvas.drawArc(progressBounds, startAngle, sweepAngle, false, progressPaint)
        canvas.restore()
    }

    private fun drawText(canvas: Canvas, text: String) {
        textPaint.textSize = Math.min(width, height) / 6f

        val bounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, bounds)

        // Center text
        val xPos = canvas.width / 2
        val yPos = canvas.height / 2 + bounds.height() / 2

        if (showGradientProgress) {
            canvas.save()
            canvas.rotate(-90f, (width / 2).toFloat(), (height / 2).toFloat())
            canvas.drawText(text, xPos.toFloat(), yPos.toFloat(), textPaint)
            canvas.restore()
        } else {
            canvas.drawText(text, xPos.toFloat(), yPos.toFloat(), textPaint)
        }
    }
}