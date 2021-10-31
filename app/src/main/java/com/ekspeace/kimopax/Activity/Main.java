package com.ekspeace.kimopax.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ekspeace.kimopax.R;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_login_button).setOnClickListener(view -> startActivity(new Intent(this, Login.class)));
        findViewById(R.id.main_create_account_button).setOnClickListener(view -> startActivity(new Intent(this, CreateAccount.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}