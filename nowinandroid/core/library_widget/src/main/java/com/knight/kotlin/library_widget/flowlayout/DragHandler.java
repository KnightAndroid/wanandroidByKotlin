package com.knight.kotlin.library_widget.flowlayout;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author created by knight
 * @organize wanandroid
 * @Date 2021/7/2 14:39
 * @descript:
 */
public class DragHandler {
    private final int mSlop;
    private final FlowLayout flowLayout;
    private final LSwitchViewAnimator mSwitchViewAnimator;

    private View mMobileView;
    private float mLastMotionEventY;
    private float mLastMotionEventX;
    private TagsHoverDrawable mHoverDrawable;
    private float mDownX;
    private float mDownY;
    private int firstClickPosition;
    private TagInfo clickTag;
    private boolean isDrag;

    boolean isDrag() {
        return isDrag;
    }

    private ImageView delete;
    private BitmapDrawable deleteDrawable;

    public TagInfo getLastTagInfo() {
        return lastTagInfo;
    }

    private TagInfo lastTagInfo = new TagInfo();

    public DragHandler(FlowLayout FlowLayout) {
        this.flowLayout = FlowLayout;
        ViewConfiguration vc = ViewConfiguration.get(FlowLayout.getContext());
        mSlop = vc.getScaledTouchSlop();
        mSwitchViewAnimator = new LSwitchViewAnimator();
    }

    public boolean onTouchEvent(@NonNull final MotionEvent event) {
        boolean handled = false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionEventY = event.getY();
                mLastMotionEventX = event.getX();
                handled = handleDownEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDrag) {
                    mLastMotionEventY = event.getY();
                    mLastMotionEventX = event.getX();
                    handled = handleMoveEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                isDrag = false;
                handled = handleUpEvent();
                mLastMotionEventY = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                isDrag = false;
                handled = handleCancelEvent();
                mLastMotionEventY = -1;
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }

    private boolean handleDownEvent(@NonNull final MotionEvent event) {
        mDownX = event.getRawX();
        mDownY = event.getRawY();
        return true;
    }

    private boolean handleMoveEvent(final MotionEvent event) {
        boolean handled = false;

        float deltaX = event.getRawX() - mDownX;
        float deltaY = event.getRawY() - mDownY;
        TagInfo tagInfo = pointToPosition((int) event.getX(), (int) event.getY());
        if (mHoverDrawable == null && (deltaY * deltaY + deltaX * deltaX > mSlop * mSlop)) {
            if (tagInfo != null) {
                startDragging(tagInfo.dataPosition);
                handled = true;
            }
        } else if (mHoverDrawable != null) {
            mHoverDrawable.handleMoveEvent(event);
            if (tagInfo != null && tagInfo.type == TagInfo.TYPE_TAG_USER && (tagInfo != lastTagInfo || !tagInfo.rect.contains(clickTag.rect))) {
                switchViews(tagInfo);
            }
            flowLayout.invalidate();
            handled = true;
        }
        lastTagInfo = tagInfo;
        return handled;
    }

    private void switchViews(TagInfo tagInfo) {
        //        flowLayout.setDataPosition(currentPosition, position);
        mSwitchViewAnimator.animateSwitchView(tagInfo);
    }

    void startDragging(int position) {
        isDrag = true;
        mMobileView = flowLayout.getChildAt(position);
        if (flowLayout.deleteIconImageViews.size() > 0) {
            delete = flowLayout.deleteIconImageViews.get(position);
            delete.setVisibility(View.INVISIBLE);
            deleteDrawable = new BitmapDrawable(flowLayout.getResources(), TagsHoverDrawable.getBitmapFromView(delete));
        }
        if (mMobileView != null) {
            mHoverDrawable = new TagsHoverDrawable(mMobileView, mLastMotionEventY, mLastMotionEventX);
            mMobileView.setVisibility(View.INVISIBLE);
        }
        clickTag = (TagInfo) mMobileView.getTag();
        firstClickPosition = flowLayout.getTagInfos().indexOf(clickTag);
    }

    private boolean handleUpEvent() {
        if (mMobileView == null) {
            return false;
        }
        mMobileView.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        delete = null;
        deleteDrawable = null;
        mHoverDrawable = null;
        mMobileView = null;
        return true;
    }

