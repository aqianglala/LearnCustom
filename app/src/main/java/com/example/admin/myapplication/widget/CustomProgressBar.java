package com.example.admin.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.admin.myapplication.R;

/**
 * Created by admin on 2016/2/26.
 */
public class CustomProgressBar extends View{

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mSpeed;

    private boolean isNext;
    private int mProgress;
    private int defSize;

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40,
                getResources()
                .getDisplayMetrics());
        // 获取自定义属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .CustomProgressBar, defStyleAttr, 0);
        int indexCount = a.getIndexCount();
        for(int i=0;i<indexCount;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor = a.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue
                            .COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_speed:
                    mSpeed = a.getInt(attr, 6);
                    break;
            }
        }
        a.recycle();
        // 在子线程中以一定的速度来更新界面
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    mProgress++;
                    if(mProgress==360){
                        mProgress=0;
                        if(!isNext){
                            isNext=true;
                        }else{
                            isNext=false;
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{//设置默认40dp
            width=defSize;
        }
        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{//设置默认40dp
            height=defSize;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 设置画笔
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mCircleWidth);
        paint.setAntiAlias(true);

        // 获取圆心坐标和半径
        int center = getWidth() / 2;
        int radius = getWidth() / 2 - mCircleWidth / 2;
        RectF rectF = new RectF(center-radius,center-radius,center+radius,center+radius);
        if(!isNext){// 第一圈
            paint.setColor(mFirstColor);
            canvas.drawCircle(center,center,radius,paint);
            paint.setColor(mSecondColor);
            canvas.drawArc(rectF,-90,mProgress,false,paint);
        }else{// 第二圈
            paint.setColor(mSecondColor);
            canvas.drawCircle(center,center,radius,paint);
            paint.setColor(mFirstColor);
            canvas.drawArc(rectF,-90,mProgress,false,paint);
        }
    }
}
