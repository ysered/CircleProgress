package com.ysered.circleprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ysered.circleprogress.R;

/**
 * Custom view to show circular progress.
 */
public class CircleProgressView extends View {

    // defaults
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 16f;
    private static final float DEFAULT_START_PROGRESS_ANGLE = 0f;
    private static final float DEFAULT_END_PROGRESS_ANGLE = 360f;
    private static final int DEFAULT_MAX_PROGRESS = 100;

    // dimens
    private int width;
    private int height;
    private final float strokeWidth;

    // flags
    private final boolean showGradientProgress;
    private final boolean isRoundedStroke;
    private boolean isShowText;

    // colors
    private final int startProgressColor;
    private final int endProgressColor;
    private final int progressPathColor;
    private final int backgroundColor;
    private final int textColor;

    // progress
    private final float startAngle;
    private final float endAngle;
    private float sweepAngle;
    private final float maxProgress;

    private String text;

    private final Paint progressPaint;
    private final Paint progressPathPaint;
    private final Paint backgroundPaint;
    private final Paint textPaint;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);
        try {
            backgroundColor = a.getColor(R.styleable.CircleProgressView_backgroundColor, Color.WHITE);
            showGradientProgress = a.getBoolean(R.styleable.CircleProgressView_showGradientProgress, false);
            startProgressColor = a.getColor(R.styleable.CircleProgressView_startProgressColor, Color.BLACK);
            endProgressColor = a.getColor(R.styleable.CircleProgressView_endProgressColor, Color.BLACK);
            progressPathColor = a.getColor(R.styleable.CircleProgressView_progressPathColor, Color.LTGRAY);
            strokeWidth = a.getDimension(R.styleable.CircleProgressView_progressStrokeWidth, DEFAULT_PROGRESS_STROKE_WIDTH);
            isRoundedStroke = a.getBoolean(R.styleable.CircleProgressView_roundedProgressStroke, true);
            textColor = a.getColor(R.styleable.CircleProgressView_textColor, Color.BLACK);
            startAngle = a.getFloat(R.styleable.CircleProgressView_startAngle, DEFAULT_START_PROGRESS_ANGLE);
            endAngle = a.getFloat(R.styleable.CircleProgressView_endAngle, DEFAULT_END_PROGRESS_ANGLE);
            maxProgress = a.getInt(R.styleable.CircleProgressView_maxProgress, DEFAULT_MAX_PROGRESS);
        } finally {
            a.recycle();
        }

        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(isRoundedStroke ? Paint.Cap.ROUND : Paint.Cap.BUTT);

        progressPathPaint = new Paint(progressPaint);
        progressPathPaint.setColor(progressPathColor);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(0);
        textPaint.setColor(textColor);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public void setText(String text) {
        isShowText = !TextUtils.isEmpty(text);
        if (isShowText) {
            this.text = text;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        if (isShowText) {
            drawText(canvas, text);
        }
    }

    public void setProgress(int progress) {
        sweepAngle = (endAngle / maxProgress) * progress;
        invalidate();
    }

    private void drawProgress(Canvas canvas) {
        final int diameter = (int) (Math.min(width, height) - strokeWidth);
        final int centerX = width / 2;
        final int centerY = height / 2;

        // background
        canvas.drawCircle(centerX, centerY, diameter / 2, backgroundPaint);

        if (showGradientProgress) {
            final int[] colors = {startProgressColor, endProgressColor};
            final Shader gradientShader = new SweepGradient(centerX, centerY, colors, null);
            progressPaint.setShader(gradientShader);
            // to start gradient from bottom center instead of right center
            canvas.rotate(90, centerX, centerY);
        } else {
            progressPaint.setColor(startProgressColor);
        }


        // progress
        final RectF progressBounds = new RectF(strokeWidth, strokeWidth, diameter, diameter);
        canvas.save();
        canvas.drawArc(progressBounds, startAngle, endAngle, false, progressPathPaint);
        canvas.drawArc(progressBounds, startAngle, sweepAngle, false, progressPaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas, String text) {
        textPaint.setTextSize(Math.min(width, height) / 6f);

        final Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        // Center text
        int xPos = canvas.getWidth() / 2;
        int yPos = (canvas.getHeight() / 2) + (bounds.height() / 2);

        if (showGradientProgress) {
            canvas.save();
            canvas.rotate(-90, getWidth() / 2, getHeight() / 2);
            canvas.drawText(text, xPos, yPos, textPaint);
            canvas.restore();
        } else {
            canvas.drawText(text, xPos, yPos, textPaint);
        }
    }
}
