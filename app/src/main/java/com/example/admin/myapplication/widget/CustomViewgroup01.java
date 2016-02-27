package com.example.admin.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2016/2/27.
 * 我们定义一个ViewGroup，内部可以传入0到4个childView，分别依次显示在左上角，右上角，左下角，右下角。
 */
public class CustomViewgroup01 extends ViewGroup {


    public CustomViewgroup01(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightTize = MeasureSpec.getSize(heightMeasureSpec);

        // 让子view去测量自身，在调用此方法后就可以使用getMeasureWidth等方法了
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int childCount = getChildCount();
        int lHeight = 0;
        int rHeight = 0;
        int tWidth = 0;
        int bWidth = 0;

        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            MarginLayoutParams cParams= (MarginLayoutParams) childView.getLayoutParams();
            if(i==0 || i==1){
                tWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
            }
            if(i==2 || i==3){
                bWidth+=cWidth+cParams.leftMargin+cParams.rightMargin;
            }
            if(i==0 || i==2){
                lHeight+=cHeight+cParams.topMargin+cParams.bottomMargin;
            }
            if(i==1 || i==3){
                rHeight+=cHeight+cParams.topMargin+cParams.bottomMargin;
            }
        }

        setMeasuredDimension((widthMode==MeasureSpec.EXACTLY)?widthSize:Math.max(tWidth,bWidth),
                (heightMode==MeasureSpec.EXACTLY)?heightTize:Math.max(lHeight,rHeight));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            MarginLayoutParams cParams= (MarginLayoutParams) childView.getLayoutParams();
            int cl=0;
            int ct=0;
            int cr=0;
            int cb=0;
            switch (i){
                case 0:
                    cl=cParams.leftMargin;
                    ct=cParams.topMargin;
                    break;
                case 1:
                    cl=getWidth()-cWidth-cParams.rightMargin-cParams.leftMargin;
                    ct=cParams.topMargin;
                    break;
                case 2:
                    cl=cParams.leftMargin;
                    ct=getHeight()-cHeight-cParams.topMargin-cParams.bottomMargin;
                    break;
                case 3:
                    cl=getWidth()-cWidth-cParams.rightMargin-cParams.leftMargin;
                    ct=getHeight()-cHeight-cParams.topMargin-cParams.bottomMargin;
                    break;
            }
            cr=cl+cWidth;
            cb=ct+cHeight;
            childView.layout(cl,ct,cr,cb);
        }
    }
}
