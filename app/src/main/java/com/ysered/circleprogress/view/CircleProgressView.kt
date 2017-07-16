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

    private val DP_IN_PX = Resources.getSystem().displayMetrics.density
    private val DEFAULT_PROGRESS_STROKE_WIDTH = 16 * DP_IN_PX

    // dimens
    private var progressX = 0f
    private var progressY = 0f
    private var progressRadius = 0f

    // progress
    private val progressStrokeWidth: Float
    private val progressPathPaint: Paint
    private val progressTextPaint: Paint

    // action text
    private var actionTextX: Float = 0f
    private var actionTextY: Float = 0f
    private val actionText: String
    private val actionTextSize: Float
    private val actionTextPaint: Paint

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0)
        progressStrokeWidth = array.getDimension(R.styleable.CircleProgressView_progressStrokeWidth, DEFAULT_PROGRESS_STROKE_WIDTH)
        actionText = array.getString(R.styleable.CircleProgressView_actionText) ?: ""
        actionTextSize = array.getDimension(R.styleable.CircleProgressView_actionTextSize, 32f)
        val selectedColor = array.getColor(R.styleable.CircleProgressView_selectedColor, Color.RED)
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
        }

        actionTextPaint = Paint().apply {
            isAntiAlias = true
            color = Color.LTGRAY
            strokeWidth = 0f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
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

        progressX = viewWidth / 2f
        progressY = viewHeight / 2f
        progressRadius = progressX - progressStrokeWidth

        val textBounds = Rect()
        actionTextPaint.getTextBounds(actionText, 0, actionText.length, textBounds)

        actionTextX = viewWidth / 2f
        actionTextY = viewHeight - textBounds.height() * 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(progressX, progressY, progressRadius, progressPathPaint)
        canvas.drawText(actionText, actionTextX, actionTextY, actionTextPaint)
    }

}
