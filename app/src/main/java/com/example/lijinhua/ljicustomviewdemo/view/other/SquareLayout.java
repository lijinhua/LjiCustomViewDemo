package com.example.lijinhua.ljicustomviewdemo.view.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lijinhua on 2016/4/14.
 * http://blog.csdn.net/aigestudio/article/details/43378131
 */
public class SquareLayout extends ViewGroup {


    private static final int ORIENTATION_HORIZONTAL = 0, ORIENTATION_VERTICAL = 1;// 排列方向的常量标识值
    private static final int DEFAULT_MAX_ROW = Integer.MAX_VALUE, DEFAULT_MAX_COLUMN = Integer.MAX_VALUE;// 最大行列默认值

    private int mMaxRow = DEFAULT_MAX_ROW;// 最大行数
    private int mMaxColumn = DEFAULT_MAX_COLUMN;// 最大列数

    private int mOrientation = ORIENTATION_HORIZONTAL;// 排列方向默认横向


    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * ViewGroup和LayoutParams之间的关系
     * 当在LinearLayout中写childView的时候，可以写layout_gravity，layout_weight属性；
     * 在RelativeLayout中的childView有layout_centerInParent属性，却没有layout_gravity，layout_weight，
     * 这是为什么呢？这是因为每个ViewGroup需要指定一个LayoutParams，用于确定支持childView支持哪些属性，
     * 比如LinearLayout指定LinearLayout.LayoutParams等。如果大家去看LinearLayout的源码，
     * 会发现其内部定义了LinearLayout.LayoutParams，在此类中，你可以发现weight和gravity的身影
     * <p/>
     * 总结一点:就是如果想让子控件支持margin直接使用系统的MarginLayoutParams
     *
     * @return
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 重写checkLayoutParams方法去验证当前所使用的LayoutParams对象是否MarginLayoutParams
     *
     * @return
     */
    @Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*
         * 声明临时变量存储父容器的期望值
         * 该值应该等于父容器的内边距加上所有子元素的测量宽高和外边距
         */
        int parentDesireWidth = 0;
        int parentDesireHeight = 0;

        // 声明临时变量存储子元素的测量状态
        int childMeasureState = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 获取对应遍历下标的子元素
            View child = getChildAt(i);
             /*
              * 如果该子元素没有以“不占用空间”的方式隐藏则表示其需要被测量计算
              */
            if (child.getVisibility() != View.GONE) {
                continue;
            }
            // 测量子元素并考量其外边距
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            // 比较子元素测量宽高并比较取其较大值
            int childMeasureSize = Math.max(child.getMeasuredWidth(), child.getMeasuredHeight());
            // 重新封装子元素测量规格
            int childMeasureSpec = MeasureSpec.makeMeasureSpec(childMeasureSize, MeasureSpec.EXACTLY);
            // 重新测量子元素
            child.measure(childMeasureSpec, childMeasureSpec);
            // 获取子元素布局参数
            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
            /*
             * 考量外边距计算子元素实际宽高
             */
            int childActualWidth = child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
            int childActualHeight = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;
             /*
              * 如果为横向排列
              */
            if (mOrientation == ORIENTATION_HORIZONTAL) {
                // 累加子元素的实际宽度
                parentDesireWidth += childActualWidth;
                // 获取子元素中高度最大值
                parentDesireHeight = Math.max(parentDesireHeight, childActualHeight);
            } else if (mOrientation == ORIENTATION_VERTICAL) {/* * 如果为竖向排列*/
                // 累加子元素的实际高度
                parentDesireHeight += childActualHeight;

                // 获取子元素中宽度最大值
                parentDesireWidth = Math.max(parentDesireWidth, childActualWidth);
            }
            // 合并子元素的测量状态
            childMeasureState = combineMeasuredStates(childMeasureState, child.getMeasuredState());
        }
        /*
        * 考量父容器内边距将其累加到期望值
        */
        parentDesireWidth += getPaddingLeft() + getPaddingRight();
        parentDesireHeight += getPaddingTop() + getPaddingBottom();
        /*
         * 尝试比较父容器期望值与Android建议的最小值大小并取较大值
         */
        parentDesireWidth = Math.max(parentDesireWidth, getSuggestedMinimumWidth());
        parentDesireHeight = Math.max(parentDesireHeight, getSuggestedMinimumHeight());
        // 确定父容器的测量宽高
        /**
         * 那么这个resolveSize方法其实是View提供给我们解算尺寸大小的一个工具方法，
         * 其具体实现在API 11后交由另一个方法resolveSizeAndState也就是我们这一节例子所用到的去处理
         * 具体看http://blog.csdn.net/aigestudio/article/details/43378131
         */
        setMeasuredDimension(resolveSizeAndState(parentDesireWidth, widthMeasureSpec, childMeasureState),
                resolveSizeAndState(parentDesireHeight, heightMeasureSpec, childMeasureState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        // 声明临时变量存储宽高倍增值
        int multi = 0;
         /*
             * 遍历子元素
             */
        for (int i = 0; i < getChildCount(); i++) {
            // 获取对应遍历下标的子元素
            View child = getChildAt(i);
            /*
              * 如果该子元素没有以“不占用空间”的方式隐藏则表示其需要被测量计算
              */
            if (child.getVisibility() != View.GONE) {
                continue;
            }
            // 获取子元素布局参数
            MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();

            // 获取控件尺寸
            int childActualSize = child.getMeasuredWidth();// child.getMeasuredHeight()

            /*
             * 如果为横向排列
             */
            if (mOrientation == ORIENTATION_HORIZONTAL) {
                // 确定子元素左上、右下坐标
                child.layout(getPaddingLeft() + mlp.leftMargin + multi, getPaddingTop() + mlp.topMargin, childActualSize + getPaddingLeft()
                        + mlp.leftMargin + multi, childActualSize + getPaddingTop() + mlp.topMargin);

                // 累加倍增值
                multi += childActualSize + mlp.leftMargin + mlp.rightMargin;
            } else if (mOrientation == ORIENTATION_VERTICAL) {/* * 如果为竖向排列*/
                // 确定子元素左上、右下坐标
                child.layout(getPaddingLeft() + mlp.leftMargin, getPaddingTop() + mlp.topMargin + multi, childActualSize + getPaddingLeft()
                        + mlp.leftMargin, childActualSize + getPaddingTop() + mlp.topMargin + multi);

                // 累加倍增值
                multi += childActualSize + mlp.topMargin + mlp.bottomMargin;
            }
        }
    }
}
