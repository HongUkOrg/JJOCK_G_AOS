package com.david.user.sealseeksee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class LinedTextView extends android.support.v7.widget.AppCompatTextView {
    private Paint mPaint = new Paint();

    public LinedTextView(Context context) {
        super(context);
        initPaint();
    }

    public LinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public LinedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xffffffff);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("HONG", "onDraw called ");
        int left = (int)(HongController.getInstance().getWidth()*0.00);
        int right = (int)(HongController.getInstance().getWidth()*0.90);
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight() + (int)(getLineSpacingExtra());
        Log.d("HONG", "line Height "+getLineHeight());
        Log.d("HONG", "text size "+getTextSize());
        int count = (height-paddingTop-paddingBottom) / lineHeight;
        for (int i = 0; i < count; i++) {
            int baseline = lineHeight * (i+1) + paddingTop + (int)getLineSpacingExtra();
            canvas.drawLine(left+paddingLeft, baseline, (right-paddingRight), baseline, mPaint);
        }

        super.onDraw(canvas);
    }
}
