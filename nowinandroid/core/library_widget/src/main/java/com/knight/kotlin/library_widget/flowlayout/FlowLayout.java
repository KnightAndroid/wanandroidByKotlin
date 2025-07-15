package com.knight.kotlin.library_widget.flowlayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knight.kotlin.library_common.util.CacheUtils;
import com.core.library_common.util.SIzeUnikKtxKt;
import com.knight.kotlin.library_widget.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;



/**
 * @author created by knight
 * @organize wanandroid
 * @Date 2021/7/2 14:41
 * @descript:
 */
public class FlowLayout extends ViewGroup {
    private final float textSize;
    private final int tagHeight;
    private final int deleteIconMargin;
    private final int childViewPadding;
//    private int defaultViewBackground;
//    private int selectViewBackground;
//    private int fixViewEditingBackground;

    private Drawable defaultViewBackground;

    public Drawable getDefaultViewBackground() {
        return defaultViewBackground;
    }

    public void setDefaultViewBackground(Drawable defaultViewBackground) {
        this.defaultViewBackground = defaultViewBackground;
    }

    public Drawable getSelectViewBackground() {
        return selectViewBackground;
    }

    public void setSelectViewBackground(Drawable selectViewBackground) {
        this.selectViewBackground = selectViewBackground;
    }

    public Drawable getFixViewEditingBackground() {
        return fixViewEditingBackground;
    }

    public void setFixViewEditingBackground(Drawable fixViewEditingBackground) {
        this.fixViewEditingBackground = fixViewEditingBackground;
    }

    private Drawable selectViewBackground;
    private Drawable fixViewEditingBackground;

    private int fixViewEditingTextColor;
    private int selectTextColor;
    private int textViewSpacing;
    private int verticalSpacing;
    private int defaultTextColor;
    private DragHandler mDragAndDropHandler;
    private AnimatorSet lastAnimatorSet;
    public int deleteIconWidth;
    private boolean isMeasureSuccess;
    private OnTagClickListener onTagClickListener;
    private List<TextView> recommentLists = new ArrayList<>();
    private String tagId = "";

    public TextView getSelectButton() {
        return selectButton;
    }

    private TextView selectButton;

    public SparseArray<ArrayList<TagInfo>> getRowSparseArray() {
        return rowSparseArray;
    }

    private SparseArray<ArrayList<TagInfo>> rowSparseArray;

    public List<TagInfo> getTagInfos() {
        return tagInfos;
    }

    private List<TagInfo> tagInfos;
    private boolean isSettingAnimation = false;


    public int getFixViewEditingTextColor() {
        return fixViewEditingTextColor;
    }

    public void setFixViewEditingTextColor(int fixViewEditingTextColor) {
        this.fixViewEditingTextColor = fixViewEditingTextColor;
    }

