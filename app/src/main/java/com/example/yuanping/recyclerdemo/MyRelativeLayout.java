package com.example.yuanping.recyclerdemo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by yuanping on 3/10/18.
 */

public class MyRelativeLayout extends RelativeLayout {

    private Scroller scroller;

    public MyRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
    }

    public void onScroll(int dx) {
        if (this.getScrollX() != 0) {
            scroller.startScroll(this.getScrollX(), 0, dx, 0);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            this.scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

}
