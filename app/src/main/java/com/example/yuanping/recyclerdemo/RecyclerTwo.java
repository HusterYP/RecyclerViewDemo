package com.example.yuanping.recyclerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;


public class RecyclerTwo extends AppCompatActivity {

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
        recyclerView.addOnItemTouchListener(new OnItemTouch(this));
    }
}
