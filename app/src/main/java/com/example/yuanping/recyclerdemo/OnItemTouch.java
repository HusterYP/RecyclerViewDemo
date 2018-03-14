package com.example.yuanping.recyclerdemo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Created by yuanping on 3/7/18.
 */

public class OnItemTouch implements RecyclerView.OnItemTouchListener {
    private VelocityTracker velocityTracker = VelocityTracker.obtain();
    private MyAdapter.MyViewHolder curHolder;
    private MyAdapter.MyViewHolder oldHolder;
    private boolean flag = true; //标志在一个完整的过程中手势速度只判断一次
    private int state = 0;//state表示Item的状态; 0表示关闭 , 1 表示打开
    private int MAX_WIDTH;//能滑动的最大值,这里设置为屏幕宽度的1/5
    private int x;
    private boolean dealEvent = true; //是否应该进行事件处理
    private final int MAX_VELOCITY = 100; //最大临界速度,即向右滑动速度超过此值时将关闭Item

    public OnItemTouch(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        MAX_WIDTH = metrics.widthPixels / 5;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                View view = rv.findChildViewUnder(e.getX(), e.getY());
                if (view != null) {
                    curHolder = (MyAdapter.MyViewHolder) rv.getChildViewHolder(view);
                    curHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClick();
                        }
                    });
                } else {
                    curHolder = null;
                }
                //如果当前有打开的Item,并且手指DOWN处非该打开Item,那么关闭它
                if (state == 1 && oldHolder != null && curHolder != oldHolder) {
                    closeItem();
                    dealEvent = false;
                    break;
                }
                x = (int) e.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //扩展: 通过具体速度的判断,还可以实现像QQ那样的右滑出现侧边菜单
                if (flag) {
                    velocityTracker.addMovement(e);
                    velocityTracker.computeCurrentVelocity(1000);
                    flag = false;
                    //如果横向滑动
                    //或者当前有打开的Item,并且手指DOWN处仍为该Item,那么应该交给onTouchEvent()处理Item的滑动事件
                    if (Math.abs(velocityTracker.getYVelocity()) < Math.abs(velocityTracker.getXVelocity())
                            || (state == 1 && curHolder != null && curHolder == oldHolder)) {
                        return true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                reset();
                break;
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (null != curHolder && curHolder.root.getScrollX() >= 0 && dealEvent) {
                    onScroll(e);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (dealEvent) {
                    if (curHolder != null) {
                        oldHolder = curHolder;
                    }
                    //先判断手指抬起时的速度,如果大于了MAX_VELOCITY,先关闭Item
                    // 需要注意的是这里并没有取绝对值,是因为只需要判断向右滑动时
                    if (velocityTracker.getXVelocity() > MAX_VELOCITY) {
                        closeItem();
                    } else if (curHolder.root.getScrollX() > MAX_WIDTH / 4) {
                        openItem();
                    } else {
                        closeItem();
                    }
                }
                reset();
                break;
            }
        }
        x = (int) e.getX();
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * 关闭Item,还需要判断的是下一次手指DOWN时,触摸的Item时是哪个
     */
    private void closeItem() {
        state = 0;
        oldHolder.root.onScroll(-oldHolder.root.getScrollX());
    }

    private void openItem() {
        state = 1;
        int startX = oldHolder.root.getScrollX() > MAX_WIDTH ? MAX_WIDTH : oldHolder.root.getScrollX();
        int dx = startX < MAX_WIDTH ? MAX_WIDTH - startX : 0;
        oldHolder.root.onScroll(dx);
    }

    private void onScroll(MotionEvent e) {
        int dx = (int) (e.getX() - x);
        //对于QQ而言,其实其应该还加入了最后的手势速度判断,来决定最终是否应该关闭该Item
        if (curHolder.root.getScrollX() <= MAX_WIDTH && dx < 0) {
            //这个是为了防止滑动到临界值时超出边界
            if (curHolder.root.getScrollX() - dx > MAX_WIDTH) {
                dx = curHolder.root.getScrollX() - MAX_WIDTH;
                state = 1;
            }
            if (curHolder.root.getScrollX() == 0 && dx > 0) {
                dx = 0;
            }
            curHolder.root.scrollBy(-dx, 0);
        }
        //在打开状态下,向右边滑动
        if (dx >= 0 && curHolder.root.getScrollX() > 0) {
            if (curHolder.root.getScrollX() - dx < 0) {
                dx = curHolder.root.getScrollX();
                state = 0;
            }
            curHolder.root.scrollBy(-dx, 0);
        }
    }

    private void reset() {
        flag = true;
        dealEvent = true;
        velocityTracker.clear();
    }

    private void onItemClick() {
        //如果有打开的Item那么先关闭该Item
        if (null != oldHolder && 1 == state) {
            closeItem();
        }
    }
}
