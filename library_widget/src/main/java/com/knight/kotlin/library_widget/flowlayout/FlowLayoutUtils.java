package com.knight.kotlin.library_widget.flowlayout;

import android.text.TextPaint;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author created by knight
 * @organize wanandroid
 * @Date 2021/7/2 14:44
 * @descript:
 */
public class FlowLayoutUtils {
    /**
     *
     * @param tagInfos 需要排列的标签数据
     * @param width 一行标签的宽度
     * @param textSize
     * @param textViewSpacing 标签的间距
     * @param padding 标签文字和边界的距离
     * @return
     */
    public static SparseArray<ArrayList<TagInfo>> getRow(List<TagInfo> tagInfos, int width, int textSize, int textViewSpacing, int padding) {
        SparseArray<ArrayList<TagInfo>> sparseArray = new SparseArray();
        int totalWidth = 0;
        int row = 0;
        int measuredWidth;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        ArrayList<TagInfo> tagInfoList;
        for (int i = 0; i < tagInfos.size(); i++) {//25,20,15
            measuredWidth = (int) (textPaint.measureText(tagInfos.get(i).tagName) + padding);
            if (totalWidth == 0) {
                totalWidth += measuredWidth;
            } else {
                totalWidth += measuredWidth + textViewSpacing;
            }
            if (totalWidth > width) {
                row++;
                totalWidth = measuredWidth;
            }
            tagInfoList = sparseArray.get(row);
            if (tagInfoList == null) {
                tagInfoList = new ArrayList<>();
                sparseArray.put(row,tagInfoList);
            }
            tagInfoList.add(tagInfos.get(i));
        }
        return sparseArray;
    }

    public static SparseArray<ArrayList<TagInfo>> getTagRects(List<TagInfo> tagInfos, int marginTop, int width, int textSize, int height, int textViewSpacing, int verticalSpacing, int padding, onGetTagListener onGetTagListener) {
        SparseArray<ArrayList<TagInfo>> sparseArray = new SparseArray();
        ArrayList<TagInfo> tagInfoList;
        int totalWidth = 0;
        int row = 0;
        int measuredWidth;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        TagInfo tagInfo;
        for (int i = 0; i < tagInfos.size(); i++) {
            tagInfo = tagInfos.get(i);
            measuredWidth = (int) (textPaint.measureText(tagInfo.tagName) + padding);
            if (totalWidth == 0) {
                totalWidth += measuredWidth;
            } else {
                totalWidth += measuredWidth + textViewSpacing;
            }
            if (totalWidth > width) {
                row++;
                totalWidth = measuredWidth;
            }
            tagInfo.rect.left = totalWidth - measuredWidth;
            tagInfo.rect.top = marginTop + row * (height + verticalSpacing);
            tagInfo.rect.right = totalWidth;
            tagInfo.rect.bottom =marginTop + row * (height + verticalSpacing) + height;
            tagInfoList = sparseArray.get(row);
            if (tagInfoList == null) {
                tagInfoList = new ArrayList<>();
                sparseArray.put(row,tagInfoList);
            }
            tagInfoList.add(tagInfos.get(i));
            if (onGetTagListener != null) {
                onGetTagListener.onGetTag(i,tagInfo);
            }
        }
        return sparseArray;
    }

    public interface onGetTagListener{
        void onGetTag(int position, TagInfo tagInfo);
    }
}
