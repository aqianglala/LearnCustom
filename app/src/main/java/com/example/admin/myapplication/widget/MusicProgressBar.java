package com.example.admin.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.admin.myapplication.R;

/**
 * Created by admin on 2016/2/27.
 */
public class MusicProgressBar extends View{

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mDotCount;
    private int mSplitSize;
    private Bitmap mImage;
    private Paint mPaint;
    private int mCurrentCount;
    private int itemSize;
    private int center;
    private int radius;
    private float downY;
    private float moveY;

    public MusicProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MusicProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .MusicProgressBar,
                defStyleAttr, 0);
        int indexCount = a.getIndexCount();
        for(int i=0;i<indexCount;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.MusicProgressBar_MusicFirstColor:
                    mFirstColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.MusicProgressBar_MusicSecondColor:
                    mSecondColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.MusicProgressBar_MusicCircleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue
                            .COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MusicProgressBar_MusicDotCount:
                    mDotCount = a.getInt(attr, 10);
                    break;
                case R.styleable.MusicProgressBar_MusicSplitSize:
                    mSplitSize = a.getInt(attr, 20);
                    break;
                case R.styleable.MusicProgressBar_MusicBg:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 先画周围
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mCircleWidth);

        // 计算每个小块的长度
        itemSize = (360 - mSplitSize * mDotCount) / mDotCount;
        // 计算圆心和半径
        center = getWidth() / 2;
        radius = center - mCircleWidth / 2;
        // 画小块
        drawOval(canvas);

        //计算内切正方形
        int realRadius = getWidth() / 2 - mCircleWidth;
        //内切正方形的边长的一半
        int centerSquare=(int) (Math.sqrt(2)*1.0f/2*realRadius);

        Rect rect = new Rect(center-centerSquare,center-centerSquare,center+centerSquare,center+centerSquare);
        mPaint.setStyle(Paint.Style.FILL);
        // 如果图片的宽度大于内切正方形的边长，则填充内切正方形，否则，将图片画到中间
        if(mImage.getWidth()>centerSquare*2||mImage.getHeight()>centerSquare*2){
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }else{
            rect.left=center-mImage.getWidth()/2;
            rect.top=center-mImage.getHeight()/2;
            rect.right=center+mImage.getWidth()/2;
            rect.bottom=center+mImage.getHeight()/2;
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }
    }

    private void drawOval(Canvas canvas) {
        RectF rectF = new RectF(center-radius, center-radius, center + radius, center + radius);
        for(int i=0;i<mDotCount;i++){
            mPaint.setColor(mFirstColor);
            canvas.drawArc(rectF,(itemSize+mSplitSize)*i,itemSize,false,mPaint);
        }

        for(int i=0;i<mCurrentCount;i++){
            mPaint.setColor(mSecondColor);
            canvas.drawArc(rectF,(itemSize+mSplitSize)*i,itemSize,false,mPaint);
        }
    }

    public void down(){
        mCurrentCount--;
        if(mCurrentCount<0){
            mCurrentCount=0;
        }
        postInvalidate();
    }

    public void up(){
        mCurrentCount++;
        if(mCurrentCount>mDotCount){
            mCurrentCount=mDotCount;
        }
        postInvalidate();
    }

}
