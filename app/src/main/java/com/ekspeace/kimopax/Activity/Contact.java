package com.ekspeace.kimopax.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekspeace.kimopax.R;
import com.google.android.material.textfield.TextInputEditText;

public class Contact extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        InitView();
    }
    private void InitView(){
       etName = findViewById(R.id.contact_name_input);
       etEmail = findViewById(R.id.contact_email_input);
       etMessage = findViewById(R.id.contact_message_input);

       findViewById(R.id.contact_back).setOnClickListener(view -> onBackPressed());
       findViewById(R.id.contact_button).setOnClickListener(view -> SubmitMessage());
       findViewById(R.id.contact_website_button).setOnClickListener(view -> Website());
       findViewById(R.id.contact_facebook_button).setOnClickListener(view -> Facebook());
    }
    private void SubmitMessage() {
        final String email = etEmail.getText().toString().trim();
        final String message = etMessage.getText().toString().trim();
        final String name = etName.getText().toString();

        boolean error = false;
        if (!error) {
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is Required.");
                error = true;
            }
            if (TextUtils.isEmpty(name)) {
                etName.setError("Name is required");
                error = true;
            }
            if (TextUtils.isEmpty(message)) {
                etMessage.setError("Message is Required.");
                error = true;
            }
            if (error)
                return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:info@kimopax.com"));
        intent.putExtra("subject", "my subject");
        intent.putExtra("body", "my message");
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Contact.this, "No email client configured. Please check.", Toast.LENGTH_SHORT).show();
        }
    }
    private void Website(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.kimopax.com"));
        startActivity(browserIntent);
    }
    private void Facebook(){
        Intent intent = null;
        //String pageId = "Pr1518c106elb2/";
        String facebookUrl = "https://www.facebook.com/kimopax/";
        Uri uri = Uri.parse(facebookUrl);
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo;
            if(isFacebookAppInstalled(this).isEmpty()) {
                applicationInfo = packageManager.getApplicationInfo(isFacebookAppInstalled(this), 0);
                if (applicationInfo.enabled) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + uri));
                }
            }

        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/kimopax/"));
        }
        startActivity(intent);
    }
    public static String isFacebookAppInstalled(Context context){
        if(context!=null) {
            PackageManager pm=context.getPackageManager();
            ApplicationInfo applicationInfo;

            try {
                applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
                return applicationInfo.enabled?"com.facebook.katana":"";
            } catch (Exception ignored) {
            }

            //Then check that if the facebook lite is installed or not
            try {
                applicationInfo = pm.getApplicationInfo("com.facebook.lite", 0);
                return applicationInfo.enabled?"com.facebook.lite":"";
            } catch (Exception ignored) {
            }

            //Then check the other facebook app using different package name is installed or not
            try {
                applicationInfo = pm.getApplicationInfo("com.facebook.android", 0);
                return applicationInfo.enabled?"com.facebook.android":"";
            } catch (Exception ignored) {
            }

            try {
                applicationInfo = pm.getApplicationInfo("com.example.facebook", 0);
                return applicationInfo.enabled?"com.example.facebook":"";
            } catch (Exception ignored) {
            }
        }
        return "";
    }
}