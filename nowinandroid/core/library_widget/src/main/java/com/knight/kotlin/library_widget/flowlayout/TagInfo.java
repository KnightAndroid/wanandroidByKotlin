package com.knight.kotlin.library_widget.flowlayout;

import android.graphics.Rect;

/**
 * @author created by knight
 * @organize wanandroid
 * @Date 2021/7/2 14:45
 * @descript:
 */
public class TagInfo {
    public static final int TYPE_TAG_USER = 0;//可以移动的标签
    public static final int TYPE_TAG_SERVICE = 1;//不能移动的标签，一般是默认放在最前面的标签
    public String tagId;
    public String tagName;
    Rect rect = new Rect();
    public int childPosition;
    public int dataPosition = -1;
    public int type;
}
