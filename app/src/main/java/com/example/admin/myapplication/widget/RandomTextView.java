package com.example.admin.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.admin.myapplication.R;

import java.util.Random;

/**
 * Created by admin on 2016/2/26.
 */
public class RandomTextView extends View{

    private String mTitleText;
    private int mTitleTextColor;
    private int mTitleTextSize;
    private Paint mPaint;
    private Rect rect;

    public RandomTextView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RandomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .CustomTitleView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for(int i=0;i<indexCount;i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.CustomTitleView_customText:
                    mTitleText=typedArray.getString(attr);
                    break;
                case R.styleable.CustomTitleView_customTextColor:
                    mTitleTextColor=typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_customTextSize:
                    mTitleTextSize=typedArray.getDimensionPixelSize(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources()
                                    .getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        rect = new Rect();
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(), rect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 随机生成四个数字
                mTitleText=getRandomText();
                invalidate();
            }
        });
    }

    private String getRandomText() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<4;i++){
            int number = random.nextInt(10);
            sb.append(number);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 当我们设置明确的宽度和高度时，系统帮我们测量的结果就是我们设置的结果，
         * 当我们设置为WRAP_CONTENT,或者MATCH_PARENT系统帮我们测量的结果就是MATCH_PARENT的长度。
         所以，当设置了WRAP_CONTENT时，我们需要自己进行测量，即重写onMesure方法”
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            width=rect.width()+getPaddingLeft()+getPaddingRight();
        }

        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            height=rect.height()+getPaddingTop()+getPaddingBottom();
        }

        setMeasuredDimension(width,height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText,getWidth()/2-rect.width()/2 ,getHeight()/2+rect.height()/2,
                mPaint);
    }
}
