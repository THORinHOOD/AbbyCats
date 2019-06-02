package com.task.thorinhood.abbyycats.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.task.thorinhood.abbyycats.R;
import com.task.thorinhood.abbyycats.models.Cat;

public class ItemActivity extends AppCompatActivity {

    private Cat cat;
    private ImageView imageView;
    private TextView caption;
    private Button backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        String catName = getIntent().getExtras().getString("name");
        String catLink = getIntent().getExtras().getString("link");
        cat = new Cat(catName, catLink);

        imageView = findViewById(R.id.mainImage);
        imageView.setContentDescription(cat.getName());
        caption = findViewById(R.id.caption);
        backBtn = findViewById(R.id.button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        MainActivity.imageAdapter.setImage(imageView, cat);
        caption.setText(cat.getName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }
}
