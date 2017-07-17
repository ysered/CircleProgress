package com.ysered.circleprogress.view

import android.animation.ArgbEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.ysered.circleprogress.R

class CircleProgressView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    // defaults
    private val DP_IN_PX = Resources.getSystem().displayMetrics.density
    private val DEFAULT_PROGRESS_STROKE_WIDTH = 16 * DP_IN_PX
    private val DEFAULT_ANIM_DURATION = 300

    // coordinates
    private lateinit var progressBounds: RectF
    private var sweepAngle: Float = 0f
    private var progressX = 0f
    private var progressY = 0f
    private var progressRadius = 0f
    private var textX: Float = 0f
    private var progressTextY = 0f
    private var actionTextY: Float = 0f

    // dimens
    private val progressTextSize: Float
    private val actionTextSize: Float
    private val progressStrokeWidth: Float
    private val progressAnimDuration: Long
    private val colorAnimDuration: Long

    // paints
    private val progressPaint: Paint
    private val progressPathPaint: Paint
    private val progressTextPaint: Paint
    private val actionTextPaint: Paint
    private var isDrawProgress = true

    private val animationInterpolator by lazy { DecelerateInterpolator() }

    /**
     * Represents current progress from 0 to 100.
     */
    var progress: Int = 0
        set(value) {
            field = value
            ValueAnimator.ofFloat(sweepAngle, 360f / 100f * progress).apply {
                interpolator = animationInterpolator
                duration = progressAnimDuration
                addUpdateListener { animation ->
                    sweepAngle = animation.animatedValue as Float
                    invalidate()
                }
                start()
            }
        }

    /**
     * Current progress text (e.g. 50%, Running, Off, etc.)
     */
    var progressText: String = ""
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                invalidate()
            }
        }

    /**
     * Progress stroke color.
     */
    var progressColor: Int = Color.BLUE
        set(value) {
            if (field != value) {
                animateColorChange(field, value, { color ->
                    progressPaint.color = color
                    progressTextPaint.color = color
                    invalidate()
                })
                field = value
            }
        }

    /**
     * Current action for progress view (e.g. start, stop, etc.)
     */
    var actionText: String

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0)
        progressStrokeWidth = array.getDimension(R.styleable.CircleProgressView_progressStrokeWidth, DEFAULT_PROGRESS_STROKE_WIDTH)
        actionText = array.getString(R.styleable.CircleProgressView_actionText) ?: ""
        actionTextSize = array.getDimension(R.styleable.CircleProgressView_actionTextSize, 32f)
        val unselectedColor = array.getColor(R.styleable.CircleProgressView_unselectedColor, Color.BLACK)
        val selectedColor = array.getColor(R.styleable.CircleProgressView_selectedColor, Color.RED)
        progressTextSize = array.getDimension(R.styleable.CircleProgressView_progressTextSize, 32f)
        colorAnimDuration = array.getInt(R.styleable.CircleProgressView_colorAnimDuration, DEFAULT_ANIM_DURATION).toLong()
        progressAnimDuration = array.getInt(R.styleable.CircleProgressView_progressAnimDuration, DEFAULT_ANIM_DURATION).toLong()
        array.recycle()

        progressPaint = Paint().apply {
            isAntiAlias = true
            color = progressColor
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = progressStrokeWidth
        }

        progressPathPaint = Paint().apply {
            isAntiAlias = true
            color = unselectedColor
            style = Paint.Style.STROKE
            strokeWidth = progressStrokeWidth
        }

        progressTextPaint = Paint().apply {
            isAntiAlias = true
            color = unselectedColor
            strokeWidth = 0f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            textSize = progressTextSize // TODO: adjust to fit view's bounds if it too large
        }

        actionTextPaint = Paint(progressTextPaint).apply {
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = actionTextSize // TODO: adjust to fit view's bounds if it too large
        }

        setOnTouchListener(OnTouchListener { _, motionEvent: MotionEvent? ->
            return@OnTouchListener when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateColorChange(unselectedColor, selectedColor, { color ->
                        progressPathPaint.color = color
                        actionTextPaint.color = color
                        isDrawProgress = false
                    })
                    true
                }
                MotionEvent.ACTION_UP -> {
                    animateColorChange(selectedColor, unselectedColor, { color ->
                        progressPathPaint.color = color
                        actionTextPaint.color = color
                        isDrawProgress = true
                    })
                    true
                }
                else -> super.onTouchEvent(motionEvent)
            }
        })
    }

    /**
     * Calculate coordinates for progress and positions for text to omit this work in [onDraw].
     */
    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(w, h, oldWidth, oldHeight)

        val viewWidth = layoutParams.width.toFloat()
        val viewHeight = layoutParams.height.toFloat()

        // progress arc bounds
        val arcStart = progressStrokeWidth
        val arcEnd = viewWidth - arcStart
        progressBounds = RectF(arcStart, arcStart, arcEnd, arcEnd)

        (viewWidth / 2f).let {
            progressX = it
            textX = it
        }
        progressY = viewHeight / 2f
        progressRadius = progressX - progressStrokeWidth

        // progress text
        val progressTextBounds = Rect()
        progressTextPaint.getTextBounds(progressText, 0, progressText.length, progressTextBounds)
        progressTextY = viewHeight / 2f + progressTextBounds.height() / 2f

        // action text
        val actionTextBounds = Rect()
        actionTextPaint.getTextBounds(actionText, 0, actionText.length, actionTextBounds)
        actionTextY = viewHeight - actionTextBounds.height() * 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(progressX, progressY, progressRadius, progressPathPaint)
        if (isDrawProgress) {
            canvas.drawArc(progressBounds, 90f, sweepAngle, false, progressPaint)
        }
        canvas.drawText(progressText, textX, progressTextY, progressTextPaint)
        canvas.drawText(actionText, textX, actionTextY, actionTextPaint)
    }

    /**
     * Animates change from one from one color to another. Uses [ValueAnimator] and [ArgbEvaluator] to
     * interpolate color during [duration] milliseconds.
     *
     * [colorFrom] initial color
     * [colorTo] final color
     * [onColorSet] callback to be called when animated value will be available during animation updates
     * [interpolator] animator interpolator
     * [duration] animation duration
     */
    private inline fun animateColorChange(colorFrom: Int,
                                          colorTo: Int,
                                          crossinline onColorSet: (color: Int) -> Unit,
                                          interpolator: TimeInterpolator = animationInterpolator,
                                          duration: Long = colorAnimDuration) {
        ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo).apply {
            this.interpolator = interpolator
            this.duration = duration
            addUpdateListener {
                val colorValue = animatedValue as Int
                onColorSet(colorValue)
                invalidate()
            }
            start()
        }
    }
}
