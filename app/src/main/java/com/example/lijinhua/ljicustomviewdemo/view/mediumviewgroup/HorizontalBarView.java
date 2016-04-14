package com.example.lijinhua.ljicustomviewdemo.view.mediumviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.lijinhua.ljicustomviewdemo.R;

/**
 * Created by lijinhua on 2016/4/13.
 * <p/>
 * 自定义View
 * https://medium.com/android-news/prefmatters-using-custom-views-in-android-to-improve-performance-part-1-4dc9bdd75396#.ngdt7jyxh
 * 源码:
 * https://gist.github.com/alphamu/3c893704274e14f83903
 */
public class HorizontalBarView extends View {

    String mText = "";

    Paint mBarPaintEmpty = new Paint();
    Paint mBarPaintFill = new Paint();
    Paint mFillTextPaint = new Paint();
    Paint mEmptyTextPaint = new Paint();

    int mEmptyColor;
    int mFillColor;
    int mEmptyTextColor;
    int mFillTextColor;
    int mTextSize = 15;
    int mTextPadding = mTextSize;
    float mValue = 30.0f;
    int mTextWidth = 0;
    int mHalfStrokeWidth;

    // 边框底下的矩形大小
    Rect mBarRect = new Rect();
    // 文字的矩形大小
    Rect mTextRect = new Rect();

    public HorizontalBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public HorizontalBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalBarView(Context context) {
        super(context);
        init();
    }

    private void init(Context context, AttributeSet attrs) {
        // attrs contains the raw values for the XML attributes
        // that were specified in the layout, which don't include
        // attributes set by styles or themes, and which may have
        // unresolved references. Call obtainStyledAttributes()
        // to get the final values for each attribute.
        //
        // This call uses R.styleable.PieChart, which is an array of
        // the custom attributes that were declared in attrs.xml.
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalBarView, 0, 0);

        String fontPath = null;

        try {
            // Retrieve the values from the TypedArray and store into
            // fields of this class.
            //
            // The R.styleable.PieChart_* constants represent the index for
            // each custom attribute in the R.styleable.PieChart array.
            mEmptyColor = a.getColor(R.styleable.HorizontalBarView_bar_emptyColor, Color.GRAY);
            mFillColor = a.getColor(R.styleable.HorizontalBarView_bar_fillColor, Color.BLUE);
            mEmptyTextColor = a.getColor(R.styleable.HorizontalBarView_bar_emptyTextColor, Color.BLACK);
            mFillTextColor = a.getColor(R.styleable.HorizontalBarView_bar_fillTextColor, Color.WHITE);
            mTextSize = a.getDimensionPixelSize(R.styleable.HorizontalBarView_bar_textSize, mTextSize);
            mText = a.getString(R.styleable.HorizontalBarView_bar_text);
            mTextPadding = a.getDimensionPixelSize(R.styleable.HorizontalBarView_bar_textPadding, mTextPadding);
            mValue = a.getFloat(R.styleable.HorizontalBarView_bar_fillPercentage, mValue);
            fontPath = a.getString(R.styleable.HorizontalBarView_bar_fontPath);
            if (mText == null) {
                mText = "";
            }

        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }


        mBarPaintEmpty.setColor(mEmptyColor);
        mBarPaintEmpty.setStyle(Paint.Style.FILL);

        mBarPaintFill.setColor(mFillColor);
        mBarPaintFill.setStyle(Paint.Style.FILL);

        mEmptyTextPaint.setColor(mEmptyTextColor);
        mEmptyTextPaint.setStyle(Paint.Style.FILL);
        mEmptyTextPaint.setTextSize(mTextSize);
        mEmptyTextPaint.setAntiAlias(true);

        mFillTextPaint.setColor(mFillTextColor);
        mFillTextPaint.setStyle(Paint.Style.FILL);
        mFillTextPaint.setTextSize(mTextSize);
        mFillTextPaint.setAntiAlias(true);

