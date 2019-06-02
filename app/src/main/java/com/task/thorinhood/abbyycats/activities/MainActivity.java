package com.task.thorinhood.abbyycats.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.task.thorinhood.abbyycats.R;
import com.task.thorinhood.abbyycats.models.Cat;
import com.task.thorinhood.abbyycats.recycler.RecyclerViewAdapter;
import com.task.thorinhood.abbyycats.utils.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerAdapter;
    public static ImageAdapter imageAdapter;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        recyclerAdapter.addCats(getCats());

        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerAdapter.clearCats();
                imageAdapter.clearCache();
                recyclerAdapter.addCats(getCats());
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_green_dark);
    }

    private void initRecyclerView() {
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);

        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                recyclerAdapter.notifyDataSetChanged();
                timerHandler.postDelayed(this, 1000);
            }
        };

        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private List<Cat> getCats() {
        List<Cat> cats = new ArrayList<>();
        String[] names = getResources().getStringArray(R.array.names);
        String[] links = getResources().getStringArray(R.array.links);
        for (int i = 0; i < names.length; i++) {
            if (links.length > i) {
                cats.add(new Cat(names[i], links[i]));
                imageAdapter.loadImage(cats.get(i));
            }
        }
        return cats;
    }

}
