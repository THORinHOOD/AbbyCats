package com.task.thorinhood.abbyycats.recycler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task.thorinhood.abbyycats.R;
import com.task.thorinhood.abbyycats.activities.ItemActivity;
import com.task.thorinhood.abbyycats.activities.MainActivity;
import com.task.thorinhood.abbyycats.models.Cat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Cat> cats = new ArrayList<>();
    private MainActivity activity;

    public RecyclerViewAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.catitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        if (i < 0 || i >= cats.size()) {
            return;
        }

        final Cat cat = cats.get(i);
        viewHolder.setName(cat.getName());
        viewHolder.getImage().setContentDescription(cat.getName());
        MainActivity.imageAdapter.setImage(viewHolder.getImage(), cat);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ItemActivity.class);
                intent.putExtra("name", cat.getName());
                intent.putExtra("link", cat.getLink());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    public void addCats(Cat... cats) {
        this.cats.addAll(Arrays.asList(cats));
        notifyDataSetChanged();
    }

    public void addCats(List<Cat> cats) {
        this.cats.addAll(cats);
        notifyDataSetChanged();
    }

    public void clearCats() {
        this.cats.clear();
        notifyDataSetChanged();
    }
}
