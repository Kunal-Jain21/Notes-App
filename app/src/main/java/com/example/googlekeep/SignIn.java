package com.example.googlekeep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {

    EditText email_edit_text, password_edit_text, confirm_passwords_edit_text;
    Button log_in_btn;
    ProgressBar progress_bar;
    TextView login_text_view_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        log_in_btn = findViewById(R.id.log_in_btn);
        progress_bar = findViewById(R.id.progress_bar);
        login_text_view_btn = findViewById(R.id.login_text_view_btn);

        login_text_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, CreateAccount.class));
            }
        });

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    void loginUser() {
        String email = email_edit_text.getText().toString();
        String password = password_edit_text.getText().toString();

        if (validateData(email, password)) {
            loginAccountInFirebase(email, password);
        }
        else {
            return;
        }
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            changeInProgress(false);
            if (task.isSuccessful()) {
                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                    // go to Main Page
                    startActivity(new Intent(SignIn.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(this, "Email not verified, Please verify your Email", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        });
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progress_bar.setVisibility(View.VISIBLE);
            log_in_btn.setVisibility(View.GONE);
        }
        else {
            progress_bar.setVisibility(View.GONE);
            log_in_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
        String specialCharRegex= ".*[@#!$%^&+=].*";
        String UpperCaseRegex= ".*[A-Z].*";
        String NumberRegex= ".*[0-9].*";

        // compiling pattern
        Pattern specialCharPattern = Pattern.compile(specialCharRegex);
        Pattern upperCasePattern = Pattern.compile(UpperCaseRegex);
        Pattern numberPattern = Pattern.compile(NumberRegex);

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_edit_text.setError("Email is invalid");
            return false;
        }

        // checking password field
        if ((password.length() <8)) {
            password_edit_text.setError("Password must be of atleast 8 character");
            return false;
        }

        if (!upperCasePattern.matcher(password).matches()) {
            password_edit_text.setError("Upper Character missing");
            return false;
        }
        if (!numberPattern.matcher(password).matches()) {
            password_edit_text.setError("Number missing");
            return false;
        }
        if (!specialCharPattern.matcher(password).matches()) {
            password_edit_text.setError("Special Character missing");
            return false;
        }

        return true;
    }
}