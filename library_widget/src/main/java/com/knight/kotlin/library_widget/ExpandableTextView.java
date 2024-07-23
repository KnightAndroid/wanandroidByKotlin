package com.knight.kotlin.library_widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Author:Knight
 * Time:2024/7/23 10:52
 * Description:ExpandableTextView
 */
public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    /**
     * <br>handler signal
     */
    private final int WHAT = 2;
    /**
     * <br>animation end signal of handler
     */
    private final int WHAT_ANIMATION_END = 3;
    /**
     * <br>animation end and expand only,but not disappear
     */
    private final int WHAT_EXPAND_ONLY = 4;
    public OnStateChangeListener onStateChangeListener;
    /**
     * TextView
     */
    private TextView textView;
    /**
     * <br>shrink/expand TextView
     */
    private TextView tvState;
    /**
     * <br>shrink/expand icon
     */
    private ImageView ivExpandOrShrink;
    /**
     * <br>shrink/expand layout parent
     */
    private RelativeLayout rlToggleLayout;
    /**
     * <br>shrink drawable
     */
    private Drawable drawableShrink;
    /**
     * <br>expand drawable
     */
    private Drawable drawableExpand;
    /**
     * <br>color of shrink/expand text
     */
    private int textViewStateColor;
    /**
     * <br>expand text
     */
    private String textExpand;
    /**
     * <br>shrink text
     */
    private String textShrink;

    /**
     * <br>flag of shrink/expand
     */
    private boolean isShrink = false;
    /**
     * <br>flag of expand needed
     */
    private boolean isExpandNeeded = false;
    /**
     * <br>flag of TextView Initialization
     */
    private boolean isInitTextView = true;
    /**
     * <br>number of lines to expand
     */
    private int expandLines;
    /**
     * <br>Original number of lines
     */
    private int textLines;
    /**
     * <br>content text
     */
    private CharSequence textContent;
    /**
     * <br>content color
     */
    private int textContentColor;
    /**
     * <br>content text size, default = 14sp
     */
    private float textContentSize = 14;
    /**
     * <br>thread
     */
    private Thread thread;
    /**
     * <br>animation interval, default = 10
     */
    private int sleepTime = 10;

    /**
     * Show line at bottom text, default = true
     */
    private boolean showLine = true;

    @SuppressLint("HandlerLeak") private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override public void handleMessage(Message msg) {
            if (WHAT == msg.what) {
                textView.setMaxLines(msg.arg1);
                textView.invalidate();
            } else if (WHAT_ANIMATION_END == msg.what) {
                setExpandState(msg.arg1);
            } else if (WHAT_EXPAND_ONLY == msg.what) {
                changeExpandState(msg.arg1);
            }
            super.handleMessage(msg);
        }
    };

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValue(context, attrs);
        initView(context);
        initClick();
    }

    private void initValue(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);

        expandLines = ta.getInteger(R.styleable.ExpandableTextView_etv_expandLines, 5);

        drawableShrink = ta.getDrawable(R.styleable.ExpandableTextView_etv_shrinkBitmap);
        drawableExpand = ta.getDrawable(R.styleable.ExpandableTextView_etv_expandBitmap);

        textViewStateColor = ta.getColor(R.styleable.ExpandableTextView_etv_textStateColor,
                ContextCompat.getColor(context, android.R.color.darker_gray));

        textShrink = ta.getString(R.styleable.ExpandableTextView_etv_textShrink);
        textExpand = ta.getString(R.styleable.ExpandableTextView_etv_textExpand);

        if (null == drawableShrink) {
            drawableShrink =
                    ContextCompat.getDrawable(context, R.drawable.widget_icon_up);
        }

        if (null == drawableExpand) {
            drawableExpand =
                    ContextCompat.getDrawable(context, R.drawable.widget_icon_arrow);
        }

        if (TextUtils.isEmpty(textShrink)) {
            textShrink = " ";
        }

        if (TextUtils.isEmpty(textExpand)) {
            textExpand = " ";
        }

        showLine = ta.getBoolean(R.styleable.ExpandableTextView_etv_showLine, showLine);

        textContentColor = ta.getColor(R.styleable.ExpandableTextView_etv_textContentColor,
                ContextCompat.getColor(context, android.R.color.white));
        textContentSize = ta.getDimension(R.styleable.ExpandableTextView_etv_textContentSize,
                textContentSize);

        sleepTime = ta.getInt(R.styleable.ExpandableTextView_etv_animationTime, sleepTime);

        ta.recycle();
    }

    private void initView(Context context) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_textview_expand_animation, this);

        rlToggleLayout =
                (RelativeLayout) findViewById(R.id.rl_expand_text_view_animation_toggle_layout);

        textView = (TextView) findViewById(R.id.tv_expand_text_view_animation);
        textView.setTextColor(textContentColor);
        textView.getPaint().setTextSize(textContentSize);

        ivExpandOrShrink = (ImageView) findViewById(R.id.iv_expand_text_view_animation_toggle);

        tvState = (TextView) findViewById(R.id.tv_expand_text_view_animation_hint);
        tvState.setTextColor(textViewStateColor);

    }

    private void initClick() {
        textView.setOnClickListener(this);
        rlToggleLayout.setOnClickListener(this);
    }

    public void setText(CharSequence charSequence) {

        textContent = charSequence;

        textView.setText(charSequence.toString());

        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override public boolean onPreDraw() {
                if (!isInitTextView) {
                    return true;
                }
                textLines = textView.getLineCount();
                isExpandNeeded = textLines > expandLines;
                isInitTextView = false;
                if (isExpandNeeded) {
                    isShrink = true;
                    doAnimation(expandLines, expandLines, WHAT_ANIMATION_END);
                } else {
                    isShrink = false;
                    doNotExpand();
                }
                return true;
            }
        });

        if (!isInitTextView) {
            textLines = textView.getLineCount();
        }
    }

    /**
     * @param startIndex <br> start index of animation
     * @param endIndex  <br> end index of animation
     * @param what handler <br> signal of animation end
     */
    private void doAnimation(final int startIndex, final int endIndex, final int what) {

        thread = new Thread(new Runnable() {

            @Override public void run() {

                if (startIndex < endIndex) {
                    // if start index smaller than end index ,do expand action
                    int count = startIndex;
                    while (count++ < endIndex) {
                        Message msg = handler.obtainMessage(WHAT, count, 0);

                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.sendMessage(msg);
                    }
                } else if (startIndex > endIndex) {
                    // if start index bigger than end index ,do shrink action
                    int count = startIndex;
                    while (count-- > endIndex) {
                        Message msg = handler.obtainMessage(WHAT, count, 0);
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.sendMessage(msg);
                    }
                }

                // animation end,send signal
                Message msg = handler.obtainMessage(what, endIndex, 0);
                handler.sendMessage(msg);
            }
        });

        thread.start();
    }

    /**
     * change shrink/expand state(only change state,but not hide shrink/expand icon)
     */
    @SuppressWarnings("deprecation") private void changeExpandState(int endIndex) {
        rlToggleLayout.setVisibility(View.VISIBLE);
        if (endIndex < textLines) {
            ivExpandOrShrink.setBackgroundDrawable(drawableExpand);
            tvState.setText(textExpand);
        } else {
            ivExpandOrShrink.setBackgroundDrawable(drawableShrink);
            tvState.setText(textShrink);
        }
    }

    /**
     * change shrink/expand state(if number of expand lines bigger than original text lines,hide
     * shrink/expand icon,and TextView will always be at expand state)
     */
    @SuppressWarnings("deprecation") private void setExpandState(int endIndex) {

        if (endIndex < textLines) {
            isShrink = true;
            rlToggleLayout.setVisibility(View.VISIBLE);
            ivExpandOrShrink.setBackgroundDrawable(drawableExpand);
            textView.setOnClickListener(this);
            tvState.setText(textExpand);
        } else {
            isShrink = false;
            rlToggleLayout.setVisibility(View.GONE);
            ivExpandOrShrink.setBackgroundDrawable(drawableShrink);
            textView.setOnClickListener(null);
            tvState.setText(textShrink);
        }
    }

    /**
     * do not expand
     */
    private void doNotExpand() {
        textView.setMaxLines(expandLines);
        rlToggleLayout.setVisibility(View.GONE);
        textView.setOnClickListener(null);
    }

    @Override public void onClick(View v) {
        if (v.getId() == R.id.rl_expand_text_view_animation_toggle_layout
                || v.getId() == R.id.tv_expand_text_view_animation) {
            clickImageToggle();
            if (null != onStateChangeListener) onStateChangeListener.onStateChange(isShrink);
        }
    }

    private void clickImageToggle() {
        if (isShrink) {
            // do shrink action
            doAnimation(expandLines, textLines, WHAT_EXPAND_ONLY);
        } else {
            // do expand action
            doAnimation(textLines, expandLines, WHAT_EXPAND_ONLY);
        }
        // set flag
        isShrink = !isShrink;
    }

    public void resetState(boolean isShrink) {

        this.isShrink = isShrink;
        if (textLines > expandLines) {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (isShrink) {
                rlToggleLayout.setVisibility(View.VISIBLE);
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ivExpandOrShrink.setBackgroundDrawable(drawableExpand);
                } else {
                    ivExpandOrShrink.setBackground(drawableExpand);
                }
                textView.setOnClickListener(this);
                textView.setMaxLines(expandLines);
                tvState.setText(textExpand);
            } else {
                rlToggleLayout.setVisibility(View.VISIBLE);
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ivExpandOrShrink.setBackgroundDrawable(drawableShrink);
                } else {
                    ivExpandOrShrink.setBackground(drawableShrink);
                }
                textView.setOnClickListener(this);
                textView.setMaxLines(textLines);
                tvState.setText(textShrink);
            }
        } else {
            doNotExpand();
        }
    }

    public Drawable getDrawableShrink() {
        return drawableShrink;
    }

    public void setDrawableShrink(Drawable drawableShrink) {
        this.drawableShrink = drawableShrink;
    }

    public Drawable getDrawableExpand() {
        return drawableExpand;
    }

    public void setDrawableExpand(Drawable drawableExpand) {
        this.drawableExpand = drawableExpand;
    }

    public int getExpandLines() {
        return expandLines;
    }

    public void setExpandLines(int newExpandLines) {
        int start = isShrink ? this.expandLines : textLines;
        int end = textLines < newExpandLines ? textLines : newExpandLines;
        doAnimation(start, end, WHAT_ANIMATION_END);
        this.expandLines = newExpandLines;
    }

    /**
     * get content text
     *
     * @return content text
     */
    public CharSequence getTextContent() {
        return textContent;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public interface OnStateChangeListener {
        void onStateChange(boolean isShrink);
    }
}