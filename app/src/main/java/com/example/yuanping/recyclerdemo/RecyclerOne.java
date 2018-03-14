package com.example.yuanping.recyclerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * 用于测试RecyclerView的addOnItemTouchListener方法中的关系
 */
public class RecyclerOne extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_one);
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_one);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.d("@HusterYP", String.valueOf("onInterceptTouchEvent  DOWN"));
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        Log.d("@HusterYP", String.valueOf("onInterceptTouchEvent  MOVE"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Log.d("@HusterYP", String.valueOf("onInterceptTouchEvent  UP"));
                        break;
                    }
                }
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_MOVE: {
                        Log.d("@HusterYP", String.valueOf("onTouchEvent MOVE"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        Log.d("@HusterYP", String.valueOf("onTouchEvent UP"));
                        break;
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}
