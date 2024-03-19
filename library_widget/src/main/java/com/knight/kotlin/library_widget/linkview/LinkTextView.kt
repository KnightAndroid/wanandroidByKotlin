package com.knight.kotlin.library_widget.linkview


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.DynamicLayout
import android.text.Spannable
import android.text.SpannableString
import android.text.StaticLayout
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.knight.kotlin.library_widget.R
import java.lang.reflect.Field
import java.util.LinkedList
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Author:Knight
 * Time:2024/3/18 15:51
 * Description:LinkTextView
 */
class LinkTextView : AppCompatTextView {
    private var autoLinkOnClickListener: LinkOnClickListener? = null
    private lateinit var autoLinkModes: Array<LinkMode>
    private var mBoldAutoLinkModes: MutableList<LinkMode>? = null
    private var customRegex: String? = null
    private var isUnderLineEnabled = false

    /**
     * 高亮文字颜色
     */
    private var highLightColor = 0

    /**
     * 默认文字颜色
     */
    private var defaultSelectedColor = 0

    //自定义各种高亮文字颜色
    private var mentionModeColor = 0
    private var hashtagModeColor = 0
    private var urlModeColor = 0
    private var phoneModeColor = 0
    private var emailModeColor = 0
    private var customModeColor = 0

    constructor(context: Context?) : super(context!!)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.LinkTextView)
        highLightColor =
            array.getColor(R.styleable.LinkTextView_highlight_textcolor, Color.BLUE)
        defaultSelectedColor =
            array.getColor(R.styleable.LinkTextView_default_textcolor, Color.WHITE)
        setHighLightColor()
    }

    override fun setText(text: CharSequence, type: BufferType?) {
        if (TextUtils.isEmpty(text)) {
            super.setText(text, type)
            return
        }
        setTextColor(defaultSelectedColor)
        val spannableString = makeSpannableString(text)
        movementMethod = LinkTouchMovementMethod()
        super.setText(spannableString, type)
    }

    /**
     * 设置高亮颜色
     */
    fun setHighLightColor() {
        mentionModeColor = highLightColor
        hashtagModeColor = highLightColor
        urlModeColor = highLightColor
        phoneModeColor = highLightColor
        emailModeColor = highLightColor
        customModeColor = highLightColor
    }

    private fun makeSpannableString(text: CharSequence): SpannableString {
        val spannableString = SpannableString(text)
        val autoLinkItems: List<LinkItem> = matchedRanges(text)
        for (autoLinkItem in autoLinkItems) {
            val currentColor = getColorByMode(autoLinkItem.linkMode)
            val clickableSpan: TouchableSpan =
                object : TouchableSpan(currentColor, defaultSelectedColor, isUnderLineEnabled) {
                    override fun onClick(widget: View) {
                        autoLinkOnClickListener?.onAutoLinkTextClick(
                            autoLinkItem.linkMode,
                            autoLinkItem.matchedText
                        )
                    }
                }
            spannableString.setSpan(
                clickableSpan,
                autoLinkItem.startPoint,
                autoLinkItem.endPoint,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (mBoldAutoLinkModes != null && mBoldAutoLinkModes!!.contains(autoLinkItem.linkMode)) {
                spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    autoLinkItem.startPoint,
                    autoLinkItem.endPoint,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableString
    }

    private fun matchedRanges(text: CharSequence): List<LinkItem> {
        val autoLinkItems: MutableList<LinkItem> = LinkedList<LinkItem>()
        if (autoLinkModes == null) {
            return LinkedList<LinkItem>()
        }
        for (anAutoLinkMode in autoLinkModes) {
            val regex: String = LinkUtils.getRegexByAutoLinkMode(anAutoLinkMode, customRegex)
            val pattern: Pattern = Pattern.compile(regex)
            val matcher: Matcher = pattern.matcher(text)
            if (anAutoLinkMode === LinkMode.MODE_PHONE) {
                while (matcher.find()) {
                    if (matcher.group().length > MIN_PHONE_NUMBER_LENGTH) autoLinkItems.add(
                        LinkItem(
                            matcher.start(),
                            matcher.end(),
                            matcher.group(),
                            anAutoLinkMode
                        )
                    )
                }
            } else {
                while (matcher.find()) {
                    autoLinkItems.add(
                        LinkItem(
                            matcher.start(),
                            matcher.end(),
                            matcher.group(),
                            anAutoLinkMode
                        )
                    )
                }
            }
        }
        return autoLinkItems
    }

    private fun getColorByMode(autoLinkMode: LinkMode): Int {
        return when (autoLinkMode) {
            LinkMode.MODE_HASHTAG -> hashtagModeColor
            LinkMode.MODE_MENTION -> mentionModeColor
            LinkMode.MODE_URL -> urlModeColor
            LinkMode.MODE_PHONE -> phoneModeColor
            LinkMode.MODE_EMAIL -> emailModeColor
            LinkMode.MODE_CUSTOM -> customModeColor
            else -> highLightColor
        }
    }

    fun setMentionModeColor(@ColorInt mentionModeColor: Int) {
        this.mentionModeColor = mentionModeColor
    }

    fun setHashtagModeColor(@ColorInt hashtagModeColor: Int) {
        this.hashtagModeColor = hashtagModeColor
    }

    fun setUrlModeColor(@ColorInt urlModeColor: Int) {
        this.urlModeColor = urlModeColor
    }

    fun setPhoneModeColor(@ColorInt phoneModeColor: Int) {
        this.phoneModeColor = phoneModeColor
    }

    fun setEmailModeColor(@ColorInt emailModeColor: Int) {
        this.emailModeColor = emailModeColor
    }

    fun setCustomModeColor(@ColorInt customModeColor: Int) {
        this.customModeColor = customModeColor
    }

    fun setSelectedStateColor(@ColorInt defaultSelectedColor: Int) {
        this.defaultSelectedColor = defaultSelectedColor
    }

    fun addAutoLinkMode(vararg autoLinkModes: LinkMode) {
        this.autoLinkModes = autoLinkModes.asList().toTypedArray()
    }

    fun setBoldAutoLinkModes(vararg linkModes: LinkMode) {
        mBoldAutoLinkModes = ArrayList<LinkMode>()
        mBoldAutoLinkModes!!.addAll(linkModes.asList())
    }

    fun setCustomRegex(regex: String?) {
        customRegex = regex
    }

    fun setAutoLinkOnClickListener(autoLinkOnClickListener: LinkOnClickListener) {
        this.autoLinkOnClickListener = autoLinkOnClickListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (Build.VERSION.SDK_INT >= 16) {
            var layout: StaticLayout? = null
            var field: Field? = null
            try {
                val staticField: Field = DynamicLayout::class.java.getDeclaredField("sStaticLayout")
                staticField.setAccessible(true)
                layout = staticField.get(DynamicLayout::class.java) as? StaticLayout
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            if (layout != null) {
                try {
                    field = StaticLayout::class.java.getDeclaredField("mMaximumVisibleLineCount")
                    field.setAccessible(true)
                    field.setInt(layout, maxLines)
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            if (layout != null && field != null) {
                try {
                    field.setInt(layout, Int.MAX_VALUE)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun enableUnderLine() {
        isUnderLineEnabled = true
    }

    companion object {
        val TAG = LinkTextView::class.java.simpleName
        private const val MIN_PHONE_NUMBER_LENGTH = 8
    }
}