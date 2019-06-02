package com.task.thorinhood.abbyycats.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.task.thorinhood.abbyycats.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView name;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageView);
        name = itemView.findViewById(R.id.name);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public ImageView getImage() {
        return image;
    }

}
