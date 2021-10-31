package com.ekspeace.kimopax.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Constants.PopUp;
import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ResetPassword extends AppCompatActivity {
    private TextInputEditText etEmail;
    private LinearLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        InitView();
        Event();
    }
    private void InitView() {
        etEmail = findViewById(R.id.reset_password_email_input);
        loading = findViewById(R.id.reset_password_progressbar);
    }
    private void Event(){
        findViewById(R.id.reset_password_login_button).setOnClickListener(v -> {
            startActivity(new Intent(ResetPassword.this, Login.class));
        });
        findViewById(R.id.reset_password_button).setOnClickListener(v -> ResetPassword());
    }
    private void ResetPassword(){
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter your email");
            return;
        }
        loading.setVisibility(View.VISIBLE);
        if (Common.isOnline(ResetPassword.this)) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(ResetPassword.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this, Login.class));
                            } else {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(ResetPassword.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassword.this, Login.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(ResetPassword.this, "Error! :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            loading.setVisibility(View.GONE);
            PopUp.Network(ResetPassword.this);

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Connection(NetworkConnectionEvent event) {
        if (event.isNetworkConnected()) {
            ResetPassword();
        }
    }
}