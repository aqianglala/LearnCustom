package com.example.admin.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.admin.myapplication.R;

/**
 * Created by admin on 2016/2/26.
 */
public class ImgTextView extends View{


    private String mImgText;
    private int mTextColor;
    private int mTextSize;
    private Bitmap mImage;
    private int mScaleType;
    private Rect mBound;
    private Paint mPaint;

    private int IMAGE_SCALE_FITXY=0;
    private int IMAGE_SCALE_CENTER=1;

    public ImgTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImgTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .ImgTextView, defStyleAttr, 0);
        int indexCount = a.getIndexCount();
        for(int i=0;i<indexCount;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.ImgTextView_ImgText:
                    mImgText = a.getString(attr);
                    break;
                case R.styleable.ImgTextView_ImgTextColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ImgTextView_ImgTextSize:
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue
                            .COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ImgTextView_ImgTextImgRec:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.ImgTextView_ImgTextScaleType:
                    mScaleType = a.getInt(attr, 0);
                    break;
            }
        }
        mBound = new Rect();
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mImgText,0,mImgText.length(),mBound);
        mPaint.setAntiAlias(true);
    }
    int width;
    int height;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            // wrapContent的情况
            int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            int desireByText = getPaddingLeft() + getPaddingRight() + mBound.width();

            width = Math.max(desireByImg, desireByText);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            // wrapContent的情况
            height=getPaddingTop()+getPaddingBottom()+mImage.getHeight()+mBound.height();
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画个框
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        // 先画文字，如果先画图片的话，文字可能就没地方放了
        if(mBound.width()>width){// 如果文字的长度比控件宽的话，则结尾处替换成。。。
            TextPaint textPaint = new TextPaint(mPaint);
            String text = TextUtils.ellipsize(mImgText, textPaint, width - getPaddingLeft() -
                    getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(text,getPaddingLeft(),height-getPaddingBottom(),mPaint);
        }else{// 正常画字
            canvas.drawText(mImgText,getPaddingLeft(),height-getPaddingBottom(),mPaint);
        }
        Rect rect = new Rect();
        // 画图片了
        if(mScaleType==IMAGE_SCALE_FITXY){
            rect.left=getPaddingLeft();
            rect.top=getPaddingTop();
            rect.right=width-getPaddingRight();
            rect.bottom=height-getPaddingBottom()-mBound.height();
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }else if(mScaleType== IMAGE_SCALE_CENTER){
            rect.left=width/2-mImage.getWidth()/2;
            rect.top=(height-mBound.height())/2 - mImage.getHeight()/2;
            rect.right=width/2+mImage.getWidth()/2;
            rect.bottom=(height-mBound.height())/2 + mImage.getHeight()/2;
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }
    }
}
