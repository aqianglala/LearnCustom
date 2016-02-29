package com.example.admin.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by admin on 2016/2/29.
 */
public class VerticalLinearLayout extends ViewGroup{


    private final int mScreenHeight;
    private final Scroller mScroller;
    private int mScrollEnd;

    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;

        mScroller = new Scroller(context);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            int childCount = getChildCount();
            // 布局的实际高度
            MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
            params.height = mScreenHeight * childCount;
            setLayoutParams(params);

            for(int i=0;i<childCount;i++){
                View child = getChildAt(i);
                child.layout(0, mScreenHeight*i, r, mScreenHeight * (i+1));
            }
        }
    }
    private boolean isScrolling;
    private int mScrollStart;
    private int mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isScrolling){
            return super.onTouchEvent(event);
        }
        // 每一次都触摸事件都会获取当前触摸点的y轴坐标
        int y = (int) event.getY();

        // 初始化velocityTracker对象
        obtainVelocity(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 获得view相对于屏幕原点在y轴上的偏移量
                mScrollStart = getScrollY();
                Log.e("scroll","mScrollStart:"+mScrollStart);
                // 获得触摸点相对于其组件在y轴上偏移量
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                // 判断滑动方向，并控制边界
                int scrollY = getScrollY();
                int dy = mLastY - y;
                if(dy < 0 && scrollY + dy< 0){// 已经到达顶端，下拉多少，就往上滚动多少
                    dy = -scrollY;
                }
                if(dy>0 && scrollY +dy >getHeight()-mScreenHeight){// 已经到达底部，上拉多少，就往下滚动多少
                    dy = getHeight() - mScreenHeight - scrollY;
                }
                scrollBy(0,dy);
                mLastY=y;
                break;
            case MotionEvent.ACTION_UP:
                mScrollEnd = getScrollY();
                Log.e("scroll","mScrollEnd:"+mScrollEnd);
                int dScrollY = mScrollEnd - mScrollStart;
                if(dScrollY>0){// 向上滑动,滚动到下一页
                    if(shouldScrollToNext()){
                        mScroller.startScroll(0,mScrollEnd,0, mScreenHeight-dScrollY);
                    }else{
                        mScroller.startScroll(0,mScrollEnd,0,-dScrollY);
                    }
                }else{// 向下滑动
                    if(shouldScrollToPre()){
                        mScroller.startScroll(0,mScrollEnd,0,-mScreenHeight-dScrollY);
                    }
                    else
                    {
                        mScroller.startScroll(0, mScrollEnd, 0, -dScrollY);
                    }
                }
                isScrolling=true;
                postInvalidate();
                recycleVelocity();
                break;
        }
        return true;
    }

    private boolean shouldScrollToPre() {
        return mScrollStart-mScrollEnd>mScreenHeight/2 || Math.abs(getVelocity())>600;
    }

    private int getVelocity(){
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getYVelocity();
    }

    private VelocityTracker mVelocityTracker;

    private void obtainVelocity(MotionEvent event) {
        if(mVelocityTracker==null){
            mVelocityTracker=VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private boolean shouldScrollToNext() {
        return mScrollEnd - mScrollStart>mScreenHeight/2 || Math.abs(getVelocity())>600;
    }

    private int mCurrentPosition;
    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){//动画还没结束
            scrollTo(0,mScroller.getCurrY());
            postInvalidate();
        }else{
            int position = getScrollY() / mScreenHeight;
            if(position!=mCurrentPosition){
                if(mListener!=null){
                    mCurrentPosition=position;
                    mListener.onPageChange(position);
                }
            }
            isScrolling=false;
        }
    }

    private OnPageChangeListener mListener;
    public void setOnPageChangeListener(OnPageChangeListener listener){
        mListener=listener;
    }

    public interface OnPageChangeListener{
        void onPageChange(int currentPage);
    }

    /**
     * 释放资源
     */
    private void recycleVelocity()
    {
        if (mVelocityTracker != null)
        {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
