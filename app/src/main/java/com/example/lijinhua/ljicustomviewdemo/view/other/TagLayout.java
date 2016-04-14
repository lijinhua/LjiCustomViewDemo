package com.example.lijinhua.ljicustomviewdemo.view.other;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by lijinhua on 2016/4/13.
 * http://javatechig.com/android/how-to-create-custom-layout-in-android-by-extending-viewgroup-class
 */
public class TagLayout extends ViewGroup {

    int deviceWidth;

    public TagLayout(Context context) {
        this(context, null, 0);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;
        int mLeftWidth = 0;
        int rowCount = 0; // 一行有几个View

        // 声明临时变量存储子元素的测量状态
        int childState = 0;

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }
            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();

            if ((mLeftWidth / deviceWidth) > rowCount) {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            // 合并子元素的测量状态
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childRight = getMeasuredWidth() - getPaddingRight();
        final int childBottom = getMeasuredHeight() - getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                return;

            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            if (curLeft + curWidth >= childRight) {
                // 需要换一行了
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }

            //  do the layout
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            // store the max height
            if (maxHeight < curHeight) {
                maxHeight = curHeight;
            }
            curLeft += curWidth;
        }
    }
}
