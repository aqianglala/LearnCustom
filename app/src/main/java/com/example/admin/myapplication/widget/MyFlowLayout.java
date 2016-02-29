package com.example.admin.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/2/29.
 */
public class MyFlowLayout extends ViewGroup{

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    /**
     * 首先要按流式布局排列，然后记录整个布局的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSise = MeasureSpec.getSize(heightMeasureSpec);

        int width=0;
        int height=0;
        int lineHeight=0;
        int lineWidth=0;

        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams cParams = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth()+cParams.leftMargin+cParams.rightMargin;
            int cHeight = child.getMeasuredHeight()+cParams.topMargin+cParams.bottomMargin;
            // 添加下个child时，宽度大于widthSize，则换行
            if(lineWidth+cWidth>widthSize){//换行
                width=Math.max(lineWidth,cWidth);
                //重新开启新行
                lineWidth=cWidth;
                //叠加当前行的高度
                height+=lineHeight;
                //新行的高度
                lineHeight+=cHeight;
            }else{// 不换行
                lineWidth+=cWidth;
                lineHeight= Math.max(lineHeight,cHeight);
            }
            //最后一行
            if(i==childCount-1){
                width=Math.max(lineWidth,width);
                height+=lineHeight;
            }
        }
        setMeasuredDimension((widthMode==MeasureSpec.EXACTLY)?widthSize:width,
                (heightMode==MeasureSpec.EXACTLY)?heightSise:height);
    }

    private List<List<View>> allViews = new ArrayList<>();
    private List<Integer> mLineHeight = new ArrayList<>();
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        allViews.clear();
        mLineHeight.clear();
        int lineHeight=0;
        int lineWidth=0;
        int childCount = getChildCount();
        List<View> lineViews=new ArrayList<>();
        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) child.getLayoutParams();
            if(lineWidth+cWidth+cParams.leftMargin+cParams.rightMargin>getWidth()){//需要换行
                allViews.add(lineViews);
                mLineHeight.add(lineHeight);
                //重置行宽
                lineWidth=0;
                //重置行元素
                lineViews=new ArrayList<>();
            }
            lineWidth = lineWidth + cParams.leftMargin + cParams.rightMargin + cWidth;
            lineHeight = Math.max(lineHeight, cHeight + cParams.topMargin + cParams.bottomMargin);
            lineViews.add(child);

        }
        //如果是最后一行
        allViews.add(lineViews);
        mLineHeight.add(lineHeight);

        int top=0;
        for(int i=0;i<allViews.size();i++){
            lineViews = allViews.get(i);
            lineHeight = mLineHeight.get(i);
            int left=0;
            for(int j=0;j<lineViews.size();j++){
                View child = lineViews.get(j);
                if(child.getVisibility()==View.GONE){
                    continue;
                }
                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();
                MarginLayoutParams cParams = (MarginLayoutParams) child.getLayoutParams();
                int cl=left+cParams.leftMargin;
                int ct=top+cParams.topMargin;
                int cr=cl+cWidth;
                int cb=ct+cHeight;
                child.layout(cl,ct,cr,cb);
                left+=cWidth+cParams.leftMargin+cParams.rightMargin;
            }
            top+=lineHeight;
        }
    }

}
