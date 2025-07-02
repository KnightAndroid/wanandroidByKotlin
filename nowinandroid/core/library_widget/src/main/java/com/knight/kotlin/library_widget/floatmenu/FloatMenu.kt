package com.knight.kotlin.library_widget.floatmenu

import android.content.Context
import android.content.res.XmlResourceParser
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Xml
import android.view.Gravity
import android.view.InflateException
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.core.library_base.util.dp2px
import com.knight.kotlin.library_widget.R
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

/**
 * Author:Knight
 * Time:2022/5/18 9:50
 * Description:FloatMenu 方方正正悬浮窗
 */
class FloatMenu constructor(mContext: Context,mView: View):PopupWindow(mContext) {
    /** Menu tag name in XML.  */
    private val XML_MENU = "menu"

    /** Group tag name in XML.  */
    private val XML_GROUP = "group"

    /** Item tag name in XML.  */
    private val XML_ITEM = "item"

    private val ANCHORED_GRAVITY = Gravity.TOP or Gravity.START


    private var DEFAULT_MENU_WIDTH = 0
    private var VERTICAL_OFFSET = 0

    private val context: Context = mContext
    private var menuItemList: ArrayList<MenuItem>? = null
    private val view: View = mView
    private var screenPoint: Point? = null
    private var clickX = 0
    private var clickY = 0
    private var menuWidth = 0
    private var menuHeight = 0
    private var menuLayout: LinearLayout? = null
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(v: View?, position: Int)
    }

    init {
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(BitmapDrawable())
        view.setOnTouchListener(MenuTouchListener())
        VERTICAL_OFFSET = 10.dp2px()
        DEFAULT_MENU_WIDTH = 180.dp2px()
        screenPoint = Display.getScreenMetrics(context)
        menuItemList = ArrayList()

    }

    fun inflate(menuRes: Int) {
        inflate(menuRes, DEFAULT_MENU_WIDTH)
    }

    fun inflate(menuRes: Int, itemWidth: Int) {
        var parser: XmlResourceParser? = null
        try {
            parser = context.resources.getLayout(menuRes)
            val attrs = Xml.asAttributeSet(parser)
            parseMenu(parser, attrs)
        } catch (e: XmlPullParserException) {
            throw InflateException("Error inflating menu XML", e)
        } catch (e: IOException) {
            throw InflateException("Error inflating menu XML", e)
        } finally {
            parser?.close()
        }
        generateLayout(itemWidth)
    }

    fun items(vararg items: String?) {
        items(DEFAULT_MENU_WIDTH, *items)
    }

    fun items(itemWidth: Int, vararg items: String?) {
        menuItemList?.clear()
        for (i in 0 until items.size) {
            val menuModel = MenuItem()
            menuModel.setItem(items[i])
            menuItemList?.add(menuModel)
        }
        generateLayout(itemWidth)
    }

    private fun generateLayout(itemWidth: Int) {
        menuLayout = LinearLayout(context)
        menuLayout?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.widget_shadow
            )
        )
        menuLayout?.setOrientation(LinearLayout.VERTICAL)
        val padding: Int = 12.dp2px()
        for (i in menuItemList!!.indices) {
            val textView = TextView(context)
            textView.isClickable = true
            textView.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.widget_item_selector
                )
            )
            textView.setPadding(padding, padding, padding, padding)
            textView.width = itemWidth
            textView.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            textView.textSize = 15f
            textView.setTextColor(Color.BLACK)
            val menuModel = menuItemList!![i]
            if (menuModel.getItemResId() !== View.NO_ID) {
                val drawable = ContextCompat.getDrawable(context, menuModel.getItemResId())
                drawable!!.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                textView.compoundDrawablePadding = 12.dp2px()
                textView.setCompoundDrawables(drawable, null, null, null)
            }
            textView.text = menuModel.getItem()
            if (onItemClickListener != null) {
                textView.setOnClickListener(ItemOnClickListener(i))
            }
            menuLayout?.addView(textView)
        }
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        menuLayout?.measure(width, height)
        menuWidth = menuLayout?.getMeasuredWidth() ?: 0
        menuHeight = menuLayout?.getMeasuredHeight() ?: 0
        contentView = menuLayout
        setWidth(menuWidth)
        setHeight(menuHeight)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseMenu(parser: XmlPullParser, attrs: AttributeSet) {
        var eventType = parser.eventType
        var tagName: String
        var lookingForEndOfUnknownTag = false
        var unknownTagName: String? = null

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.name
                if (tagName == XML_MENU) {
                    // Go to next tag
                    eventType = parser.next()
                    break
                }
                throw RuntimeException("Expecting menu, got $tagName")
            }
            eventType = parser.next()
        } while (eventType != XmlPullParser.END_DOCUMENT)
        var reachedEndOfMenu = false
        while (!reachedEndOfMenu) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (lookingForEndOfUnknownTag) {
                        break
                    }
                    tagName = parser.name
                    if (tagName ==XML_GROUP) {
                        //	parser group
                        parser.next()
                    } else if (tagName == XML_ITEM) {
                        readItem(attrs)
                    } else if (tagName == XML_MENU) {
                        // A menu start tag denotes a submenu for an item
                        //pares subMenu
                        parser.next()
                    } else {
                        lookingForEndOfUnknownTag = true
                        unknownTagName = tagName
                    }
                }
                XmlPullParser.END_TAG -> {
                    tagName = parser.name
                    if (lookingForEndOfUnknownTag && tagName == unknownTagName) {
                        lookingForEndOfUnknownTag = false
                        unknownTagName = null
                    } else if (tagName == XML_GROUP) {
                    } else if (tagName == XML_ITEM) {
                    } else if (tagName == XML_MENU) {
                        reachedEndOfMenu = true
                    }
                }
                XmlPullParser.END_DOCUMENT -> throw RuntimeException("Unexpected end of document")
            }
            eventType = parser.next()
        }
    }

    private fun readItem(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MenuItem)
        val itemTitle = a.getText(R.styleable.MenuItem_menu_title)
        val itemIconResId = a.getResourceId(R.styleable.MenuItem_icon, View.NO_ID)
        val menu = MenuItem()
        menu.setItem(itemTitle.toString())
        if (itemIconResId != View.NO_ID) {
            menu.setItemResId(itemIconResId)
        }
        menuItemList?.add(menu)
        a.recycle()
    }

    fun show(point: Point) {
        clickX = point.x
        clickY = point.y
        show()
    }

    fun show() {
        if (isShowing) {
            return
        }
        //it is must ,other wise 'setOutsideTouchable' will not work under Android5.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundDrawable(BitmapDrawable())
        }
        if (clickX <= screenPoint!!.x / 2) {
            if (clickY + menuHeight < screenPoint!!.y) {
                animationStyle = R.style.Animation_top_left
                showAtLocation(view, ANCHORED_GRAVITY, clickX, clickY + VERTICAL_OFFSET)
            } else {
                animationStyle = R.style.Animation_bottom_left
                showAtLocation(
                    view,
                    ANCHORED_GRAVITY,
                    clickX,
                    clickY - menuHeight - VERTICAL_OFFSET
                )
            }
        } else {
            if (clickY + menuHeight < screenPoint!!.y) {
                animationStyle = R.style.Animation_top_right
                showAtLocation(
                    view,
                    ANCHORED_GRAVITY,
                    clickX - menuWidth,
                    clickY + VERTICAL_OFFSET
                )
            } else {
                animationStyle = R.style.Animation_bottom_right
                showAtLocation(
                    view,
                    ANCHORED_GRAVITY,
                    clickX - menuWidth,
                    clickY - menuHeight - VERTICAL_OFFSET
                )
            }
        }
    }


    override fun setOnDismissListener(onDismissListener: OnDismissListener?) {
        super.setOnDismissListener(onDismissListener)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
        if (onItemClickListener != null) {
            for (i in 0 until menuLayout!!.childCount) {
                val view = menuLayout?.getChildAt(i)
                view?.setOnClickListener(ItemOnClickListener(i))
            }
        }
    }

    inner class MenuTouchListener : OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (event.action == MotionEvent.ACTION_DOWN) {
                clickX = event.rawX.toInt()
                clickY = event.rawY.toInt()
            }
            return false
        }
    }

    inner class ItemOnClickListener(var position: Int) : View.OnClickListener {
        override fun onClick(v: View) {
            dismiss()
            onItemClickListener?.let {
                it.onClick(v,position)
            }
        }
    }
}