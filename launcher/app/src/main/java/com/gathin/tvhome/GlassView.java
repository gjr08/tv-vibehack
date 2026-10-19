package com.gathin.tvhome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * A colored focus border with a soft glow. One instance floats over the grid and is moved
 * with spring animations between tiles. The color follows the focused app.
 */
public class GlassView extends View {

    private final Paint rim = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint glow = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final float density;
    private float radius;
    private int color = 0xFFFFFFFF;

    public GlassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = getResources().getDisplayMetrics().density;
        radius = 20f * density;
        rim.setStyle(Paint.Style.STROKE);
        rim.setStrokeWidth(3f * density);
        rim.setColor(color);
        glow.setStyle(Paint.Style.STROKE);
    }

    void setRadius(float px) {
        radius = px;
        invalidate();
    }

    /** Sets the border color; dark colors are brightened so the border stays visible. */
    void setColor(int c) {
        float[] hsv = new float[3];
        Color.colorToHSV(c, hsv);
        hsv[2] = Math.max(hsv[2], 0.85f);
        color = Color.HSVToColor(hsv);
        rim.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float inset = 6f * density;   // room for the glow
        rect.set(inset, inset, getWidth() - inset, getHeight() - inset);

        for (int i = 1; i <= 3; i++) {
            glow.setStrokeWidth(i * 2f * density);
            glow.setColor(((0x40 / i) << 24) | (color & 0x00FFFFFF));
            float o = i * density;
            canvas.drawRoundRect(rect.left - o, rect.top - o, rect.right + o, rect.bottom + o, radius + o, radius + o, glow);
        }

        canvas.drawRoundRect(rect, radius, radius, rim);
    }
}
