package com.ysered.circleprogress.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ysered.circleprogress.R

class CircleProgressView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    // defaults
    private val DP_IN_PX = Resources.getSystem().displayMetrics.density
    private val DEFAULT_PROGRESS_STROKE_WIDTH = 16 * DP_IN_PX

    // coordinates
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

    // action text
    private val progressPathPaint: Paint
    private val progressTextPaint: Paint
    private val actionTextPaint: Paint

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
     * Current action for progress view (e.g. start, stop, etc.)
     */
    var actionText: String

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0)
        progressStrokeWidth = array.getDimension(R.styleable.CircleProgressView_progressStrokeWidth, DEFAULT_PROGRESS_STROKE_WIDTH)
        actionText = array.getString(R.styleable.CircleProgressView_actionText) ?: ""
        actionTextSize = array.getDimension(R.styleable.CircleProgressView_actionTextSize, 32f)
        val selectedColor = array.getColor(R.styleable.CircleProgressView_selectedColor, Color.RED)
        progressTextSize = array.getDimension(R.styleable.CircleProgressView_progressTextSize, 32f)
        array.recycle()

        progressPathPaint = Paint().apply {
            isAntiAlias = true
            color = Color.LTGRAY
            style = Paint.Style.STROKE
            strokeWidth = progressStrokeWidth
        }

        progressTextPaint = Paint().apply {
            isAntiAlias = true
            color = Color.LTGRAY
            strokeWidth = 0f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textSize = progressTextSize
        }

        actionTextPaint = Paint(progressTextPaint).apply {
            textSize = actionTextSize
        }

        setOnTouchListener(OnTouchListener { _, motionEvent: MotionEvent? ->
            return@OnTouchListener when (motionEvent?.action) {
                MotionEvent.ACTION_DOWN -> {
                    progressPathPaint.color = selectedColor
                    actionTextPaint.color = selectedColor
                    invalidate()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    progressPathPaint.color = Color.LTGRAY
                    actionTextPaint.color = Color.LTGRAY
                    invalidate()
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
        canvas.drawText(progressText, textX, progressTextY, progressTextPaint)
        canvas.drawText(actionText, textX, actionTextY, actionTextPaint)
    }

}
