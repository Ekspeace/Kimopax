package com.ekspeace.kimopax.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekspeace.kimopax.R;
import com.squareup.picasso.Picasso;

public class DirectorDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_details);

        InitView();
    }
    private void InitView(){
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String title = intent.getStringExtra("TITLE");
        String image = intent.getStringExtra("IMAGE");
        String info = intent.getStringExtra("INFO");

        TextView tvName = findViewById(R.id.director_details_name);
        TextView tvTitle = findViewById(R.id.director_details_title);
        TextView tvInfo = findViewById(R.id.director_details_info);
        ImageView ivImage = findViewById(R.id.director_details_image);
        Picasso.get().load(image).into(ivImage);

        tvName.setText(name);
        tvInfo.setText(Html.fromHtml(info));
        tvTitle.setText(title);

        findViewById(R.id.director_details_back).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.director_details_home).setOnClickListener(view -> startActivity(new Intent(this, Services.class)));

    }
}