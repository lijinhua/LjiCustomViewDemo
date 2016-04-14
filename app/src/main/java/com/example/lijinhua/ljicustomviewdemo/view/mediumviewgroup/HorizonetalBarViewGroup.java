package com.example.lijinhua.ljicustomviewdemo.view.mediumviewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lijinhua on 2016/4/13.
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1002/3540.html
 */
public class HorizonetalBarViewGroup extends ViewGroup {

    private static final String TAG = HorizonetalBarViewGroup.class.getSimpleName();

    public HorizonetalBarViewGroup(Context context) {
        super(context);
    }

    public HorizonetalBarViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizonetalBarViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizonetalBarViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * onMeasure()方法中告诉子view去测量自己。最简单的实现方法就是遍历子view，并对它们使用measureChild()方法
     * <p/>
     * <p/>
     * 目前，margins还不能工作。如果我们想支持margins，可以在我们容器的onLayout 里面添加它们而不是在测量一个子view的时候去考虑margins
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onMeasureNormal(widthMeasureSpec, heightMeasureSpec);
    }

    private void testData() {
        // 而当设置为 wrap_content时，容器传进去的是AT_MOST,
        // 表示子view的大小最多是多少，这样子view会根据这个上限来设置自己的尺寸。当子view的大小设置为精确值时，容器传入的是EXACTLY,
        // 而MeasureSpec的UNSPECIFIED模式目前还没有发现在什么情况下使用
        int specMode1 = MeasureSpec.getMode(MeasureSpec.EXACTLY);
        int specSize1 = MeasureSpec.getSize(MeasureSpec.EXACTLY);
        int specMode2 = MeasureSpec.getMode(MeasureSpec.AT_MOST);
        int specSize2 = MeasureSpec.getSize(MeasureSpec.AT_MOST);
        int specMode3 = MeasureSpec.getMode(MeasureSpec.UNSPECIFIED);
        int specSize3 = MeasureSpec.getSize(MeasureSpec.UNSPECIFIED);
        Log.v(TAG, "EXACTLY specMode = " + specMode1);
        Log.v(TAG, "EXACTLY specSize = " + specSize1);
        Log.v(TAG, "AT_MOST specMode = " + specMode2);
        Log.v(TAG, "AT_MOST specSize = " + specSize2);
        Log.v(TAG, "UNSPECIFIED specMode = " + specMode3);
        Log.v(TAG, "UNSPECIFIED specSize = " + specSize3);
    }

    private int measureLength(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        // Default size if no limits are specified.
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            // Calculate the ideal size of your
            // control within this maximum size.
            // If your control fills the available
            // space return the outer bound.
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // If your control can fit within these bounds return that value.
            result = specSize;
        }
        return result;
    }

    private void onMeasureNormal(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 需要调用super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         * 经典写法
         */

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = measureLength(heightMeasureSpec);
        int measuredWidth = measureLength(widthMeasureSpec);
        // 这里面只适配了EXACTLY情况
        // AT_MOST 就需要for来计刷了,

        Log.v(TAG, "onMeasureNormal measuredWidth = " + measuredWidth + "===measuredHeight===" + measuredHeight);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private void onMeasureDeimen300(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         *  高度第一个子view都固定为300, 不需要super.onMeasure
         */
        int totalWidth = 0;
        int totalHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            int width = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
            int height = MeasureSpec.makeMeasureSpec(300, MeasureSpec.EXACTLY);
            child.measure(width, height);
            totalWidth += width;
            if (height > totalHeight) {
                // height of the container, will be the largest height.
                totalHeight = child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private void onMeasureDimension(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 现在你不应该再调用 super.onMeasure()了
         * 目前为止，我们还没有告诉容器去测量自己，假设我们想在容器中使用wrap_content ？为此我们需要知道所有子view的总宽度与最高子view的高度。
         * 如果我们能够计算出这两个值，我们就能使用setMeasuredDimension()来设置容器的宽度和高度。用下面的代码来替换onMeasure中的内容
         */
        int totalWidth = 0;
        int totalHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            totalWidth += child.getMeasuredWidth();
            if (child.getMeasuredHeight() > totalHeight) {
                //height of the container, will be the largest height.
                totalHeight = child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private void onMeasureChild(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 调用这个方法当前的ViewGroup会整整个屏幕
         * 而子view显示正常
         * 分析:因为这里面只测量了子view,measureChild
         */
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void onMeasureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 区别: measureChild与measureChildren
         * measureChild 是测量单个View,如果使用循环来测量，只要是当前ViewGroup下不管是可见还是不可见的都进行测量
         *
         * 用measureChildren() 方法来简化onMeasureChild(widthMeasureSpec,heightMeasureSpec)上面的代码
         * 这个方法将自动遍历所有子view并让它们测量自己。这个方法还可以忽略那些visibility 设置为gone的子view，因此它支持visibility gone标志
         *
         */
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int prevChildRight = 0;
        int prevChildBottom = 0;

        /**
         * •Left —  view的x轴起始位置
         * •Top —  view的y轴起始位置
         * •Right —  view的x轴结束位置
         * •Bottom —  view的y轴结束位置
         */
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            child.layout(prevChildRight, prevChildBottom, prevChildRight + child.getMeasuredWidth(), prevChildBottom + child.getMeasuredHeight());
            prevChildRight += child.getMeasuredWidth();
        }
    }

}
