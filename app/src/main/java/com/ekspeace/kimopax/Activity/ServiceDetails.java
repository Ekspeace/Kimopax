package com.ekspeace.kimopax.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekspeace.kimopax.R;

import org.w3c.dom.Text;

public class ServiceDetails extends AppCompatActivity {
    TextView tvServiceName, tvServiceHeader,  tvServiceInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String header = intent.getStringExtra("HEADER");
        String info = intent.getStringExtra("INFO");

        InitView(name, header, info);
    }
    private void InitView(String name, String header, String info){
        tvServiceName = findViewById(R.id.service_details_service_name);
        tvServiceHeader = findViewById(R.id.service_details_service_header);
        tvServiceInfo = findViewById(R.id.service_details_service_info);

        tvServiceName.setText(name);
        tvServiceHeader.setText(Html.fromHtml(header));
        tvServiceInfo.setText(Html.fromHtml(info));

        findViewById(R.id.service_details_back).setOnClickListener(view -> onBackPressed());
    }
}