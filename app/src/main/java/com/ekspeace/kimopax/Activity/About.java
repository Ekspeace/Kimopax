package com.ekspeace.kimopax.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ekspeace.kimopax.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.about_back).setOnClickListener(view -> onBackPressed());
    }
}