package com.core.library_base.widget.swapeback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author created by knight
 * @organize wanandroid
 * @Date 2021/6/4 15:34
 * @descript:
 */
public class ShadowView extends View {


    private final float density;
    private final Paint mShadowBarPaint;
    private int mLinearShaderWidth;
    private int mShadowColor;

    private boolean isShowShadowBar;
    private int colors[] = new int[11];
    private float positions[] = new float[11];

    public ShadowView(Context context) {
        this(context, true, true);
    }

    public ShadowView(Context context, boolean showShadowBar, boolean showBackground) {
        super(context);
        this.density = context.getResources().getDisplayMetrics().density;
        mShadowBarPaint = new Paint();
        mShadowBarPaint.setDither(true);
        mShadowBarPaint.setAntiAlias(true);
        mShadowBarPaint.setSubpixelText(true);
        setShowColor(Color.BLACK, showShadowBar, showBackground);
    }

    /**
     * 控制阴影显示
     *
     * @param showShadowBar  是否显示阴影条
     * @param showBackground 是否显示背景
     */
    public void setShadowVisiable(boolean showShadowBar, boolean showBackground) {
        setShowColor(mShadowColor, showShadowBar, showBackground);
    }

    /**
     * 设置阴影颜色
     *
     * @param shadowColor    阴影颜色
     * @param showShadowBar  是否显示阴影条
     * @param showBackground 是否显示背景
     */
    public void setShowColor(int shadowColor, boolean showShadowBar, boolean showBackground) {
        this.mShadowColor = shadowColor | 0XFF000000;
        this.isShowShadowBar = showShadowBar;
        if (isShowShadowBar) {
            for (int i = 0; i < colors.length; i++) {
                //根据位置计算透明度
                int alpha = getShadowAlpha(i * 0.1F);
                //计算对应颜色
                colors[i] = mShadowColor & ((alpha << 24) | 0XFFFFFF);
                //根据透明度校正对应位置
                float position = getShadowPosition(alpha);
                positions[i] = position;
            }
        }
        Drawable background = showBackground ? new ColorDrawable(mShadowColor & 0X66FFFFFF) : null;
        setBackground(background);
    }

    /**
     * 根据透明度计算位置比例
     *
     * @param alpha 透明度，取值范围[0,255]
     * @return 比例，取值范围[0,1]
     */
    private float getShadowPosition(int alpha) {
        float radius = 1.25F;
        double squarePosition = radius * radius - Math.pow((radius - alpha / 255F), 2);
        if (squarePosition <= 0) {
            return 0;
        } else if (squarePosition >= 1) {
            return 1;
        } else {
            return (float) Math.sqrt(squarePosition);
        }
    }

    /**
     * 根据位置比例计算透明度
     *
     * @param ratio 比例，取值范围[0,1]
     * @return 透明度，取值范围[0,255]
     */
    private int getShadowAlpha(float ratio) {
        float radius = 1.25F;
        double alphaF = radius - Math.sqrt(radius * radius - ratio * ratio);
        int alphaI = (int) (alphaF * 255 + 0.5F);
        if (alphaI <= 0) {
            return 0;
        } else if (alphaI >= 255) {
            return 255;
        } else {
            return alphaI;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (getWidth() != mLinearShaderWidth) {
            mLinearShaderWidth = getWidth();
            LinearGradient linearShader = new LinearGradient(mLinearShaderWidth - density * 22, 0F, mLinearShaderWidth, 0F,
                    colors, positions, LinearGradient.TileMode.CLAMP);
            mShadowBarPaint.setShader(linearShader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowShadowBar) {
            canvas.save();
            canvas.clipRect(getRight() - density * 22, getTop(), getRight(), getBottom());
            canvas.drawPaint(mShadowBarPaint);
            canvas.restore();
        }
    }
}