    public int getSelectTextColor() {
        return selectTextColor;
    }

    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
    }

    public int getDefaultTextColor() {
        return defaultTextColor;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.flowLayout, defStyle, 0);

        textViewSpacing = ViewSizeUtil.getCustomDimen(context, a.getInt(R.styleable.flowLayout_horizontalSpacingSize, 18));
        verticalSpacing = ViewSizeUtil.getCustomDimen(context, a.getInt(R.styleable.flowLayout_verticalSpacingSize, 15));
        tagHeight = ViewSizeUtil.getCustomDimen(context, a.getInt(R.styleable.flowLayout_tagHeight, 40));
        childViewPadding = ViewSizeUtil.getCustomDimen(context, a.getInt(R.styleable.flowLayout_childViewPadding, 26));
        textSize = ViewSizeUtil.getCustomDimen(context, a.getInt(R.styleable.flowLayout_flowLayoutTextSize, 14)) * 1.0f / ViewSizeUtil.getDensity(context);

        deleteIconWidth = ViewSizeUtil.getCustomDimen(context, a.getInt(R.styleable.flowLayout_deleteIconWidth, 16));
        deleteIconMargin = ViewSizeUtil.getCustomDimen(context, a.getInt(R.styleable.flowLayout_deleteIconMargin, 10));

        //(当前所在标签背景颜色)
      //  selectViewBackground = a.getResourceId(R.styleable.flowLayout_selectViewBackground, R.drawable.widget_label_bg);
        //(当前所在标签文字颜色)
        selectTextColor = a.getColor(R.styleable.flowLayout_selectTextColor, 0x55aff4);

        //默认标签文字颜色
        defaultTextColor = a.getColor(R.styleable.flowLayout_defaultTextColor, 0x55aff4);
        //默认标签背景颜色
     //   defaultViewBackground = a.getResourceId(R.styleable.flowLayout_defaultViewBackground, R.drawable.widget_label_bg);

        //编辑状态下
        //固定标签背景
        fixViewEditingTextColor = a.getColor(R.styleable.flowLayout_fixViewTextColor, 0x55aff4);
        //固定标签文字颜色
      //  fixViewEditingBackground = a.getResourceId(R.styleable.flowLayout_fixViewEditingBackground, R.drawable.widget_label_bg);


        a.recycle();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    public void setDefault() {
        mDragAndDropHandler = null;
    }

    public List<ImageView> deleteIconImageViews = new ArrayList<>();

    public void setIsEdit(boolean isEdit) {
        if (tagInfos != null) {
            if (isEdit) {
                for (int i = 0; i < tagInfos.size(); i++) {
                    addDeleteImageView(i);
                }
                for (int i = 0; i < recommentLists.size(); i++) {
                    setTextViewColor(recommentLists.get(i), fixViewEditingBackground, fixViewEditingTextColor);
                    recommentLists.get(i).setOnClickListener(null);
                }

                requestLayout();
            } else {
                recommentLists.clear();
                initData();
            }
        } else {
            Toast.makeText(getContext(), R.string.widget_set_edit_tips, Toast.LENGTH_SHORT).show();
        }
    }


    public void addDeleteImageView(int i) {
        ImageView imageView;
        imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LayoutParams(SIzeUnikKtxKt.dp2px(16), SIzeUnikKtxKt.dp2px(16)));
        imageView.setBackgroundResource(R.drawable.widget_label_delete);
        final int finalI = i;
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getChildAt(finalI);
                TagInfo tagInfo = (TagInfo) view.getTag();
                deleteTag(tagInfo);
            }
        });
        setOnLongClick(getChildAt(i), i);
        if (((TagInfo) getChildAt(i).getTag()).type == TagInfo.TYPE_TAG_SERVICE) {
            imageView.setVisibility(INVISIBLE);
        }
        addView(imageView);
        deleteIconImageViews.add(imageView);
    }

    public void deleteTag(TagInfo tagInfo) {
        tagInfos.remove(tagInfo);

        removeAllViews();
        isMeasureSuccess = false;
        addTags(tagInfos);
        deleteIconImageViews.clear();
        setIsEdit(true);
        if (onTagClickListener != null) {
            onTagClickListener.onTagDelete(tagInfo);
        }
    }

    public void initData() {
        removeAllViews();
        isMeasureSuccess = false;
        deleteIconImageViews.clear();
        setTags(tagInfos);
    }

    public void addTag(TagInfo tagInfo, boolean isEdit) {
        tagInfos.add(tagInfo);
        removeAllViews();
        isMeasureSuccess = false;
        addTags(tagInfos);
        deleteIconImageViews.clear();
        setIsEdit(isEdit);
    }

    /**
     * 当前选中的tagId
     *
     * @param tagId
     */
    public void setSelectTagId(String tagId) {
        this.tagId = tagId;
    }


    public void setTags(List<TagInfo> tagInfos) {
        addTags(tagInfos);
        requestLayout();
    }


    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    public void addTags(List<TagInfo> tagInfos) {
        this.tagInfos = tagInfos;
        for (int i = 0; i < tagInfos.size(); i++) {
            addListViewTextView(tagInfos, i);
        }
        if (getChildCount() > tagInfos.size()) {
            removeViews(tagInfos.size(), getChildCount() - tagInfos.size());
        }
    }


    public void addTextView(List<TagInfo> tagInfos, int i) {
        TagInfo tagInfo;
        TextView button;
        tagInfo = tagInfos.get(i);
        tagInfo.childPosition = i;
        button = new TextView(getContext());
        button.setTextColor(defaultTextColor);
        button.setGravity(Gravity.CENTER);
        button.setTextSize(textSize);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, tagHeight);
        button.setBackgroundResource(R.drawable.widget_label_bg);
        addView(button, layoutParams);
        button.setText(tagInfo.tagName);
        button.setTag(tagInfo);
        button.setTag(tagInfo);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTagClickListener != null) {
                    onTagClickListener.onTagClick((TagInfo) v.getTag());
                }
            }
        });
    }

    public void addListViewTextView(List<TagInfo> tagInfos, int i) {
        TagInfo tagInfo;
        TextView button;
        tagInfo = tagInfos.get(i);
        tagInfo.childPosition = i;
        if (i < getChildCount()) {
            button = (TextView) getChildAt(i);
        } else {
            button = new TextView(getContext());
            if (tagInfo.type == TagInfo.TYPE_TAG_SERVICE) {
                recommentLists.add(button);
            }
            if (tagId.equals("-1") && i == 0 && deleteIconImageViews.size() == 0) {
                selectButton = button;
                setTextViewColor(button, selectViewBackground, selectTextColor);
            } else {
                if (tagInfo.tagId.equals(tagId) && deleteIconImageViews.size() == 0) {
                    selectButton = button;
                    setTextViewColor(button, selectViewBackground, selectTextColor);
                } else {
                    setTextViewColor(button, defaultViewBackground, defaultTextColor);
                }
            }
            button.setGravity(Gravity.CENTER);
            button.setTextSize(textSize);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, tagHeight);
            addView(button, layoutParams);
        }
        button.setText(tagInfo.tagName);
        button.setTag(tagInfo);
        button.setTag(tagInfo);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTagClickListener != null) {
                    onTagClickListener.onTagClick((TagInfo) v.getTag());
                }
            }
        });
    }


    private void setTextViewColor(TextView textView, Drawable backgroundRes, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setStroke(2, CacheUtils.getThemeColor());
        gradientDrawable.setCornerRadius(SIzeUnikKtxKt.dp2px(6));
        //textView.setBackground(backgroundRes);
        textView.setBackground(gradientDrawable);
        textView.setTextColor(color);
    }

    void setOnLongClick(View button, int i) {
        if (mDragAndDropHandler != null) {
            final int finalI = i;
            TagInfo tagInfo = (TagInfo) getChildAt(finalI).getTag();
            if (tagInfo.type == TagInfo.TYPE_TAG_USER) {
                button.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        startDragging(finalI);
                        return true;
                    }
                });
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (!isMeasureSuccess && tagInfos != null) {
            final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(tagHeight, MeasureSpec.EXACTLY);
            final int imageMeasureSpec = MeasureSpec.makeMeasureSpec(deleteIconWidth, MeasureSpec.EXACTLY);

            rowSparseArray = FlowLayoutUtils.getTagRects(
                    tagInfos,
                    deleteIconMargin,
                    width - deleteIconMargin,
                    (int) (textSize * ViewSizeUtil.getDensity(getContext())),
                    tagHeight,
                    textViewSpacing,
                    verticalSpacing,
                    childViewPadding, new FlowLayoutUtils.onGetTagListener() {
                        @Override
                        public void onGetTag(int position, TagInfo tagInfo) {
                            getChildAt(position).measure(MeasureSpec.makeMeasureSpec(tagInfo.rect.width(), MeasureSpec.EXACTLY), childHeightMeasureSpec);
                            if (deleteIconImageViews.size() > 0) {
                                //                                tagInfo.rect.top += deleteIconMargin;
                                //                                tagInfo.rect.bottom += deleteIconMargin;
                                deleteIconImageViews.get(position).measure(imageMeasureSpec, imageMeasureSpec);
                            }
                        }
                    });
        }
        if (rowSparseArray != null && rowSparseArray.size() > 0) {
            List<TagInfo> tagInfos = rowSparseArray.get(rowSparseArray.size() - 1);
            if (tagInfos != null && tagInfos.size() > 0) {
                setMeasuredDimension(width, tagInfos.get(tagInfos.size() - 1).rect.bottom);
            } else {
                setMeasuredDimension(width, 0);
            }
        } else {
            setMeasuredDimension(width, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isMeasureSuccess && tagInfos != null) {
            TagInfo tagInfo;
            for (int i = 0; i < tagInfos.size(); i++) {
                tagInfo = getTagInfos().get(i);
                if (deleteIconImageViews.size() > 0) {
                    deleteIconImageViews.get(i).layout(getDeleteIconL(tagInfo.rect.right), getDeleteIconT(tagInfo.rect.top), getDeleteIconR(tagInfo.rect.right), getDeleteIconB(tagInfo.rect.top));
                }
                getChildAt(i).layout(tagInfo.rect.left, tagInfo.rect.top, tagInfo.rect.right, tagInfo.rect.bottom);
            }
        }
    }

    public int getDeleteIconB(int top) {
        return top - deleteIconMargin + deleteIconWidth;
    }

    public int getDeleteIconR(int right) {
        return right + deleteIconMargin;
    }

    public int getDeleteIconT(int top) {
        return top - deleteIconMargin;
    }

    public int getDeleteIconL(int right) {
        return right + deleteIconMargin - deleteIconWidth;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mDragAndDropHandler != null) {
            mDragAndDropHandler.dispatchDraw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDragAndDropHandler != null) {
            mDragAndDropHandler.dispatchDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDragAndDropHandler != null) {
            return mDragAndDropHandler.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDragAndDropHandler != null) {
            mDragAndDropHandler.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void enableDragAndDrop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            throw new UnsupportedOperationException("Drag and drop is only supported API levels 14 and up!");
        }
        mDragAndDropHandler = new DragHandler(this);
    }

    public void startDragging(final int position) {
        if (mDragAndDropHandler != null) {
            mDragAndDropHandler.startDragging(position);
        }
    }

    private void sortTag() {
        rowSparseArray = FlowLayoutUtils.getTagRects(tagInfos, deleteIconMargin, getMeasuredWidth() - deleteIconMargin, (int) (textSize * ViewSizeUtil.getDensity(getContext()) + 0.5f), tagHeight, textViewSpacing, verticalSpacing, childViewPadding, null);
    }

    public void startAnimation(final TagInfo lastTagInfo) {
        if (isSettingAnimation) {
            return;
        }
        sortTag();
        TagInfo tagInfo;
        Rect rect;
        List<Animator> animationList = new ArrayList<>();
        for (int i = 0; i < tagInfos.size(); i++) {
            rect = new Rect();
            tagInfo = (TagInfo) getChildAt(i).getTag();
            getChildAt(i).getHitRect(rect);
            //            if (deleteIconImageViews.size() > 0) {
            //                tagInfo.rect.top += deleteIconMargin;
            //                tagInfo.rect.bottom += deleteIconMargin;
            //            }
            if (getChildAt(i).isShown()) {
                if (rect.left != tagInfo.rect.left) {
                    animationList.add(getObjectAnimator(tagInfo.rect.left, "x", getChildAt(i), 250));
                    if (deleteIconImageViews.size() > 0) {
                        animationList.add(getObjectAnimator(getDeleteIconL(tagInfo.rect.right), "x", deleteIconImageViews.get(i), 250));
                    }
                }
                if (rect.top != tagInfo.rect.top) {
                    animationList.add(getObjectAnimator(tagInfo.rect.top, "y", getChildAt(i), 250));
                    if (deleteIconImageViews.size() > 0) {
                        animationList.add(getObjectAnimator(getDeleteIconT(tagInfo.rect.top), "y", deleteIconImageViews.get(i), 250));
                    }
                }
            } else {
                animationList.add(getObjectAnimator(tagInfo.rect.left, "x", getChildAt(i), 0));
                animationList.add(getObjectAnimator(tagInfo.rect.top, "y", getChildAt(i), 0));
                if (deleteIconImageViews.size() > 0) {
                    animationList.add(getObjectAnimator(getDeleteIconL(tagInfo.rect.right), "x", deleteIconImageViews.get(i), 0));
                    animationList.add(getObjectAnimator(getDeleteIconT(tagInfo.rect.top), "y", deleteIconImageViews.get(i), 0));
                }
            }
        }
        lastAnimatorSet = new AnimatorSet();
        lastAnimatorSet.playTogether(animationList);
        lastAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isSettingAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSettingAnimation = false;
                if (mDragAndDropHandler.getLastTagInfo() != null && lastTagInfo != mDragAndDropHandler.getLastTagInfo()) {
                    startAnimation(mDragAndDropHandler.getLastTagInfo());
                } else if (tagInfos.get(tagInfos.size() - 1).rect.bottom != getMeasuredHeight()) {
                    isMeasureSuccess = true;
                    requestLayout();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isSettingAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        lastAnimatorSet.start();
    }

    @NonNull
    public ObjectAnimator getObjectAnimator(int value, String property, View view, long duration) {
        ObjectAnimator x;
        x = ObjectAnimator.ofFloat(view, property, value);
        x.setDuration(duration);
        return x;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDragAndDropHandler != null && mDragAndDropHandler.isDrag()) {
            requestDisallowInterceptTouchEvent(true);
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);

        }

    }

}
