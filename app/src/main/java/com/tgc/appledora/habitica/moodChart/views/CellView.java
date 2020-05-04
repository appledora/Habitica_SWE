package com.tgc.appledora.habitica.moodChart.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.maseno.franklinesable.lifereminder.R;


public class CellView extends AppCompatTextView {

    private Path mTrianglePath;
    private Paint mTrianglePaint;
    private int mTriangleColor;

    public CellView(Context context) {
        super(context);
        init(null);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        // Triangle paint
        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setStyle(Paint.Style.FILL);

        // Triangle path
        mTrianglePath = new Path();

        TypedArray typedArray = getContext().obtainStyledAttributes(set, R.styleable.CellView);
        mTriangleColor = typedArray.getColor(R.styleable.CellView_second_color, Color.TRANSPARENT);
        mTrianglePaint.setColor(mTriangleColor);
        typedArray.recycle();
    }

    public void setTriangleColor(int triangleColor) {
        mTriangleColor = triangleColor;
        mTrianglePaint.setColor(mTriangleColor);
        invalidate();
        requestLayout();
    }

    public int getTriangleColor() {
        return mTriangleColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Triangle path
        mTrianglePath.moveTo(0, getHeight());
        mTrianglePath.lineTo(getWidth(), 0);
        mTrianglePath.lineTo(getWidth(), getHeight());
        mTrianglePath.lineTo(0, getHeight());
        mTrianglePath.close();

        // Draw triangle the above path and defined paint
        canvas.drawPath(mTrianglePath, mTrianglePaint);
    }
}