    private boolean handleCancelEvent() {
        return handleUpEvent();
    }

    public void dispatchDraw(@NonNull final Canvas canvas) {
        if (mHoverDrawable != null) {
            mHoverDrawable.draw(canvas);
            deleteDrawable.setBounds(
                    flowLayout.getDeleteIconL(mHoverDrawable.getBounds().right),
                    flowLayout.getDeleteIconT(mHoverDrawable.getBounds().top),
                    flowLayout.getDeleteIconR(mHoverDrawable.getBounds().right),
                    flowLayout.getDeleteIconB(mHoverDrawable.getBounds().top));
            deleteDrawable.draw(canvas);
        }
    }

    private TagInfo pointToPosition(int x, int y) {
        TagInfo resultTagInfo = null;
        int count = 0;
        int row = 0;
        if (mMobileView != null && !clickTag.rect.contains(x, y)) {
            List<TagInfo> tagInfoList = null;
            for (int i = 0; i < flowLayout.getRowSparseArray().size(); i++) {
                if (y >= flowLayout.getRowSparseArray().get(i).get(0).rect.top && y <= flowLayout.getRowSparseArray().get(i).get(0).rect.bottom) {
                    tagInfoList = flowLayout.getRowSparseArray().get(i);
                    row = i;
                    break;
                } else {
                    count += flowLayout.getRowSparseArray().get(i).size();
                }
            }
            int clickPosition = flowLayout.getTagInfos().indexOf(clickTag);
            TagInfo tagInfo;
            if (tagInfoList != null) {
                if (x > tagInfoList.get(tagInfoList.size() - 1).rect.right) {
                    if (row == flowLayout.getRowSparseArray().size() - 1) {
                        resultTagInfo = tagInfoList.get(tagInfoList.size() - 1);
                        resultTagInfo.dataPosition = tagInfoList.size() + count - 1;
                    } else {
                        resultTagInfo = flowLayout.getRowSparseArray().get(row + 1).get(0);
                        resultTagInfo.dataPosition = count + flowLayout.getRowSparseArray().get(row).size();
                    }
                } else {
                    for (int i = 0; i < tagInfoList.size(); i++) {
                        tagInfo = tagInfoList.get(i);
                        if (tagInfo.rect.contains(x, y)) {
                            resultTagInfo = tagInfo;
                            if (x <= (tagInfo.rect.left + tagInfo.rect.right) / 2) {
                                resultTagInfo.dataPosition = count + i;
                            } else {
                                resultTagInfo.dataPosition = i + count + 1;
                            }
                            break;
                        }
                    }
                }
            }

            if (resultTagInfo != null && resultTagInfo.type == TagInfo.TYPE_TAG_USER && resultTagInfo.dataPosition != clickPosition) {
                if (resultTagInfo.dataPosition == flowLayout.getTagInfos().size() - 1) {
                    flowLayout.getTagInfos().remove(clickTag);
                    flowLayout.getTagInfos().add(clickTag);
                } else {
                    if (resultTagInfo.dataPosition < clickPosition) {
                        flowLayout.getTagInfos().add(resultTagInfo.dataPosition, clickTag);
                        flowLayout.getTagInfos().remove(clickPosition + 1);
                    } else {
                        flowLayout.getTagInfos().add(resultTagInfo.dataPosition, clickTag);
                        flowLayout.getTagInfos().remove(clickPosition);
                    }
                }
            }
        }
        return resultTagInfo;
    }

    private class LSwitchViewAnimator {

        public void animateSwitchView(TagInfo tagInfo) {
            flowLayout.getViewTreeObserver().addOnPreDrawListener(new AnimateSwitchViewOnPreDrawListener(tagInfo));
        }

        private class AnimateSwitchViewOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {

            private TagInfo tagInfo;

            public AnimateSwitchViewOnPreDrawListener(TagInfo tagInfo) {
                this.tagInfo = tagInfo;
            }

            @Override
            public boolean onPreDraw() {
                flowLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                flowLayout.startAnimation(tagInfo);
                return true;
            }
        }
    }
}
