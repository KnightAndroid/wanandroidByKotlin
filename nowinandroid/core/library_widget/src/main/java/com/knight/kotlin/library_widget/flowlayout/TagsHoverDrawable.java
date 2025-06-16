package com.knight.kotlin.library_widget.flowlayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * @author created by knight
 * @organize wanandroid
 * @Date 2021/7/2 14:48
 * @descript:
 */
public class TagsHoverDrawable extends BitmapDrawable {

    private final int maxBottom;
    private final int maxRight;
    private  float mDownX;
    private  int mOriginalX;
    /**
     * The original y coordinate of the top of given {@code View}.
     */
    private float mOriginalY;

    /**
     * The original y coordinate of the position that was touched.
     */
    private float mDownY;

    /**
     * Creates a new {@code HoverDrawable} for given {@link View}, using given {@link MotionEvent}.
     *
     * @param view  the {@code View} to represent.
     * @param downY the y coordinate of the down event.
     */
    TagsHoverDrawable(@NonNull final View view, final float downY, final float downX) {
        super(view.getResources(), getBitmapFromView(view));
        TagInfo tagInfo = (TagInfo) view.getTag();
        ViewGroup parent = (ViewGroup) view.getParent();
        maxBottom = parent.getHeight();
        maxRight = parent.getWidth();
        mOriginalY = tagInfo.rect.top;
        mOriginalX = tagInfo.rect.left;
        mDownY = downY;
        mDownX = downX;
        setBounds(tagInfo.rect.left, tagInfo.rect.top, tagInfo.rect.right, tagInfo.rect.bottom);
    }

    /**
     * Calculates the new position for this {@code HoverDrawable} using given {@link MotionEvent}.
     *
     * @param ev the {@code MotionEvent}.
     *           {@code ev.getActionMasked()} should typically equal {@link MotionEvent#ACTION_MOVE}.
     */
    void handleMoveEvent(@NonNull final MotionEvent ev) {
        int top = (int) (mOriginalY - mDownY + ev.getY());
        int left = (int) (mOriginalX - mDownX + ev.getX());
        setBounds(left, top, left + getIntrinsicWidth(), top + getIntrinsicHeight());
    }

    /**
     * Returns whether the user is currently dragging this {@code HoverDrawable} upwards.
     *
     * @return true if dragging upwards.
     */
    boolean isMovingUpwards() {
        return mOriginalY > getBounds().top;
    }

    /**
     * Returns the number of pixels between the original y coordinate of the view, and the current y coordinate.
     * A negative value means this {@code HoverDrawable} is moving upwards.
     *
     * @return the number of pixels.
     */
    int getDeltaY() {
        return (int) (getBounds().top - mOriginalY);
    }

    /**
     * Returns the top coordinate of this {@code HoverDrawable}.
     */
    int getTop() {
        return getBounds().top;
    }

    /**
     * Shifts the original y coordinates of this {@code HoverDrawable} {code height} pixels upwards or downwards,
     * depending on the move direction.
     *
     * @param height the number of pixels this {@code HoverDrawable} should be moved. Should be positive.
     */
    void shift(final int height) {
        int shiftSize = isMovingUpwards() ? -height : height;
        mOriginalY += shiftSize;
        mDownY += shiftSize;
    }

    static Bitmap getBitmapFromView(@NonNull final View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
}