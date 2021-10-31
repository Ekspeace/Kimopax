package com.ekspeace.kimopax.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.kimopax.Activity.ResetPassword;
import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Constants.PopUp;
import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.Model.User;
import com.ekspeace.kimopax.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    private boolean isShown = false;
    private TextInputEditText etEmail, etPassword;
    private CheckBox cbRemember;
    private ImageView ivShowPassword;
    private LinearLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitView();
        Event();
    }
    private void InitView(){
        etEmail = findViewById(R.id.login_email_input);
        etPassword = findViewById(R.id.login_password_input);
        cbRemember = findViewById(R.id.login_remember_password);
        loading = findViewById(R.id.login_progressbar);
        ivShowPassword = findViewById(R.id.login_show_password);

        Paper.init(this);

    }
    private void Event(){
        String UserEmailKey = Paper.book().read(Common.UserEmailKey);
        String UserPasswordKey = Paper.book().read(Common.UserPasswordKey);
        cbRemember.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                if (UserEmailKey != null) {
                    etEmail.setText(UserEmailKey);
                    etPassword.setText(UserPasswordKey);
                }
                else{
                    Paper.book().destroy();
                }
            }
        });
        findViewById(R.id.login_create_account).setOnClickListener(view -> startActivity(new Intent(this, CreateAccount.class)));
        findViewById(R.id.login_button).setOnClickListener(view -> {LoginProcess();});
        findViewById(R.id.login_forgot_password).setOnClickListener(view -> startActivity(new Intent(this, ResetPassword.class)));
        ivShowPassword.setOnClickListener(view -> {
            if(isShown){
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isShown = false;
            } else{
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isShown = true;
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivShowPassword.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivShowPassword.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ivShowPassword.setVisibility(View.VISIBLE);
            }
        });
    }
    private void LoginProcess() {
        String userEmail = etEmail.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();

        boolean error = false;
        if (!error) {
            if (TextUtils.isEmpty(userEmail)) {
                etEmail.setError("Email is Required.");
                error = true;
            }
            if (TextUtils.isEmpty(userPassword)) {
                etPassword.setError("Password is Required.");
                ivShowPassword.setVisibility(View.GONE);
                error = true;
            } else if (userPassword.length() < 6) {
                etPassword.setError("Password Must be greater 5 Characters");
                ivShowPassword.setVisibility(View.GONE);
                error = true;
            }
            if (error)
                return;

        }
        loading.setVisibility(View.VISIBLE);
        if (cbRemember.isChecked()) {
            Paper.book().write(Common.UserEmailKey, userEmail);
            Paper.book().write(Common.UserPasswordKey, userPassword);
        }
        if (Common.isOnline(this)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(task.getResult()).getUser().getUid());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    User user = Objects.requireNonNull(task.getResult()).toObject(User.class);
                                    assert user != null;
                                    user.setName(task.getResult().get("name").toString());
                                    user.setEmail(task.getResult().get("email").toString());

                                    Common.currentUser = user;
                                    Intent myIntent = new Intent(Login.this, Services.class);
                                    myIntent.putExtra("LOGIN", "Your have logged in successfully");
                                    startActivity(myIntent);
                                }else {
                                    Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    loading.setVisibility(View.GONE);
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Error! :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Error! :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Please Check your credential and try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Error! :" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            LoginProcess();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}