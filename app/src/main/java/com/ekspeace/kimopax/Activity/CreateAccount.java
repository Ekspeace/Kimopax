package com.ekspeace.kimopax.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Constants.PopUp;
import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.Model.User;
import com.ekspeace.kimopax.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.paperdb.Paper;

import static java.security.AccessController.getContext;

public class CreateAccount extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private LinearLayout loading;
    private CheckBox cbTermsOfService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        InitView();
        Events();
    }
    private void InitView() {
        etName = findViewById(R.id.create_account_name_input);
        etEmail = findViewById(R.id.create_account_email_input);
        etPassword = findViewById(R.id.create_account_password_input);
        etConfirmPassword = findViewById(R.id.create_account_confirm_password_input);
        loading = findViewById(R.id.create_account_progressbar);
        cbTermsOfService = findViewById(R.id.create_account_check_terms);

        Paper.init(this);
    }

    private void Events(){
        findViewById(R.id.create_account_button).setOnClickListener(view -> CreateAccountProcess());
        findViewById(R.id.create_account_login).setOnClickListener(view -> startActivity(new Intent(this, Login.class)));
    }
    private void CreateAccountProcess(){
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String name = etName.getText().toString();
        final String confirm = etConfirmPassword.getText().toString();

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
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password is Required.");
                error = true;
            } else if (password.length() < 6) {
                etPassword.setError("Password Must be greater than 6 Characters");
                error = true;
            }
            if (!password.equals(confirm)) {
                etPassword.setError("Password does not match");
                etConfirmPassword.setError("Password does not match");
                error = true;
            }
            if(!cbTermsOfService.isChecked()){
                Toast.makeText(this, "Please accept the terms of services", Toast.LENGTH_SHORT).show();
                error = true;
            }
            if (error)
                return;
        }
        loading.setVisibility(View.VISIBLE);
        if (Common.isOnline(this)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(id);
                        User user = new User();
                        user.setName(name);
                        user.setEmail(email);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(CreateAccount.this,"Account created successfully",Toast.LENGTH_LONG).show();
                                Paper.book().destroy();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(CreateAccount.this, "Error ! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(CreateAccount.this, "Error ! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(CreateAccount.this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            loading.setVisibility(View.GONE);
            PopUp.Network(this);
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
            CreateAccountProcess();
        }
    }
}