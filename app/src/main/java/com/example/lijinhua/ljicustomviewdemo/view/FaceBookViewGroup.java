package com.example.lijinhua.ljicustomviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijinhua.ljicustomviewdemo.R;

/**
 * Created by lijinhua on 2016/4/13.
 * 学习文章使用facebook来自下定义控件
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0524/2920.html#commettop
 */
public class FaceBookViewGroup extends ViewGroup {
    private final ImageView mProfileImage,mIcon;
    private final TextView mAuthorText;
    private final TextView mMessageText;

    public FaceBookViewGroup(Context context) {
        this(context, null);
    }

    public FaceBookViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceBookViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_view, this, true);
        mIcon = (ImageView) findViewById(R.id.iv2);
        mProfileImage = (ImageView) findViewById(R.id.iv);
        mAuthorText = (TextView) findViewById(R.id.tv1);
        mMessageText = (TextView) findViewById(R.id.tv2);



    }

    private void layoutView(View view, int left, int top, int width, int height) {
        MarginLayoutParams margins = (MarginLayoutParams) view.getLayoutParams();
        final int leftWithMargins = left + margins.leftMargin;
        final int topWithMargins = top + margins.topMargin;

        view.layout(leftWithMargins, topWithMargins,
                leftWithMargins + width, topWithMargins + height);
    }

    private int getWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    private int getMeasuredWidthWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredWidthImage(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private int getMeasuredHeightWithMargins(View child) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        return child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int Size = MeasureSpec.getSize(widthMeasureSpec);

        int widthUsed  = getPaddingLeft() + getPaddingRight();

        //int widthUsed = 0;
        int heightUsed = 0+getPaddingTop();
        //ltrb
        measureChildWithMargins(mIcon,
                widthMeasureSpec, widthUsed,
                getMeasuredHeightWithMargins(mIcon), heightUsed);
        heightUsed  += getPaddingTop() + getMeasuredHeightWithMargins(mIcon);

        widthUsed += getMeasuredWidthWithMargins(mProfileImage);

   /*     measureChildWithMargins(mIcon,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);*/
        measureChildWithMargins(mProfileImage,
                widthMeasureSpec, widthUsed,
                heightMeasureSpec, heightUsed);
        widthUsed += getMeasuredWidthWithMargins(mProfileImage);
        heightUsed += getMeasuredHeightWithMargins(mProfileImage);

        measureChildWithMargins(mAuthorText,
                getMeasuredWidthWithMargins(mAuthorText), 0,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mAuthorText);

        measureChildWithMargins(mMessageText,
                getMeasuredWidthWithMargins(mMessageText), 0,
                heightMeasureSpec, heightUsed);
        heightUsed += getMeasuredHeightWithMargins(mMessageText);


        int heightSize = heightUsed + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        int currentTop = 0;//paddingTop;
        // ltrb


        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mIcon.getLayoutParams();

        layoutView(mIcon, 0, 0,
                mIcon.getMeasuredWidth(),
                mIcon.getMeasuredHeight());
        currentTop += getHeightWithMargins(mIcon);



        layoutView(mProfileImage, 0, currentTop,
                mProfileImage.getMeasuredWidth(),
                mProfileImage.getMeasuredHeight());

        final int contentLeft = getWidthWithMargins(mProfileImage) ;
        final int contentWidth = r - l - contentLeft - getPaddingRight();

        final MarginLayoutParams lp2 = (MarginLayoutParams) mProfileImage.getLayoutParams();


        currentTop += getHeightWithMargins(mProfileImage);


        layoutView(mAuthorText,  0,currentTop ,
                mAuthorText.getMeasuredWidth(),
                mAuthorText.getMeasuredHeight());
        currentTop += getHeightWithMargins(mAuthorText);

        layoutView(mMessageText,   (getWidth()/2)-(getMeasuredWidthWithMargins(mMessageText)/2), currentTop,
                mMessageText.getMeasuredWidth(),
                mMessageText.getMeasuredHeight());
        //currentTop += getHeightWithMargins(mMessageText);


    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


}