        if (fontPath != null && fontPath.length() > 0) {
            Typeface t = Typeface.createFromAsset(context.getAssets(), fontPath);
            mFillTextPaint.setTypeface(t);
            mEmptyTextPaint.setTypeface(t);
        }

        mTextWidth = (int) mEmptyTextPaint.measureText(mText);

    }

    private void init() {
        mBarPaintEmpty.setColor(Color.GRAY);
        mBarPaintEmpty.setStyle(Paint.Style.FILL);

        mBarPaintFill.setColor(Color.BLUE);
        mBarPaintFill.setStyle(Paint.Style.FILL);

        mEmptyTextPaint.setColor(Color.BLACK);
        mEmptyTextPaint.setAntiAlias(true);

        mFillTextPaint.setColor(Color.WHITE);
        mFillTextPaint.setAntiAlias(true);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        int minHeight = super.getSuggestedMinimumHeight();
        if (minHeight <= 0) {
            return mTextSize + (mTextPadding * 2);
        } else {
            return minHeight;
        }

    }

    @Override
    protected int getSuggestedMinimumWidth() {
        int minWidth = super.getSuggestedMinimumHeight();
        if (minWidth <= 0) {
            return mTextSize + (mTextPadding * 4);
        } else {
            return minWidth;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 得到画边框(画笔)的宽度的一半
        mHalfStrokeWidth = mBarPaintEmpty.getStrokeWidth() < 2 ? (int) mBarPaintEmpty.getStrokeWidth() : (int) mBarPaintEmpty.getStrokeWidth() / 2;

        int ypad = getPaddingTop() + getPaddingBottom();
        int xpad = getPaddingLeft() + getPaddingRight();

        mBarRect.left = getPaddingLeft() + mHalfStrokeWidth;
        mBarRect.top = getPaddingTop() + mHalfStrokeWidth;
        mBarRect.right = getWidth() - getPaddingRight() - mHalfStrokeWidth;
        mBarRect.bottom = getHeight() - getPaddingBottom() - mHalfStrokeWidth;

        mEmptyTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        //recalculate this on draw, so it's close to the fill
        mTextRect.left = getPaddingLeft() + mHalfStrokeWidth;
        // isInEditMode() 就是为了在可视化编辑的时候不报错
        mTextRect.top = mBarRect.centerY() + mTextRect.height() / 2 + mHalfStrokeWidth; // 居中
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - mTextWidth + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w) - mTextWidth, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画有值的部分
        mBarRect.right = (int) (mBarRect.right * (mValue / 100f));
        int fillRight = mBarRect.right;
        canvas.drawRect(mBarRect, mBarPaintFill);

        // Draw the empty part starting from the end of the filled part

        int originalLeft = mBarRect.left;
        mBarRect.left = mBarRect.right;
        mBarRect.right = getWidth() - getPaddingRight() - mHalfStrokeWidth;
        int emptyRight = mBarRect.right;
        canvas.drawRect(mBarRect, mBarPaintEmpty);

        mBarRect.left = originalLeft;
        //double the padding to account for the left and right sides.
//        if (emptyRight - fillRight > mTextWidth + (mTextPadding * 2)) {
//            //recalculate left for text to keep it close to the fill line.
//            mTextRect.left = fillRight + mTextPadding;
//            canvas.drawText(mText, mTextRect.left, mTextRect.top, mEmptyTextPaint);
//
//        } else {
//            //recalculate left for text to keep it close to the fill line.
//            mTextRect.left = fillRight - mTextWidth - mTextPadding;
//            canvas.drawText(mText,mTextRect.left, mTextRect.top, mFillTextPaint);
//        }
        // 居中
        mTextRect.left = getWidth() / 2 - mTextWidth / 2 + mHalfStrokeWidth;
        canvas.drawText(mText, mTextRect.left, mTextRect.top, mFillTextPaint);

    }
}
